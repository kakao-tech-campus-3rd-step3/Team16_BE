package com.kakaotechcampus.team16be.comment.dto;

import java.util.List;

public record GetCommentsResponse(List<CommentResponse> items, Long nextCursor) {
}
