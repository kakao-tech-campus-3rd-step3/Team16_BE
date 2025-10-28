package com.kakaotechcampus.team16be.common.eventListener;

import com.kakaotechcampus.team16be.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserScoreEvent {

    private final UserService userService;

    @EventListener
    public void increaseUserScoreByAttendance(IncreaseUserScore event) {
        userService.increaseUserScoreByAttendance(event.user());
    }

    @EventListener
    public void increaseUserScoreByPosting(IncreaseUserScore event) {
        userService.increaseUserScoreByPosting(event.user());
    }
}
