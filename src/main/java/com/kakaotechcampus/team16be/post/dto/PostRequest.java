package com.kakaotechcampus.team16be.post.dto;

import java.util.List;

public record PostRequest(String title, String content, List<String> images) {
}
