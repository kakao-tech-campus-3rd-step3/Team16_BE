package com.kakaotechcampus.team16be.common.eventListener;

import com.kakaotechcampus.team16be.comment.repository.CommentRepository;
import com.kakaotechcampus.team16be.post.domain.Post;
import com.kakaotechcampus.team16be.post.repository.PostRepository;
import com.kakaotechcampus.team16be.report.ReportRepository;
import com.kakaotechcampus.team16be.report.domain.Report;
import com.kakaotechcampus.team16be.report.domain.TargetType;
import com.kakaotechcampus.team16be.user.domain.User;
import com.kakaotechcampus.team16be.user.repository.UserRepository;
import com.kakaotechcampus.team16be.user.service.UserService;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
@Slf4j
@Component
@RequiredArgsConstructor
public class UserScoreReportListener {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ReportRepository reportRepository;

    private static final Double BASE_PENALTY = 1.0;

    @EventListener
    @Transactional
    public void handleReportResolvedEvent(ReportResolvedEvent event) {
        Report resolvedReport = event.report();

        if (resolvedReport.isPenaltyApplied()) {
            log.info("신고(ID: {})는 이미 패널티가 적용된 묶음입니다. 건너뜁니다.", resolvedReport.getId());
            return;
        }

        // 2. 패널티를 받을 유저 찾기 (이전 로직과 동일)
        User userToPenalize = findUserToPenalize(resolvedReport);
        if (userToPenalize == null) {
            log.warn("신고(ID: {}) 대상 유저를 찾을 수 없어 패널티를 적용할 수 없습니다.", resolvedReport.getId());
            return;
        }

        // 3. "같은 날" 생성된 "동일 대상" 신고 묶음(batch)을 모두 조회
        LocalDate reportDate = resolvedReport.getCreatedAt().toLocalDate();
        LocalDateTime startOfDay = reportDate.atStartOfDay();
        LocalDateTime endOfDay = reportDate.plusDays(1).atStartOfDay();

        List<Report> reportBatch = reportRepository.findAllByTargetTypeAndTargetIdAndCreatedAtBetween(
                resolvedReport.getTargetType(),
                resolvedReport.getTargetId(),
                startOfDay,
                endOfDay
        );

        if (reportBatch.isEmpty()) return; // 로직상 발생하기 어려움

        // 4. 규칙 1: 중복 신고 가중치 계산
        double dailyDuplicateWeight = calculateDailyDuplicateWeight(reportBatch.size());

        // 5. 규칙 2: 신고자 신뢰도 가중치 계산
        // (이벤트를 발생시킨 관리자가 승인한 'resolvedReport'의 신고자를 기준으로 계산)
        User reporter = resolvedReport.getReporter();
        double reporterReliabilityWeight = calculateReporterReliability(reporter.getScore());

        // 6. 최종 패널티 계산
        double finalPenalty = BASE_PENALTY * dailyDuplicateWeight * reporterReliabilityWeight;

        // 7. 패널티 적용 (UserService 및 User 엔티티 수정 필요)
        try {
            // (UserService에 decreaseScoreByReport(User user, Double penalty)가 있다고 가정)
            userService.decreaseScoreByReport(userToPenalize, finalPenalty);
            log.info("신고 묶음(대상: {} {}, 날짜: {}) 처리: 사용자(ID: {})에게 {}점 패널티 적용",
                    resolvedReport.getTargetType(), resolvedReport.getTargetId(), reportDate, userToPenalize.getId(), finalPenalty);

            // 8. 묶음(batch)의 모든 신고에 "패널티 적용됨" 표시 (중복 방지)
            for (Report report : reportBatch) {
                report.markPenaltyApplied();
            }
            reportRepository.saveAll(reportBatch);

        } catch (Exception e) {
            log.error("신고 패널티 적용 중 오류 발생 (Report ID: {}): {}", resolvedReport.getId(), e.getMessage());
            // (트랜잭션이 롤백되어 패널티 적용이 취소됨)
        }
    }

    /**
     * 규칙 1: 중복 신고 가중치 계산
     */
    private double calculateDailyDuplicateWeight(int count) {
        if (count == 1) return 1.0;
        if (count == 2) return 1.25;
        if (count == 3) return 1.5;
        return 2.0; // 4건 이상 최대 2.0배
    }

    /**
     * 규칙 2: 신고자 신뢰도 가중치 계산 (예시 로직)
     * (기본 점수 40.0을 1.0배로 가정)
     */
    private double calculateReporterReliability(Double score) {
        if (score == null) score = 40.0;
        double baseScore = 40.0;

        // (점수 20점당 0.2배 변동 => 1점당 0.01배)
        double reliability = 1.0 + (score - baseScore) * 0.01;

        // 0.8 ~ 1.2배 사이로 제한
        return Math.max(0.8, Math.min(1.2, reliability));
    }

    /**
     * 신고 대상(Target)에 해당하는 유저(User)를 찾는 헬퍼 메서드
     */
    private User findUserToPenalize(Report report) {
        TargetType type = report.getTargetType();
        Long targetId = report.getTargetId();

        switch (type) {
            case USER:
                return userRepository.findById(targetId).orElse(null);
            case POST:
                Post post = postRepository.findById(targetId).orElse(null);
                if (post != null) {
                    return userRepository.findByNickname(post.getAuthor());
                }
                break;
            case COMMENT:
                log.warn("COMMENT 신고 패널티 로직이 구현되지 않았습니다.");
                break;
            case GROUP:
                break;
        }
        return null;
    }
}