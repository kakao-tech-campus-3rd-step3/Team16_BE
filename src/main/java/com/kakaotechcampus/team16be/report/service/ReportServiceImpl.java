package com.kakaotechcampus.team16be.report.service;


import com.kakaotechcampus.team16be.comment.domain.Comment;
import com.kakaotechcampus.team16be.comment.exception.CommentErrorCode;
import com.kakaotechcampus.team16be.comment.exception.CommentException;
import com.kakaotechcampus.team16be.comment.repository.CommentRepository;
import com.kakaotechcampus.team16be.comment.service.CommentService;
import com.kakaotechcampus.team16be.common.eventListener.groupEvent.DecreaseGroupScoreByReport;
import com.kakaotechcampus.team16be.common.eventListener.userEvent.DecreaseScoreByReport;
import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.group.exception.GroupException;
import com.kakaotechcampus.team16be.group.service.GroupService;
import com.kakaotechcampus.team16be.post.domain.Post;
import com.kakaotechcampus.team16be.post.exception.PostErrorCode;
import com.kakaotechcampus.team16be.post.exception.PostException;
import com.kakaotechcampus.team16be.post.repository.PostRepository;
import com.kakaotechcampus.team16be.report.ReportRepository;
import com.kakaotechcampus.team16be.report.domain.ReportStatus;
import com.kakaotechcampus.team16be.report.domain.TargetType;
import com.kakaotechcampus.team16be.report.dto.ReportRequestDto;
import com.kakaotechcampus.team16be.report.dto.ReportResolveRequestDto;
import com.kakaotechcampus.team16be.report.dto.ReportResponseDto;
import com.kakaotechcampus.team16be.report.domain.Report;
import com.kakaotechcampus.team16be.report.exception.ReportErrorCode;
import com.kakaotechcampus.team16be.report.exception.ReportException;
import com.kakaotechcampus.team16be.user.domain.User;
import com.kakaotechcampus.team16be.user.repository.UserRepository;
import com.kakaotechcampus.team16be.user.service.UserService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportServiceImpl implements ReportService{

  private final ReportRepository reportRepository;
  private final ApplicationEventPublisher eventPublisher;
  private final UserService userService;
  private final GroupService groupService;
  private final CommentRepository commentRepository;
  private final PostRepository postRepository;
  private final UserRepository userRepository;


  @Override
  @Transactional
  public ReportResponseDto createReport(User reporter, TargetType targetType, Long targetId,
      ReportRequestDto reportRequestDto) {

    Report report = Report.builder()
        .reporter(reporter)
        .targetType(targetType)
        .targetId(targetId)
        .reasonCode(reportRequestDto.reasonCode())
        .reason(reportRequestDto.reason())
        .build();

    Report saved = reportRepository.save(report);
    return toDto(saved);
  }

  @Override
  public ReportResponseDto getReport(Long reportId) {
    Report report =  reportRepository.findById(reportId)
        .orElseThrow(() -> new ReportException(ReportErrorCode.REPORT_NOT_FOUND));

    return toDto(report);
  }

  @Override
  public List<ReportResponseDto> getAllReports() {
    return reportRepository.findAll()
        .stream()
        .map(this::toDto)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public ReportResponseDto resolveReport(Long reportId, User adminUser,
      ReportResolveRequestDto reportResolveRequestDto) {

    Report report = reportRepository.findById(reportId)
        .orElseThrow(() -> new ReportException(ReportErrorCode.REPORT_NOT_FOUND));

    report.resolve(adminUser, reportResolveRequestDto.reportStatus());

    if(ReportStatus.RESOLVED.equals(report.getStatus())) {

      TargetType targetType = report.getTargetType();
      Long targetId = report.getTargetId();

      if (targetType.equals(TargetType.USER)) {
        eventPublisher.publishEvent(new DecreaseScoreByReport(userService.findById(targetId)));
      }

      if (targetType.equals(TargetType.GROUP)) {
        eventPublisher.publishEvent(
            new DecreaseGroupScoreByReport(groupService.findGroupById(targetId)));
      }

      if (targetType.equals(TargetType.COMMENT)) {
        Comment comment = commentRepository.findById(targetId)
                                           .orElseThrow(() -> new CommentException(CommentErrorCode.COMMENT_NOT_FOUND));

        User commentAuthor = comment.getUser();
        eventPublisher.publishEvent(new DecreaseScoreByReport(commentAuthor));
      }

      if (targetType.equals(TargetType.POST)) {
        Post post = postRepository.findById(targetId)
            .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));

        Group targetGroup = post.getGroup();
        User targetUser = userRepository.findByNickname(post.getAuthor());

        eventPublisher.publishEvent(new DecreaseGroupScoreByReport(targetGroup));
        eventPublisher.publishEvent(new DecreaseScoreByReport(targetUser));
      }
    }
    return toDto(report);
  }

  @Override
  @Transactional
  public void deleteReport(Long reportId) {
    Report report = reportRepository.findById(reportId)
        .orElseThrow(() -> new ReportException(ReportErrorCode.REPORT_NOT_FOUND));
    reportRepository.delete(report);
  }

  private ReportResponseDto toDto(Report report){
    return new ReportResponseDto(
      report.getId(),
      report.getReporter().getId(),
      report.getTargetType(),
      report.getTargetId(),
      report.getReasonCode(),
      report.getReason(),
      report.getStatus(),
      report.getCreatedAt() != null ? report.getCreatedAt().toString() : null,
      report.getUpdatedAt() != null ? report.getUpdatedAt().toString() : null
    );
  }
}
