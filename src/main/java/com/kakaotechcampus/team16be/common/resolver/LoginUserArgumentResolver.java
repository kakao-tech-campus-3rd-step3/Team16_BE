package com.kakaotechcampus.team16be.common.resolver;

import com.kakaotechcampus.team16be.common.annotation.LoginUser;
import com.kakaotechcampus.team16be.user.domain.User;
import com.kakaotechcampus.team16be.user.exception.UserErrorCode;
import com.kakaotechcampus.team16be.user.exception.UserException;
import com.kakaotechcampus.team16be.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

//컨트롤러 메서드에 진입하기 전처리를 통해 객체를 주입할 수 있다.
//역할 : Spring MVC에서 컨트롤러 진입 전에 JWT 토큰을 파싱해서 @LoginUser가 붙은 파라미터에 Member 객체를 자동으로 주입해줌
@Component
@RequiredArgsConstructor
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final UserRepository userRepository;

    //처리할 파라미터인지 판단하는 메서드
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginUser.class) &&
                parameter.getParameterType().equals(User.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        String userIdStr = (String) webRequest.getAttribute("userId", NativeWebRequest.SCOPE_REQUEST);
        if (userIdStr == null) {
            throw new UserException(UserErrorCode.USER_NOT_FOUND);
        }
        Long userId = Long.valueOf(userIdStr);

        //User객체 반환
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
    }
}
