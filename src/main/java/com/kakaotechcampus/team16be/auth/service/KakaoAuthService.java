package com.kakaotechcampus.team16be.auth.service;

import com.kakaotechcampus.team16be.auth.client.KakaoAuthClient;
import com.kakaotechcampus.team16be.auth.dto.KakaoLoginResponse;
import com.kakaotechcampus.team16be.auth.dto.KakaoTokenResponse;
import com.kakaotechcampus.team16be.auth.dto.KakaoUserInfoResponse;
import com.kakaotechcampus.team16be.auth.jwt.JwtProvider;
import com.kakaotechcampus.team16be.user.domain.User;
import com.kakaotechcampus.team16be.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class KakaoAuthService {

    private final KakaoAuthClient kakaoAuthClient;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Transactional
    public KakaoLoginResponse loginWithCode(String code, HttpServletRequest request) {
        // 1. 인가 코드로 Access Token 요청
        KakaoTokenResponse kakaoTokenResponse = kakaoAuthClient.requestAccessToken(code);
        String kakaoAccessToken = kakaoTokenResponse.accessToken();

        // 2. Access Token으로 kakaoId 조회
        KakaoUserInfoResponse kakaoUserInfo = kakaoAuthClient.requestKakaoUserInfo(kakaoAccessToken);
        String kakaoId = String.valueOf(kakaoUserInfo.kakaoId());


        // 3. kakaoId로 기존 회원 조회, 없으면 kakaoId 저장
        User user = userRepository.findByKakaoId(kakaoId)
                .orElseGet(() -> {
                    User newUser = new User(kakaoId);
                    return userRepository.save(newUser);
                });

        // 5. JWT 발급
        String accessToken = jwtProvider.createToken(user);

        return new KakaoLoginResponse(accessToken);
    }

    @Transactional
    public void logout(HttpServletRequest request) {

    }

}
