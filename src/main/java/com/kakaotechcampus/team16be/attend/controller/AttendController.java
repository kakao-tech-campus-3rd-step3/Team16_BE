package com.kakaotechcampus.team16be.attend.controller;

import com.kakaotechcampus.team16be.attend.domain.Attend;
import com.kakaotechcampus.team16be.attend.service.AttendService;
import com.kakaotechcampus.team16be.common.annotation.LoginUser;
import com.kakaotechcampus.team16be.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
@Tag(name = "그룹 일정 출석 API", description = "그룹 일정 출석 관련 API")
public class AttendController {

    private final AttendService attendService;

    @Operation(summary = "그룹 일정 출석", description = "유저가 특정 그룹의 일정에 출석합니다.")
    @PostMapping("/{groupId}/attend")
    public ResponseEntity<ResponseAttendDto> attendGroup(@LoginUser User user, @PathVariable Long groupId, @RequestBody RequestAttendDto requestAttendDto) {
        Attend attend = attendService.attendGroup(user, groupId,requestAttendDto);

        return ResponseEntity.ok(ResponseAttendDto.success(HttpStatus.OK, "그룹 출석이 완료되었습니다. 출석 상태 : " + attend.getAttendStatus()));
    }
}
