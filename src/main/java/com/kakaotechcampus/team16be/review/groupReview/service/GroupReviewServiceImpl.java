package com.kakaotechcampus.team16be.review.groupReview.service;

import com.kakaotechcampus.team16be.review.groupReview.repository.GroupReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupReviewServiceImpl implements GroupReviewService {

    private final GroupReviewRepository groupReviewRepository;

}
