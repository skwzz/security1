package com.skwzz.security1.config.oauth;

import com.skwzz.security1.config.CustomBCryptPasswordEncoder;
import com.skwzz.security1.config.auth.PrincipalDetails;
import com.skwzz.security1.model.User;
import com.skwzz.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private CustomBCryptPasswordEncoder customBCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

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

        // oAuth2User.getAttributes() 를 토대로 User 객체 생성 -> 강제로 회원가입
        String provider = userRequest.getClientRegistration().getClientId();
        String providerId = oAuth2User.getAttribute("sub");
        String email = oAuth2User.getAttribute("email");
        String username = provider+"_"+providerId; // google_123213123123123
        String password = customBCryptPasswordEncoder.encode("getInThere");
        String role = "ROLE_USER";
        // 유효성검사
        User user = userRepository.findByUsername(username);
        if(user == null){
            user = User.builder()
                    .username(username)
                    .password(password)
                    .email(email).role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepository.save(user);
        }
        //return super.loadUser(userRequest);
        return new PrincipalDetails(user, oAuth2User.getAttributes());
    }
}
