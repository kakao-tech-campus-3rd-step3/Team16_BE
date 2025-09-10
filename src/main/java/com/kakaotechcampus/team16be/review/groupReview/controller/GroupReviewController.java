package com.kakaotechcampus.team16be.review.groupReview.controller;

import com.kakaotechcampus.team16be.common.annotation.LoginUser;
import com.kakaotechcampus.team16be.group.dto.ResponseGroupDto;
import com.kakaotechcampus.team16be.group.service.GroupService;
import com.kakaotechcampus.team16be.review.common.ResponseReviewDto;
import com.kakaotechcampus.team16be.review.groupReview.dto.CreateGroupReviewDto;
import com.kakaotechcampus.team16be.review.groupReview.service.GroupReviewService;
import com.kakaotechcampus.team16be.review.groupReview.service.ReviewService;
import com.kakaotechcampus.team16be.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpResponse;

@RestController("/api/groups/review")
@RequiredArgsConstructor
public class GroupReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ResponseReviewDto> createGroupReview(@LoginUser User user, CreateGroupReviewDto createGroupReviewDto) {
        reviewService.createReview(user, createGroupReviewDto);

        return ResponseEntity.ok(ResponseReviewDto.success(HttpStatus.OK, "그룹 리뷰를 성공적으로 생성했습니다."));
    }

}
