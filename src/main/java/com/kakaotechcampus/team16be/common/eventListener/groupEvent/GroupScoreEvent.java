package com.kakaotechcampus.team16be.common.eventListener.groupEvent;


import static com.kakaotechcampus.team16be.group.exception.GroupErrorCode.GROUP_CANNOT_FOUND;

import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.group.exception.GroupException;
import com.kakaotechcampus.team16be.group.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class GroupScoreEvent {

    private final GroupRepository groupRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void increaseGroupScoreByPosting(IncreaseGroupScoreByPosting event) {
        Group group = groupRepository.findById(event.group().getId())
                .orElseThrow(() -> new GroupException(GROUP_CANNOT_FOUND));

        group.increaseScoreByPosting();
        groupRepository.saveAndFlush(group);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void increaseGroupScoreByPlanning(IncreaseScoreByPlanning event) {
        Group group = groupRepository.findById(event.group().getId())
                .orElseThrow(() -> new GroupException(GROUP_CANNOT_FOUND));

        group.increaseScoreByPlanning();
        groupRepository.saveAndFlush(group);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void decreaseGroupScoreByReport(DecreaseGroupScoreByReport event) {
        Group targetGroup = event.targetGroup();

        targetGroup.decreaseScoreByReport();
        groupRepository.saveAndFlush(targetGroup);
    }
}
