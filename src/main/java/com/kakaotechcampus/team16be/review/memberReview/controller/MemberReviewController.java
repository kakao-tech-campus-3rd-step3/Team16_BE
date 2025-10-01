package com.kakaotechcampus.team16be.review.memberReview.controller;

import com.kakaotechcampus.team16be.common.annotation.LoginUser;
import com.kakaotechcampus.team16be.review.common.dto.ResponseReviewDto;
import com.kakaotechcampus.team16be.review.memberReview.domain.MemberReview;
import com.kakaotechcampus.team16be.review.memberReview.dto.CreateMemberReviewDto;
import com.kakaotechcampus.team16be.review.memberReview.dto.ResponseMemberReviewListDto;
import com.kakaotechcampus.team16be.review.memberReview.service.MemberReviewService;
import com.kakaotechcampus.team16be.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "회원 리뷰 API", description = "탈퇴한 회원에 대한 리뷰 생성 및 조회 API")
public class MemberReviewController {

    private final MemberReviewService memberReviewService;

    @Operation(summary = "회원 리뷰 생성", description = "그룹 내 멤버 전원이 탈퇴한 회원에 대한 리뷰를 작성합니다.")
    @PostMapping("/review")
    public ResponseEntity<ResponseReviewDto> createMemberReview(@LoginUser User user, @RequestBody CreateMemberReviewDto createMemberReviewDto) {
        memberReviewService.createReview(user, createMemberReviewDto);

        return ResponseEntity.ok(ResponseReviewDto.success(HttpStatus.OK, "탈퇴한 회원에 대한 리뷰를 성공적으로 생성했습니다."));
    }

    @Operation(summary = "그룹별 회원 리뷰 조회", description = "특정 그룹에 작성된 모든 회원 리뷰를 조회합니다.")
    @GetMapping("/{groupId}/review")
    public ResponseEntity<List<ResponseMemberReviewListDto>> getMemberReviewsByGroup(@LoginUser User user, @PathVariable Long groupId) {
        List<MemberReview> reviews = memberReviewService.getAllReviewsByGroup(user, groupId);
        List<ResponseMemberReviewListDto> result = ResponseMemberReviewListDto.from(reviews);

        return ResponseEntity.ok(result);
    }

    @Operation(summary = "회원 리뷰 조회", description = "해당 회원의 리뷰를 조회합니다.")
    @GetMapping
    public ResponseEntity<List<ResponseMemberReviewListDto>> getMemberReviews(@LoginUser User user) {
        List<MemberReview> reviews = memberReviewService.getAllReviews(user);

        return ResponseEntity.ok(ResponseMemberReviewListDto.from(reviews));
    }
}
