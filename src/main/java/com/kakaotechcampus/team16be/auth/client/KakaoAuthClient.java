package com.kakaotechcampus.team16be.auth.client;

import com.kakaotechcampus.team16be.auth.config.KakaoProperties;
import com.kakaotechcampus.team16be.auth.dto.KakaoTokenResponse;
import com.kakaotechcampus.team16be.auth.dto.KakaoUserInfoResponse;
import com.kakaotechcampus.team16be.auth.exception.KakaoErrorCode;
import com.kakaotechcampus.team16be.auth.exception.KakaoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
@Slf4j
public class KakaoAuthClient {

    // 카카오 API URL 상수
    private static final String KAKAO_TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    private static final String KAKAO_USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";

    private final KakaoProperties kakaoProperties;
    private final RestTemplate restTemplate;

    /**
     * 인가 코드(code)를 사용하여 카카오에서 Access Token 요청
     * @param code 카카오 서버로부터 받은 인가 코드
     * @return KaKaoTokenResponse 액세스 토큰 정보
     */
    public KakaoTokenResponse requestAccessToken(String code) {
        HttpEntity<MultiValueMap<String, String>> request = buildTokenRequestEntity(code);

        try {
            ResponseEntity<KakaoTokenResponse> response = restTemplate.exchange(
                    KAKAO_TOKEN_URL,
                    HttpMethod.POST,
                    request,
                    KakaoTokenResponse.class
            );

            log.info("Kakao Token API Response status={}", response.getStatusCode());

            // 1. HTTP 상태가 2xx 아닌 경우
            if (!response.getStatusCode().is2xxSuccessful()) {
                log.error("Kakao Token API returned non-2xx status: {}", response.getStatusCode());
                throw new KakaoException(KakaoErrorCode.TOKEN_REQUEST_FAILED_HTTP_STATUS);
            }

            KakaoTokenResponse tokenResponse = response.getBody();

            // 2. body 자체가 null
            if (tokenResponse == null) {
                log.error("Kakao Token API response body is null");
                throw new KakaoException(KakaoErrorCode.TOKEN_REQUEST_FAILED_NULL_BODY);
            }

            // 3. access_token이 없는 경우
            if (tokenResponse.accessToken() == null) {
                log.error("Kakao Token API response does not contain access_token");
                throw new KakaoException(KakaoErrorCode.TOKEN_REQUEST_FAILED_NO_TOKEN);
            }

            return tokenResponse;

        } catch (HttpClientErrorException ex) {
            String responseBody = ex.getResponseBodyAsString();
            log.warn("Kakao Token Request Failed - Status: {}", ex.getStatusCode());
            String bodyLower = responseBody.toLowerCase(); // 소문자로 변환
            if (bodyLower.contains("invalid_grant")) {
                if (bodyLower.contains("code expired")) {
                    throw new KakaoException(KakaoErrorCode.AUTH_CODE_EXPIRED);
                } else if (bodyLower.contains("already used")) {
                    throw new KakaoException(KakaoErrorCode.AUTH_CODE_ALREADY_USED);
                } else if (bodyLower.contains("redirect uri mismatch")) {
                    throw new KakaoException(KakaoErrorCode.REDIRECT_URI_MISMATCH);
                } else {
                    throw new KakaoException(KakaoErrorCode.AUTH_CODE_INVALID);
                }
            } else if (bodyLower.contains("invalid_client")) {
                throw new KakaoException(KakaoErrorCode.INVALID_CLIENT_ID);
            }
            throw new KakaoException(KakaoErrorCode.TOKEN_REQUEST_FAILED_CLIENT);
        } catch (HttpServerErrorException ex) {
            log.error("HttpServerErrorException: status={}", ex.getStatusCode());
            throw new KakaoException(KakaoErrorCode.TOKEN_REQUEST_FAILED_SERVER);
        } catch (ResourceAccessException ex) {
            log.error("Kakao Token API Connection Failed: {}", ex.getMessage());
            throw new KakaoException(KakaoErrorCode.CONNECTION_FAILED);
        } catch (Exception ex) {
            log.error("Unknown error during Kakao token request", ex);
            throw new KakaoException(KakaoErrorCode.UNKNOWN_ERROR);
        }
    }

    /**
     * Access Token을 사용하여 카카오에서 kakaoId 조회
     * @param accessToken 카카오에서 발급받은 Access Token
     * @return kakaoId
     */
    public String requestKakaoId(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken); // Bearer 방식 인증 헤더

        HttpEntity<Void> request = new HttpEntity<>(headers);
        try {
            ResponseEntity<KakaoUserInfoResponse> response = restTemplate.exchange(
                    KAKAO_USER_INFO_URL,
                    HttpMethod.GET,
                    request,
                    KakaoUserInfoResponse.class
            );

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new KakaoException(KakaoErrorCode.USER_INFO_REQUEST_FAILED);
            }

            KakaoUserInfoResponse userInfo = response.getBody();
            if (userInfo == null || userInfo.kakaoId() == null) {
                throw new KakaoException(KakaoErrorCode.USER_INFO_REQUEST_FAILED);
            }

            return String.valueOf(userInfo.kakaoId()); // kakaoId 반환

        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new KakaoException(KakaoErrorCode.USER_INFO_REQUEST_FAILED);
        } catch (ResourceAccessException ex) {
            throw new KakaoException(KakaoErrorCode.CONNECTION_FAILED);
        }
    }

    //카카오 유저 정보 한번에 가져옴
    public KakaoUserInfoResponse requestKakaoUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<KakaoUserInfoResponse> response = restTemplate.exchange(
                    KAKAO_USER_INFO_URL,
                    HttpMethod.GET,
                    request,
                    KakaoUserInfoResponse.class
            );

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new KakaoException(KakaoErrorCode.USER_INFO_REQUEST_FAILED);
            }

            KakaoUserInfoResponse userInfo = response.getBody();
            if (userInfo == null || userInfo.kakaoId() == null) {
                throw new KakaoException(KakaoErrorCode.USER_INFO_REQUEST_FAILED);
            }

            return userInfo;

        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new KakaoException(KakaoErrorCode.USER_INFO_REQUEST_FAILED);
        } catch (ResourceAccessException ex) {
            throw new KakaoException(KakaoErrorCode.CONNECTION_FAILED);
        }
    }

    // Access Token 요청을 위한 HttpEntity 생성
    private HttpEntity<MultiValueMap<String, String>> buildTokenRequestEntity(String code) {
        code = code.trim();
        try {
            code = URLDecoder.decode(code, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Failed to decode code", e);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED); // form-data 방식

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoProperties.getClientId());
        body.add("redirect_uri", kakaoProperties.getRedirectUri());
        body.add("code", code);

        return new HttpEntity<>(body, headers);
    }

}

