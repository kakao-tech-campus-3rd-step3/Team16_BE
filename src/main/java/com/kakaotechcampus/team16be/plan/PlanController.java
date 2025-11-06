package com.kakaotechcampus.team16be.plan;

import com.kakaotechcampus.team16be.common.annotation.LoginUser;
import com.kakaotechcampus.team16be.plan.dto.PlanRequestDto;
import com.kakaotechcampus.team16be.plan.dto.PlanResponseDto;
import com.kakaotechcampus.team16be.plan.service.PlanFacadeServiceImpl;
import com.kakaotechcampus.team16be.plan.service.PlanService;
import com.kakaotechcampus.team16be.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "일정", description = "그룹 일정 관련 API")
public class PlanController {

    private final PlanService planService;
    private final PlanFacadeServiceImpl attendFacadeService;

    @Operation(summary = "일정 생성", description = "그룹 내에서 새로운 일정을 생성합니다.")
    @PostMapping("/groups/{groupId}/plans")
    public ResponseEntity<Long> createPlan(
            @LoginUser User user,
            @PathVariable Long groupId,
            @RequestBody PlanRequestDto planRequestDto) {
        Long planId = attendFacadeService.createPlan(user, groupId, planRequestDto);
        URI location = URI.create(String.format("/api/groups/%d/plans/%d", groupId, planId));
        return ResponseEntity.created(location).build();
    }


    @Operation(summary = "일정 조회", description = "특정 그룹 내 일정 하나를 조회합니다.")
    @GetMapping("/groups/{groupId}/plans/{planId}")
    public ResponseEntity<PlanResponseDto> getPlan(
            @PathVariable Long groupId,
            @PathVariable Long planId) {
        return ResponseEntity.status(HttpStatus.OK).body(planService.getPlan(groupId, planId));
    }

    @Operation(summary = "그룹 일정 전체 조회", description = "특정 그룹 내 모든 일정을 조회합니다.")
    @GetMapping("/groups/{groupId}/plans")
    public ResponseEntity<List<PlanResponseDto>> getAllPlans(
            @PathVariable Long groupId
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(planService.getAllPlans(groupId));
    }

    @Operation(summary = "일정 수정", description = "특정 그룹 내 일정을 수정합니다.")
    @PatchMapping("/groups/{groupId}/plans/{planId}")
    public ResponseEntity<Void> updatePlan(
            @LoginUser User user,
            @PathVariable Long groupId,
            @PathVariable Long planId,
            @RequestBody PlanRequestDto planRequestDto
    ) {
        planService.updatePlan(user, groupId, planId, planRequestDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "일정 삭제", description = "특정 그룹 내 일정을 삭제합니다.")
    @DeleteMapping("/groups/{groupId}/plans/{planId}")
    public ResponseEntity<Void> deletePlan(
            @LoginUser User user,
            @PathVariable Long groupId,
            @PathVariable Long planId) {
        planService.deletePlan(user, groupId, planId);
        return ResponseEntity.noContent().build();
    }
}
