package com.kakaotechcampus.team16be.common.init;

import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.group.repository.GroupRepository;
import com.kakaotechcampus.team16be.post.domain.Post;
import com.kakaotechcampus.team16be.post.repository.PostRepository;
import com.kakaotechcampus.team16be.user.domain.Role;
import com.kakaotechcampus.team16be.user.domain.User;
import com.kakaotechcampus.team16be.user.domain.VerificationStatus;
import com.kakaotechcampus.team16be.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final PostRepository postRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            System.out.println("데이터베이스가 비어있어 초기 데이터를 생성합니다...");

            // 1. 테스트 유저 생성
            User testUser = User.builder()
                    .kakaoId("test-kakao-12345")
                    .nickname("테스트유저")
                    .role(Role.USER)
                    .verificationStatus(VerificationStatus.UNVERIFIED)
                    .build();
            userRepository.save(testUser);

            // 2. 테스트 그룹 생성
            Group testGroup = Group.builder()
                    .name("테스트 그룹")
                    .intro("테스트 그룹입니다.")
                    .capacity(10)
                    .coverImageUrl("https://picsum.photos/200/300")
                    .build();
            groupRepository.save(testGroup);

            // 3. 테스트 포스트 생성
            Post testPost = new Post("첫 번째 게시물", "게시물 내용입니다.", Collections.emptyList(), testUser, testGroup);
            postRepository.save(testPost);

            System.out.println("초기 데이터 생성 완료. User ID: " + testUser.getId() + ", Group ID: " + testGroup.getId() + ", Post ID: " + testPost.getId());
        }
    }
}
