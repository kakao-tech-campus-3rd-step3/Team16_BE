package com.kakaotechcampus.team16be.common.eventListener;

import com.kakaotechcampus.team16be.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserScoreEvent {

    private UserService userService;

    public void increaseUserScoreByAttendance(IncreaseUserScore event) {
        userService.increaseUserScoreByAttendance(event.user());
    }
}
