package com.kakaotechcampus.team16be.common.aspect;

import com.kakaotechcampus.team16be.common.annotation.AdminOnly;
import com.kakaotechcampus.team16be.common.annotation.LoginUser;
import com.kakaotechcampus.team16be.user.domain.Role;
import com.kakaotechcampus.team16be.user.domain.User;
import com.kakaotechcampus.team16be.user.exception.UserErrorCode;
import com.kakaotechcampus.team16be.user.exception.UserException;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AdminCheckAspect {

  @Before("@annotation(adminOnly) && args(.., @com.kakaotechcampus.team16be.common.annotation.LoginUser user)")
  public void checkAdmin(AdminOnly adminOnly, User user){
    if(user.getRole() != Role.ADMIN){
      throw new UserException(UserErrorCode.USER_NOT_ADMIN);
    }
  }
}
