package com.example.demo.util;

import org.springframework.beans.factory.annotation.Value;

public class KaKaoUtil {
    @Value("{spring.security.oauth2.client.registration.kakao.client-id")
    private String client;
    @Value("{spring.security.oauth2.client.registration.kakao.redirect-url")
    private String redirectUrl;
}
