package com.kakaotechcampus.team16be.attend.service;

import com.kakaotechcampus.team16be.attend.dto.RequestAttendDto;
import com.kakaotechcampus.team16be.attend.dto.ResponseAttendsDto;
import com.kakaotechcampus.team16be.attend.domain.Attend;
import com.kakaotechcampus.team16be.user.domain.User;

import java.util.List;

public interface AttendService {
    Attend attendGroup(User user, Long groupId, RequestAttendDto requestAttendDto);

    List<Attend> getAllAttends(User user, Long groupId, Long planId);

    List<Attend> getAttendsByGroup(User user, Long groupId);
}
