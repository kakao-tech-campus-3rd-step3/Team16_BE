package com.kakaotechcampus.team16be.attend.service;

import com.kakaotechcampus.team16be.attend.controller.RequestAttendDto;
import com.kakaotechcampus.team16be.attend.domain.Attend;
import com.kakaotechcampus.team16be.user.domain.User;

public interface AttendService {
    Attend attendGroup(User user, Long groupId, RequestAttendDto requestAttendDto);
}
