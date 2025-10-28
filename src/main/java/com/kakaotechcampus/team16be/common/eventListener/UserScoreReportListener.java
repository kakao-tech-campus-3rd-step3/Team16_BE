package com.kakaotechcampus.team16be.common.eventListener;

import com.kakaotechcampus.team16be.comment.repository.CommentRepository;
import com.kakaotechcampus.team16be.post.domain.Post;
import com.kakaotechcampus.team16be.post.repository.PostRepository;
import com.kakaotechcampus.team16be.report.domain.Report;
import com.kakaotechcampus.team16be.report.domain.TargetType;
import com.kakaotechcampus.team16be.user.domain.User;
import com.kakaotechcampus.team16be.user.repository.UserRepository;
import com.kakaotechcampus.team16be.user.service.UserService;
import jakarta.transaction.Transactional;
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
    private final CommentRepository commentRepository;

    @EventListener
    @Transactional
    public void handleReportResolvedEvent(ReportResolvedEvent event) {
        Report report = event.report();
        TargetType type = report.getTargetType();
        Long targetId = report.getTargetId();

        User userToPenalize = null;

        try {
            switch (type) {
                case USER: //
                    userToPenalize = userService.findById(targetId);
                    break;

                case POST: //
                    Post post = postRepository.findById(targetId).orElse(null);
                    if (post != null) {
                        userToPenalize = userRepository.findByNickname(post.getAuthor());
                    }
                    break;

                case COMMENT: //

                    log.warn("COMMENT 신고에 대한 점수 차감 로직이 구현되지 않았습니다.");
                    break;
            }

            if (userToPenalize != null) {
                userService.decreaseScoreByReport(userToPenalize);
                log.info("신고 처리(ID: {}): 사용자(ID: {})의 점수를 차감했습니다.", report.getId(), userToPenalize.getId());
            }

        } catch (Exception e) {
            log.error("신고 처리(ID: {}) 점수 차감 중 오류 발생: {}", report.getId(), e.getMessage());
        }
    }
}