package com.kakaotechcampus.team16be.attend.controller;

import com.kakaotechcampus.team16be.attend.domain.Attend;
import com.kakaotechcampus.team16be.attend.dto.*;
import com.kakaotechcampus.team16be.attend.service.AttendService;
import com.kakaotechcampus.team16be.common.annotation.LoginUser;
import com.kakaotechcampus.team16be.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
@Tag(name = "그룹 일정 출석 API", description = "그룹 일정 출석 관련 API")
public class AttendController {

    private final AttendService attendService;

    @Operation(summary = "그룹 일정 출석", description = "유저가 특정 그룹의 일정에 출석합니다.")
    @PostMapping("/{groupId}/attend")
    public ResponseEntity<ResponseAttendDto> attendGroup(@LoginUser User user, @PathVariable Long groupId, @RequestBody RequestAttendDto requestAttendDto) {

        Attend attend = attendService.attendGroup(user, groupId, requestAttendDto);


        return ResponseEntity.ok(ResponseAttendDto.success(HttpStatus.OK, "그룹 출석이 완료되었습니다. 출석 상태 : " + attend.getAttendStatus()));
    }


    @Operation(summary = "그룹 일정 전체 출석 조회", description = "특정 그룹의 모든 일정 출석 정보를 조회합니다.")
    @GetMapping("/{groupId}/attends/{planId}")
    public ResponseEntity<GetAttendeesResponse> getAllAttends(@LoginUser User user, @PathVariable Long groupId, @PathVariable Long planId) {
        List<Attend> allAttends = attendService.getAllAttends(user, groupId, planId);
        List<ResponseAttendsDto> attendsDtos = ResponseAttendsDto.from(allAttends);
        boolean isUserAttended = allAttends.stream().anyMatch(attend -> attend.getGroupMember().getUser().getId().equals(user.getId()));

        return ResponseEntity.ok(GetAttendeesResponse.from(attendsDtos, isUserAttended));
    }

    @Operation(summary = "그룹별 일정 개인별 출석 조회", description = "특정 그룹의 개인별 일정 출석 정보를 조회합니다.")
    @GetMapping("/{groupId}/attend")
    public ResponseEntity<List<ResponseAttendsDto>> getUserAttends(@LoginUser User user, @PathVariable Long groupId) {
        List<Attend> userAttends = attendService.getAttendsByGroup(user, groupId);

        return ResponseEntity.ok(ResponseAttendsDto.from(userAttends));
    }

    @Operation(summary = "일정별 개인 출석 조회",description = "특정 일정에 대한 개인 출석 정보를 조회합니다.")
    @GetMapping("/{groupId}/attend/{planId}")
    public ResponseEntity<ResponseAttendsDto> getUserAttendsByPlan(@LoginUser User user, @PathVariable Long groupId, @PathVariable Long planId) {
        Attend userAttends = attendService.getAttendByPlan(user, groupId, planId);

        return ResponseEntity.ok(ResponseAttendsDto.from(userAttends));
    }

    @Operation(summary = "결석한 인원 조회", description = "특정 그룹의 일정에 결석한 인원들을 조회합니다.")
    @GetMapping("/{groupId}/attend/{planId}/absent")
    public ResponseEntity<List<ResponseAbsentAttendsDto>> getAbsentMembers(@LoginUser User user, @PathVariable Long groupId, @PathVariable Long planId) {
        List<Attend> absentMembers = attendService.getAbsentMembers(user, groupId, planId);

        return ResponseEntity.ok(ResponseAbsentAttendsDto.from(absentMembers));
    }
}

