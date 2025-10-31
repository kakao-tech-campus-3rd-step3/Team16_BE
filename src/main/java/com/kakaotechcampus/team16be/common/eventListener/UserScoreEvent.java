package com.kakaotechcampus.team16be.common.eventListener;

import com.kakaotechcampus.team16be.user.domain.User;
import com.kakaotechcampus.team16be.user.exception.UserErrorCode;
import com.kakaotechcampus.team16be.user.exception.UserException;
import com.kakaotechcampus.team16be.user.repository.UserRepository;
import com.kakaotechcampus.team16be.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class UserScoreEvent {

  private final UserRepository userRepository;

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void increaseUserScoreByAttendance(IncreaseScoreByAttendance event) {
    User user = userRepository.findById(event.user().getId())
                              .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

    user.increaseScoreByAttendance();
    userRepository.saveAndFlush(user);
  }

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void increaseUserScoreByPosting(IncreaseScoreByPosting event) {
    User user = userRepository.findById(event.user().getId())
                              .orElseThrow();

    user.increaseScoreByPosting();
    userRepository.saveAndFlush(user);
  }
}