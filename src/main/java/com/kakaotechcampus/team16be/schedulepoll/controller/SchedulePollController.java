package com.kakaotechcampus.team16be.schedulepoll.controller;

import com.kakaotechcampus.team16be.common.annotation.LoginUser;
import com.kakaotechcampus.team16be.schedulepoll.dto.*;
import com.kakaotechcampus.team16be.schedulepoll.service.SchedulePollServiceImpl;
import com.kakaotechcampus.team16be.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "시간 투표", description = "그룹 일정 시간 투표 API")
public class SchedulePollController {

  private final SchedulePollServiceImpl schedulePollServiceImpl;

  @Operation(summary = "시간 투표 생성", description = "그룹에서 일정 시간 투표를 생성합니다")
  @PostMapping("/groups/{groupId}/schedule-polls")
  public ResponseEntity<SchedulePollResponseDto> createSchedulePoll(
      @LoginUser User user,
      @PathVariable Long groupId,
      @RequestBody SchedulePollRequestDto request
  ) {
    SchedulePollResponseDto response = schedulePollServiceImpl.createSchedulePoll(groupId, user.getId(), request);
    URI location = URI.create("/api/schedule-polls/" + response.pollId());
    return ResponseEntity.created(location).body(response);
  }

  @Operation(summary = "투표 목록 조회", description = "그룹의 모든 시간 투표를 조회합니다")
  @GetMapping("/groups/{groupId}/schedule-polls")
  public ResponseEntity<SchedulePollListResponseDto> getSchedulePolls(
      @PathVariable Long groupId
  ) {
    return ResponseEntity.ok(schedulePollServiceImpl.getSchedulePolls(groupId));
  }

  @Operation(summary = "투표 현황 조회", description = "시간대별 투표 현황을 조회합니다")
  @GetMapping("/schedule-polls/{pollId}/time-slots")
  public ResponseEntity<TimeSlotListResponseDto> getTimeSlotsWithVotes(
      @PathVariable Long pollId
  ) {
    return ResponseEntity.ok(schedulePollServiceImpl.getTimeSlotsWithVotes(pollId));
  }

  @Operation(summary = "투표 제출", description = "가능한 시간대에 투표합니다")
  @PostMapping("/schedule-polls/{pollId}/votes")
  public ResponseEntity<VoteResponseDto> submitVote(
      @LoginUser User user,
      @PathVariable Long pollId,
      @RequestBody SubmitVoteRequest request
  ) {
    return ResponseEntity.ok(schedulePollServiceImpl.submitVote(pollId, user.getId(), request));
  }

  @Operation(summary = "내 투표 조회", description = "내가 선택한 시간대를 조회합니다")
  @GetMapping("/schedule-polls/{pollId}/my-votes")
  public ResponseEntity<MyVoteResponseDto> getMyVotes(
      @LoginUser User user,
      @PathVariable Long pollId
  ) {
    return ResponseEntity.ok(schedulePollServiceImpl.getMyVotes(pollId, user.getId()));
  }

  @Operation(summary = "투표 마감", description = "투표를 마감합니다 (생성자만 가능)")
  @PatchMapping("/schedule-polls/{pollId}/close")
  public ResponseEntity<Void> closePoll(
      @LoginUser User user,
      @PathVariable Long pollId
  ) {
    schedulePollServiceImpl.closePoll(pollId, user.getId());
    return ResponseEntity.noContent().build();
  }
}
