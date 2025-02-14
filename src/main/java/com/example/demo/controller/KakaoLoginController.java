package com.example.demo.controller;
import com.example.demo.dto.UserLoginResponse;
import com.example.demo.response.ResponseBody;
import com.example.demo.response.ResponseUtil;
import com.example.demo.service.kakao.KakaoTokenService;
import com.example.demo.service.kakao.LoginService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
public class KakaoLoginController {
    private final LoginService loginService;
    private final KakaoTokenService kakaoTokenService;

    // 카카오 로그인 요청
    @GetMapping("/api/auth/kakao/login")
    public void login(HttpServletResponse response) throws IOException {
        String url=loginService.login();
        response.sendRedirect(url);
    }
    // 카카오 토큰 생성
    @GetMapping("/auth/login/kakao")
    public ResponseEntity<ResponseBody<?>> kakaoCallback(@RequestParam("code") String accessCode,HttpServletResponse response){
        UserLoginResponse userLoginResponse=kakaoTokenService.exchangeCodeForToken(accessCode,response);
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(userLoginResponse));
    }
}
