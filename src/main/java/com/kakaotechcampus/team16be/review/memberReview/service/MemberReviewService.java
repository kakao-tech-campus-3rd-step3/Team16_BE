package com.kakaotechcampus.team16be.review.memberReview.service;

import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.group.service.GroupService;
import com.kakaotechcampus.team16be.review.common.service.ReviewService;
import com.kakaotechcampus.team16be.review.memberReview.domain.Evaluation;
import com.kakaotechcampus.team16be.review.memberReview.domain.MemberReview;
import com.kakaotechcampus.team16be.review.memberReview.dto.CreateMemberReviewDto;
import com.kakaotechcampus.team16be.review.memberReview.repository.MemberReviewRepository;
import com.kakaotechcampus.team16be.user.domain.User;
import com.kakaotechcampus.team16be.user.exception.UserErrorCode;
import com.kakaotechcampus.team16be.user.exception.UserException;
import com.kakaotechcampus.team16be.user.repository.UserRepository;
import com.kakaotechcampus.team16be.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class MemberReviewService implements ReviewService<CreateMemberReviewDto, MemberReview> {

    private final MemberReviewRepository memberReviewRepository;
    private final GroupService groupService;
//    private final GroupMemberService groupMemberService;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public MemberReview createReview(User user, CreateMemberReviewDto createReviewDto) {
       Group targetGroup = groupService.findGroupById(createReviewDto.getGroupId());
       User reviewee = userRepository.findById(createReviewDto.getRevieweeId()).orElseThrow(()->new UserException(UserErrorCode.USER_NOT_FOUND));

//        groupMemberService.checkUserInGroup(user,targetGroup);
//        groupMemberService.checkUserStatus(reviewee, targetGroup);

        String content = createReviewDto.getContent();
        Evaluation evaluation = createReviewDto.getEvaluation();
        MemberReview memberReview = MemberReview.create(user, reviewee, targetGroup, content,evaluation);

        return memberReviewRepository.save(memberReview);
    }

    @Override
    public List<MemberReview> getAllReviews(User user, Long groupId) {
        Group targetGroup = groupService.findGroupById(groupId);

        return memberReviewRepository.findByRevieweeAndGroup(user, targetGroup);
    }
}
