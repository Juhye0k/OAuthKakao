package com.example.demo.service.kakao;
import com.example.demo.dto.UserLoginResponse;
import com.example.demo.response.exception.BusinessException;
import com.example.demo.response.exception.ExceptionType;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import java.net.URLEncoder;

@Service
@RequiredArgsConstructor
public class KakaoTokenService {

    private final WebClient webClient;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;

    // 인증 코드를 사용하여 액세스 토큰 발급
    public UserLoginResponse exchangeCodeForToken(String code, HttpServletResponse response) {
        String tokenUri = "https://kauth.kakao.com/oauth/token";

        String result = webClient.post()
                .uri(tokenUri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                        .with("client_id", clientId)
                        // client-secret 설정이 필요한 경우에만 추가
                        .with("redirect_uri", redirectUri)
                        .with("code", code)
                )
                .retrieve()
                .bodyToMono(String.class)
                .block();
        try{
            String accessToken=result.substring(result.indexOf("access_token=")+13);
            String refreshToken= URLEncoder.encode(result.substring(result.indexOf("refresh_token=")+14),"UTF-8");
            UserLoginResponse userLoginResponse=new UserLoginResponse("로그인 성공",accessToken);
            Cookie cookie=setCookie(refreshToken);
            response.addCookie(cookie);
            return userLoginResponse;
        }catch(Exception e){
            throw new BusinessException(ExceptionType.KAKAO_TOKEN_ERROR);
        }

    }
    public Cookie setCookie(String refreshToken){
        String cookieName="refresh-token";
        Cookie cookie=new Cookie(cookieName,refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(60*60*24*7);
        return cookie;
    }

}
