package com.kakaotechcampus.team16be.post.dto;

import java.util.List;

public record GetPostsResponse(List<PostResponse> items, Long nextCursor) {
}
