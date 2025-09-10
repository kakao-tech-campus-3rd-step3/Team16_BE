package com.kakaotechcampus.team16be.review.groupReview.service;

import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.group.service.GroupService;
import com.kakaotechcampus.team16be.review.common.ResponseReviewDto;
import com.kakaotechcampus.team16be.review.common.ReviewCreateDto;
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
public class GroupReviewServiceImpl implements ReviewService<CreateGroupReviewDto, GroupReview>  {

    private final GroupReviewRepository groupReviewRepository;
    private final GroupService groupService;
//    private final UserService userService;


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

        return groupReviewRepository.findAllByGroup(targetGroup);
    }
}
