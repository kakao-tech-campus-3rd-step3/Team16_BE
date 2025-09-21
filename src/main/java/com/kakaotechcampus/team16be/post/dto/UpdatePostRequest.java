package com.kakaotechcampus.team16be.post.dto;

import jakarta.validation.constraints.Size;
import java.util.List;

public record UpdatePostRequest (

        @Size(min = 3, max = 15,message = "Title은 3자 이상 15자 이하로 작성해주세요.")
        String title,

        @Size(max=500, message = "Content는 최대 500자까지 작성 가능합니다.")
        String content,

        List<String> imageUrls
) {}
