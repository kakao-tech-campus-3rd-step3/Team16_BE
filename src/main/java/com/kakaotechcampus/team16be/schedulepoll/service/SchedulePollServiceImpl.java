package com.kakaotechcampus.team16be.schedulepoll.service;

import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.group.service.GroupService;
import com.kakaotechcampus.team16be.schedulepoll.domain.PollTimeSlot;
import com.kakaotechcampus.team16be.schedulepoll.domain.PollVote;
import com.kakaotechcampus.team16be.schedulepoll.domain.SchedulePoll;
import com.kakaotechcampus.team16be.schedulepoll.dto.*;
import com.kakaotechcampus.team16be.schedulepoll.dto.SchedulePollListResponseDto.SchedulePollSummaryDto;
import com.kakaotechcampus.team16be.schedulepoll.dto.SchedulePollRequestDto.TimeSlotDto;
import com.kakaotechcampus.team16be.schedulepoll.dto.TimeSlotListResponseDto.TimeSlotWithVotes;
import com.kakaotechcampus.team16be.schedulepoll.repository.PollTimeSlotRepository;
import com.kakaotechcampus.team16be.schedulepoll.repository.PollVoteRepository;
import com.kakaotechcampus.team16be.schedulepoll.repository.SchedulePollRepository;
import com.kakaotechcampus.team16be.user.domain.User;
import com.kakaotechcampus.team16be.user.service.UserService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SchedulePollServiceImpl {

  private final SchedulePollRepository schedulePollRepository;
  private final PollTimeSlotRepository pollTimeSlotRepository;
  private final PollVoteRepository pollVoteRepository;
  private final UserService userService;
  private final GroupService groupService;

  @Transactional
  public SchedulePollResponseDto createSchedulePoll(Long groupId, Long userId, SchedulePollRequestDto request) {

    Group group = groupService.findGroupById(groupId);
    User user = userService.findById(userId);

    SchedulePoll schedulePoll = SchedulePoll.builder()
        .group(group)
        .creator(user)
        .title(request.title())
        .description(request.description())
        .build();
    schedulePollRepository.save(schedulePoll);

    for(LocalDate date : request.dates()){
      for(TimeSlotDto timeSlotDto : request.timeSlots()){
        PollTimeSlot pollTimeSlot = PollTimeSlot.builder()
            .schedulePoll(schedulePoll)
            .pollDate(date)
            .startTime(timeSlotDto.startTime())
            .endTime(timeSlotDto.endTime())
            .build();
        pollTimeSlotRepository.save(pollTimeSlot);
      }
    }
    return SchedulePollResponseDto.from(schedulePoll);
  }

  public SchedulePollListResponseDto getSchedulePolls(Long groupId) {
    List<SchedulePoll> polls = schedulePollRepository.findByGroupId(groupId);
    List<SchedulePollSummaryDto> summaries = polls.stream()
        .map(poll -> new SchedulePollSummaryDto(
            poll.getId(),
            poll.getTitle(),
            poll.getCreator().getNickname(),
            poll.getIsClosed(),
            poll.getCreatedAt().toString()
        ))
        .toList();

    return new SchedulePollListResponseDto(summaries);
  }

  // TODO 3: 시간대 목록 및 투표 현황 조회
  public TimeSlotListResponseDto getTimeSlotsWithVotes(Long pollId) {

    SchedulePoll schedulePoll = schedulePollRepository.findById(pollId)
        .orElseThrow(() -> new IllegalArgumentException("해당 투표가 존재하지 않습니다.")); //시험 이후에 예외처리 다시 적용하겠습니다.

    List<PollTimeSlot> timeSlots = pollTimeSlotRepository
        .findBySchedulePollIdOrderByPollDateAscStartTimeAsc(pollId);

    List<TimeSlotWithVotes> timeSlotDto = timeSlots.stream()
          .map(slot -> {
            List<PollVote> votes = pollVoteRepository.findByTimeSlotId(slot.getId());
            List<Long> userIds = votes.stream()
                  .map(vote -> vote.getUser().getId())
                  .toList();

             return new TimeSlotWithVotes(
                          slot.getId(),
                          slot.getPollDate(),
                          slot.getStartTime(),
                          slot.getEndTime(),
                          votes.size(),
                          userIds
             );
          })
          .toList();

    return new TimeSlotListResponseDto(
        schedulePoll.getId(),
        schedulePoll.getTitle(),
        schedulePoll.getIsClosed(),
        timeSlotDto
    );
  }

  @Transactional
  public VoteResponseDto submitVote(Long pollId, Long userId, SubmitVoteRequest request) {
    SchedulePoll schedulePoll = schedulePollRepository.findById(pollId)
                                                      .orElseThrow(() -> new IllegalArgumentException("해당 투표가 존재하지 않습니다."));

    if (schedulePoll.getIsClosed()) {
      throw new IllegalStateException("이미 마감된 투표입니다.");
    }

    User user = userService.findById(userId);
    pollVoteRepository.deleteBySchedulePollIdAndUserId(pollId, userId);

    List<PollVote> votes = new ArrayList<>();

    for (Long slotId : request.availableSlotIds()) {
      PollTimeSlot timeSlot = pollTimeSlotRepository.findById(slotId)
                                                    .orElseThrow(() -> new IllegalArgumentException("해당 시간대가 존재하지 않습니다."));

      PollVote pollVote = PollVote.builder()
                                  .timeSlot(timeSlot)
                                  .user(user)
                                  .build();

      votes.add(pollVote);
    }
    pollVoteRepository.saveAll(votes);

    return new VoteResponseDto("투표가 완료되었습니다", request.availableSlotIds().size());
  }

  public MyVoteResponseDto getMyVotes(Long pollId, Long userId) {
    List<PollVote> myVotes = pollVoteRepository
        .findBySchedulePollIdAndUserId(pollId, userId);

    List<Long> votedSlotIds = myVotes.stream()
                                     .map(vote -> vote.getTimeSlot().getId())
                                     .toList();
    return new MyVoteResponseDto(pollId, votedSlotIds);
  }

  @Transactional
  public void closePoll(Long pollId, Long userId) {
    SchedulePoll schedulePoll = schedulePollRepository.findById(pollId)
                                                      .orElseThrow(() -> new IllegalArgumentException("해당 투표가 존재하지 않습니다."));

    if (!schedulePoll.getCreator().getId().equals(userId)) {
      throw new IllegalArgumentException("투표 생성자만 마감할 수 있습니다.");
    }
    schedulePoll.close();
  }
}
