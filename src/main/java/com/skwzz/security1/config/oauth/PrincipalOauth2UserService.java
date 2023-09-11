package com.skwzz.security1.config.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
    // 구글로그인 후 응답으로 받은 userRequest 데이터에 대한 후처리하는 함수
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("userRequest = " + userRequest);
        System.out.println(userRequest.toString());
        /*
        username = google_112665211029494930815 super.loadUser(userRequest).getAttribute().get("sub")
        password = "encrypt(겟인데어)
        email = super.loadUser(userRequest).getAttribute().get("email")
        role = ROLE_USER
         */
        OAuth2User oAuth2User = super.loadUser(userRequest);
        return super.loadUser(userRequest);
    }
}
