package com.kakaotechcampus.team16be.review.groupReview.controller;

import com.kakaotechcampus.team16be.common.annotation.LoginUser;
import com.kakaotechcampus.team16be.group.service.GroupService;
import com.kakaotechcampus.team16be.review.common.ReviewCreateDto;
import com.kakaotechcampus.team16be.review.groupReview.dto.CreateGroupReviewDto;
import com.kakaotechcampus.team16be.review.groupReview.dto.ResponseReviewDto;
import com.kakaotechcampus.team16be.review.groupReview.service.GroupReviewService;
import com.kakaotechcampus.team16be.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpResponse;

@RestController("/api/groups/review")
@RequiredArgsConstructor
public class GroupReviewController {

    private final GroupService groupReviewService;
}
