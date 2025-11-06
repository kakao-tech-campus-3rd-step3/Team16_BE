package com.kakaotechcampus.team16be.group.service;

import com.kakaotechcampus.team16be.common.eventListener.userEvent.ImageDeletedEvent;
import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.group.dto.CreateGroupDto;
import com.kakaotechcampus.team16be.group.dto.UpdateGroupDto;
import com.kakaotechcampus.team16be.group.exception.GroupErrorCode;
import com.kakaotechcampus.team16be.group.exception.GroupException;
import com.kakaotechcampus.team16be.group.repository.GroupRepository;
import com.kakaotechcampus.team16be.groupMember.domain.GroupMember;
import com.kakaotechcampus.team16be.groupMember.repository.GroupMemberRepository;
import com.kakaotechcampus.team16be.user.domain.User;
import com.kakaotechcampus.team16be.user.exception.UserErrorCode;
import com.kakaotechcampus.team16be.user.exception.UserException;
import com.kakaotechcampus.team16be.user.repository.UserRepository;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final GroupMemberRepository groupMemberRepository;


    @Transactional
    @Override
    public Group createGroup(User user, CreateGroupDto createGroupDto) {
        String groupName = createGroupDto.name();
        String groupIntro = createGroupDto.intro();
        Integer groupCapacity = createGroupDto.capacity();

        User leader = userRepository.findById(user.getId())
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        if (existGroupName(groupName)) {
            throw new GroupException(GroupErrorCode.GROUP_NAME_DUPLICATE);
        }

        Group createdGroup = Group.createGroup(leader, groupName, groupIntro, groupCapacity, createGroupDto.fileName());
        groupRepository.save(createdGroup);

        GroupMember leaderMember = GroupMember.create(createdGroup, leader);
        groupMemberRepository.save(leaderMember);

        return createdGroup;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Group> getAllGroups() {

        List<Group> findGroups = groupRepository.findAll(Sort.by(Direction.DESC, "score"));

        if (findGroups.isEmpty()) {
            throw new GroupException(GroupErrorCode.GROUP_CANNOT_FOUND);
        }

        return findGroups;
    }


    @Transactional
    @Override
    public void deleteGroup(User user, Long groupId) {
        User leader = userRepository.findById(user.getId())
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        Group targetGroup = findGroupById(groupId);
        targetGroup.checkLeader(leader);

        groupRepository.deleteById(groupId);
    }

    @Transactional
    @Override
    public Group updateGroup(User user, Long groupId, UpdateGroupDto updateGroupDto) {
        Group targetGroup = findGroupById(groupId);

        User leader = userRepository.findById(user.getId())
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        targetGroup.checkLeader(leader);

        String updatedName = updateGroupDto.name();
        String updatedIntro = updateGroupDto.intro();
        Integer updatedCapacity = updateGroupDto.capacity();
        targetGroup.update(updatedName, updatedIntro, updatedCapacity);
        return targetGroup;
    }

    @Override
    public Group findGroupById(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupException(GroupErrorCode.GROUP_CANNOT_FOUND));
    }


    @Transactional
    @Override
    public Group updateGroupImage(User user, Long groupId, UpdateGroupDto UpdateGroupDto) {
        Group targetGroup = findGroupById(groupId);
        User leader = userRepository.findById(user.getId())
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        targetGroup.checkLeader(leader);

        String oldImgUrl = targetGroup.getCoverImageUrl();
        String updatedImgUrl = UpdateGroupDto.coverImageUrl();

        targetGroup.changeCoverImage(updatedImgUrl);

        boolean isImageChanged = !Objects.equals(updatedImgUrl, oldImgUrl);
        if (isImageChanged && oldImgUrl != null && !oldImgUrl.isEmpty()) {
            eventPublisher.publishEvent(new ImageDeletedEvent(oldImgUrl));
        }

        return targetGroup;
    }

    public boolean existGroupName(String groupName) {
        return groupRepository.existsGroupByName(groupName);
    }

    public List<Group> findAll() {
        return groupRepository.findAll();
    }

    @Transactional
    @Override
    public void updateGroupScore(Group group, Double avg) {
        group.groupScoreUpdate(avg);
    }

    @Transactional
    @Override
    public void updateGroupTag(Group group) {
        group.updateSafetyTagByScore();
    }

    @Transactional(readOnly = true)
    public Group getGroupById(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupException(GroupErrorCode.GROUP_CANNOT_FOUND));
    }

    @Transactional
    @Override
    public void updateGroupScoreAndTag(Group group, Double newScore) {
        group.groupScoreUpdate(newScore);
        group.updateSafetyTagByScore();
    }

    @Override
    public void checkGroupLeader(User user, Group targetGroup) {
        targetGroup.checkLeader(user);
    }
}
