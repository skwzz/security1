package com.skwzz.security1.controller;

import com.skwzz.security1.config.auth.PrincipalDetails;
import com.skwzz.security1.model.User;
import com.skwzz.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/test/login")
    public @ResponseBody String testLogin(Authentication authentication, @AuthenticationPrincipal PrincipalDetails principalDetails){
        System.out.println("TEST LOGIN");
        System.out.println("authenticationPrincipal = " + authentication);
        System.out.println("authenticationPrincipal.getPincipal() = " + authentication.getPrincipal());
        System.out.println("principalDetails = " + principalDetails.getUser());
        System.out.println("userDetails = " + principalDetails.getUser());
        return "세션정보확인하기";
    }

    @GetMapping("/test/oauth/login")
    public @ResponseBody String testOAuthLogin(Authentication authentication, @AuthenticationPrincipal OAuth2User oauth){
        System.out.println("TEST OAUTH LOGIN");
        System.out.println("authenticationPrincipal = " + authentication);
        OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();
        System.out.println("authenticationPrincipal.getPincipal() = " + (OAuth2User)authentication.getPrincipal());
        System.out.println("oAuth2User.getAttributes() = " + oAuth2User.getAttributes());
        System.out.println("oauth.getAttributes() = " + oauth.getAttributes());
        return "OAuth2 세션정보확인하기";
    }

    @GetMapping({"", "/"})
    public String index(){
        return "index";
    }

    @GetMapping("/user")
    public @ResponseBody String user(){
        return "user";
    }

    @GetMapping("/admin")
    public @ResponseBody String admin(){
        return "admin";
    }

    @GetMapping("/manager")
    public @ResponseBody String manager(){
        return "manager";
    }

    @GetMapping("/loginForm")
    public String loginForm(){
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm(){
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(User user){
        System.out.println(user);
        user.setRole("ROLE_USER");
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);
        userRepository.save(user);
        return "redirect:/loginForm";
    }

    @Secured("ROLE_ADMIN") // 특정 메소드에 간단한게 권한 관련 체크를 하고 싶을 경우
    @GetMapping("/info")
    public @ResponseBody String info(){
        return "개인정보";
    }

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')") // PreAuthorize는 이 메소드가 실행되기 직전에 실행된다
    @GetMapping("/data")
    public @ResponseBody String data(){
        return "데이터정보";
    }
}

