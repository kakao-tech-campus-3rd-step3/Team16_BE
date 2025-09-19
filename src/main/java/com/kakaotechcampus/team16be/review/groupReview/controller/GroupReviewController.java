package com.kakaotechcampus.team16be.review.groupReview.controller;

import com.kakaotechcampus.team16be.common.annotation.LoginUser;
import com.kakaotechcampus.team16be.review.common.dto.ResponseReviewDto;
import com.kakaotechcampus.team16be.review.common.service.ReviewService;
import com.kakaotechcampus.team16be.review.groupReview.domain.GroupReview;
import com.kakaotechcampus.team16be.review.groupReview.dto.CreateGroupReviewDto;
import com.kakaotechcampus.team16be.review.groupReview.dto.ResponseGroupReviewListDto;
import com.kakaotechcampus.team16be.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups/reviews")
@Tag(name = "그룹 리뷰 API", description = "그룹 리뷰 생성 및 조회 관련 API")
public class GroupReviewController {

    private final ReviewService reviewService;

    public GroupReviewController(@Qualifier("groupReviewServiceImpl") ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Operation(summary = "그룹 리뷰 생성", description = "탈퇴한 회원이 특정 그룹에 대한 리뷰를 작성합니다.")
    @PostMapping
    public ResponseEntity<ResponseReviewDto> createGroupReview(@LoginUser User user, @RequestBody CreateGroupReviewDto createGroupReviewDto) {
        reviewService.createReview(user, createGroupReviewDto);

        return ResponseEntity.ok(ResponseReviewDto.success(HttpStatus.OK, "그룹 리뷰를 성공적으로 생성했습니다."));
    }

    @Operation(summary = "그룹 리뷰 조회", description = "특정 그룹에 작성된 모든 리뷰를 조회합니다.")
    @GetMapping("/{groupId}")
    public ResponseEntity<List<ResponseGroupReviewListDto>> getAllGroupReviews(@LoginUser User user, @PathVariable Long groupId) {
        List<GroupReview> reviews = reviewService.getAllReviews(user,groupId);
        List<ResponseGroupReviewListDto> result = ResponseGroupReviewListDto.from(reviews);

        return ResponseEntity.ok(result);
    }

}
