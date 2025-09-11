package com.kakaotechcampus.team16be.review.groupReview.controller;

import com.kakaotechcampus.team16be.common.annotation.LoginUser;
import com.kakaotechcampus.team16be.review.common.dto.ResponseReviewDto;
import com.kakaotechcampus.team16be.review.groupReview.domain.GroupReview;
import com.kakaotechcampus.team16be.review.groupReview.dto.CreateGroupReviewDto;
import com.kakaotechcampus.team16be.review.groupReview.dto.ResponseGroupReviewListDto;
import com.kakaotechcampus.team16be.review.common.service.ReviewService;
import com.kakaotechcampus.team16be.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/groups/reviews")
@RequiredArgsConstructor
public class GroupReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ResponseReviewDto> createGroupReview(@LoginUser User user, @RequestBody CreateGroupReviewDto createGroupReviewDto) {
        reviewService.createReview(user, createGroupReviewDto);

        return ResponseEntity.ok(ResponseReviewDto.success(HttpStatus.OK, "그룹 리뷰를 성공적으로 생성했습니다."));
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<List<ResponseGroupReviewListDto>> getAllGroupReviews(@LoginUser User user, @PathVariable Long groupId) {
        List<GroupReview> reviews = reviewService.getAllReviews(user,groupId);
        List<ResponseGroupReviewListDto> result = ResponseGroupReviewListDto.from(reviews);

        return ResponseEntity.ok(result);
    }

}
