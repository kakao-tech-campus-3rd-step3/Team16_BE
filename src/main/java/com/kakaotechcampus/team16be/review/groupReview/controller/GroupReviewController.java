package com.kakaotechcampus.team16be.review.groupReview.controller;

import com.kakaotechcampus.team16be.review.groupReview.service.GroupReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/groups/review")
@RequiredArgsConstructor
public class GroupReviewController {

    private final GroupReviewService groupReviewService;
}
