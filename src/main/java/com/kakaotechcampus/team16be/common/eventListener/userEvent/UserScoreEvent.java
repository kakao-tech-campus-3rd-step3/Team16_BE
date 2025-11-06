package com.kakaotechcampus.team16be.common.eventListener.userEvent;

import static com.kakaotechcampus.team16be.user.exception.UserErrorCode.*;

import com.kakaotechcampus.team16be.user.domain.User;
import com.kakaotechcampus.team16be.user.exception.UserException;
import com.kakaotechcampus.team16be.user.repository.UserRepository;
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
                              .orElseThrow(() -> new UserException(USER_NOT_FOUND));

    user.increaseScoreByAttendance();
    userRepository.saveAndFlush(user);
  }

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void increaseUserScoreByPosting(IncreaseUserScoreByPosting event) {
    User user = userRepository.findById(event.user().getId())
                              .orElseThrow(() -> new UserException(USER_NOT_FOUND));

    user.increaseScoreByPosting();
    userRepository.saveAndFlush(user);
  }

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void increaseUserScoreByComment(IncreaseScoreByComment event){
    User user = userRepository.findById(event.user().getId())
                              .orElseThrow(() -> new UserException(USER_NOT_FOUND));

    user.increaseScoreByComment();
    userRepository.saveAndFlush(user);
  }

   @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
   @Transactional(propagation = Propagation.REQUIRES_NEW)
   public void decreaseUserScoreByReport(DecreaseScoreByReport event){
     User targetUser = event.targetUser();

     targetUser.decreaseScoreByReport();
     userRepository.saveAndFlush(targetUser);
   }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void decreaseUserScoreByAbsent(DecreaseScoreByAbsent event) {
        User targetUser = event.user();

        targetUser.decreaseScoreByAbsent();
        userRepository.saveAndFlush(targetUser);
    }
}
