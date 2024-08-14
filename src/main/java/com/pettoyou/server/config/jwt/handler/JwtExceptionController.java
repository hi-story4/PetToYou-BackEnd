package com.pettoyou.server.config.jwt.handler;

import com.pettoyou.server.constant.enums.CustomResponseStatus;
import com.pettoyou.server.constant.exception.CustomException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exception")
public class JwtExceptionController {
    @GetMapping("/accessDenied")
    public void accessException() {
        throw new CustomException(CustomResponseStatus.ACCESS_DENIED);
    }

    @GetMapping("/entrypoint/nullToken")
    public void nullTokenException() {
        throw new CustomException(CustomResponseStatus.NULL_JWT);
    }

    @GetMapping("/entrypoint/expiredToken")
    public void expiredTokenException() {
       throw new CustomException(CustomResponseStatus.EXPIRED_JWT);
    }

    @GetMapping("/entrypoint/badToken")
    public void badTokenException() {
        throw new CustomException(CustomResponseStatus.BAD_JWT);
    }

    @GetMapping("/entrypoint/logout")
    public void logoutMemberAccessException() {
        throw new CustomException(CustomResponseStatus.LOGOUT_MEMBER);
    }
}
