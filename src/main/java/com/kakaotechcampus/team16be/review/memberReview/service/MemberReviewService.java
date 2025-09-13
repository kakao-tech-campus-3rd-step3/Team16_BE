package com.kakaotechcampus.team16be.review.memberReview.service;

import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.group.service.GroupService;
import com.kakaotechcampus.team16be.groupMember.domain.GroupMember;
import com.kakaotechcampus.team16be.groupMember.service.GroupMemberService;
import com.kakaotechcampus.team16be.review.common.exception.ReviewException;
import com.kakaotechcampus.team16be.review.common.service.ReviewService;
import com.kakaotechcampus.team16be.review.memberReview.domain.Evaluation;
import com.kakaotechcampus.team16be.review.memberReview.domain.MemberReview;
import com.kakaotechcampus.team16be.review.memberReview.dto.CreateMemberReviewDto;
import com.kakaotechcampus.team16be.review.memberReview.repository.MemberReviewRepository;
import com.kakaotechcampus.team16be.user.domain.User;
import com.kakaotechcampus.team16be.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.kakaotechcampus.team16be.groupMember.domain.GroupMemberStatus.ACTIVE;
import static com.kakaotechcampus.team16be.review.common.exception.ErrorCode.ACTIVE_CANNOT_REVIEW;

@Service
@RequiredArgsConstructor

public class MemberReviewService implements ReviewService<CreateMemberReviewDto, MemberReview> {

    private final MemberReviewRepository memberReviewRepository;
    private final GroupService groupService;
    private final GroupMemberService groupMemberService;
    private final UserService userService;

    @Transactional
    @Override
    public MemberReview createReview(User user, CreateMemberReviewDto createReviewDto) {
       Group targetGroup = groupService.findGroupById(createReviewDto.getGroupId());
       User reviewee = userService.findById(createReviewDto.getRevieweeId());

        GroupMember groupMember = groupMemberService.findByGroupAndUser(targetGroup, user);

        groupMember.checkUserInGroup(user);
        if (groupMember.getStatus().equals(ACTIVE)) {
            throw new ReviewException(ACTIVE_CANNOT_REVIEW);
        }

        String content = createReviewDto.getContent();
        Evaluation evaluation = createReviewDto.getEvaluation();
        MemberReview memberReview = MemberReview.create(user, reviewee, targetGroup, content,evaluation);

        return memberReviewRepository.save(memberReview);
    }

    @Transactional(readOnly = true)
    @Override
    public List<MemberReview> getAllReviews(User user, Long groupId) {
        Group targetGroup = groupService.findGroupById(groupId);

        return memberReviewRepository.findByRevieweeAndGroup(user, targetGroup);
    }
}
