package com.kakaotechcampus.team16be.review.groupReview.service;

import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.group.service.GroupService;
import com.kakaotechcampus.team16be.review.common.service.ReviewService;
import com.kakaotechcampus.team16be.review.groupReview.domain.GroupReview;
import com.kakaotechcampus.team16be.review.groupReview.dto.CreateGroupReviewDto;
import com.kakaotechcampus.team16be.review.groupReview.repository.GroupReviewRepository;
import com.kakaotechcampus.team16be.user.domain.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupReviewServiceImpl implements ReviewService<CreateGroupReviewDto, GroupReview> {

    private final GroupReviewRepository groupReviewRepository;
    private final GroupService groupService;



    @Transactional
    @Override
    public GroupReview createReview(User user, CreateGroupReviewDto createGroupReviewDto) {
        Group targetGroup = groupService.findGroupById(createGroupReviewDto.getGroupId());
        /***
         * 아직 GroupMember를 구현을 못해서,, 일단 임시로 넣겠습니다!
         */
        // groupMemberService.isExistByUserAndGroup(user, targetGroup);
        GroupReview createdGroupReview = GroupReview.createReview(user, targetGroup, createGroupReviewDto.getContent());

        return groupReviewRepository.save(createdGroupReview);

    }

    @Override
    public List<GroupReview> getAllReviews(User user,Long groupId) {
        Group targetGroup = groupService.findGroupById(groupId);
        /***
         * 추후에 GroupMember 도메인 추가 시, 해당 유저 탈퇴 여부와 그룹 등을 연관지어서 유효성 검사 추가 예정입니다!
         */
        return groupReviewRepository.findAllByGroup(targetGroup);
    }
}
