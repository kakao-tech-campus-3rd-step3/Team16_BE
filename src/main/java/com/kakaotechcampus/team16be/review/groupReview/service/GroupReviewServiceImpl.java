package com.kakaotechcampus.team16be.review.groupReview.service;

import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.group.service.GroupService;
import com.kakaotechcampus.team16be.groupMember.service.GroupMemberService;
import com.kakaotechcampus.team16be.review.common.service.ReviewService;
import com.kakaotechcampus.team16be.review.groupReview.domain.GroupReview;
import com.kakaotechcampus.team16be.review.groupReview.dto.CreateGroupReviewDto;
import com.kakaotechcampus.team16be.review.groupReview.repository.GroupReviewRepository;
import com.kakaotechcampus.team16be.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupReviewServiceImpl implements ReviewService<CreateGroupReviewDto, GroupReview> {

    private final GroupReviewRepository groupReviewRepository;
    private final GroupService groupService;
    private final GroupMemberService groupMemberService;



    @Transactional
    @Override
    public GroupReview createReview(User user, CreateGroupReviewDto createGroupReviewDto) {
        Group targetGroup = groupService.findGroupById(createGroupReviewDto.getGroupId());

        groupMemberService.findByGroupAndUser(targetGroup, user);

        boolean checkMemberLeft = groupMemberService.checkMemberHasLeft(targetGroup,user);

        if (checkMemberLeft) {
            return groupReviewRepository.save(GroupReview.createReview(user, targetGroup, createGroupReviewDto.getContent()));
        }
        else
            throw new RuntimeException();

    }

    @Override
    @Transactional(readOnly = true)
    public List<GroupReview> getAllReviews(User user,Long groupId) {
        Group targetGroup = groupService.findGroupById(groupId);

        return groupReviewRepository.findAllByGroup(targetGroup);
    }
}
