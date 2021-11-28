package com.example.aws_boot.config.auth;

import com.example.aws_boot.config.auth.dto.OAuthAttributes;
import com.example.aws_boot.config.auth.dto.SessionUser;
import com.example.aws_boot.domain.user.User;
import com.example.aws_boot.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{
        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        //registrationId : 현재 진행중인 서비스 구분 코드 / 지금은 구글만 사용하지만 추후 네이버도 추가 연동시 네이버와 구글 구분하기 위해 사용
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        //userNameAttributeName : OAuth2 로그인 진행시 키가되는 필드값/ 구글은 기본적으로 코드지원하지만 네이버 카카오 등은 기본지원 안함
        //구글의 기본값은 "sub"
        // 추후 구글과 네이버 동시 지원시 사용
        String userNameAttributeName = userRequest.getClientRegistration()
                                                    .getProviderDetails()
                                                    .getUserInfoEndpoint()
                                                    .getUserNameAttributeName();
        //OAuthAttributes : OAuth2UserService 를 통해 가져온 OAuth2User 의 attribute 를 담을 클래스
        // 이후 네이버 등 다른 소셜 로그인도 이 클래스 사용
        OAuthAttributes attributes = OAuthAttributes.of(registrationId,userNameAttributeName,oAuth2User.getAttributes());

        User user = saveOrUpdate(attributes);
        //SessionUser : 사용자 정보를 저장하기 위한 dto
        httpSession.setAttribute("user",new SessionUser(user));

        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                attributes.getAttributes(),attributes.getNameAttributeKey());
    }

    private User saveOrUpdate(OAuthAttributes attributes){
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName(),attributes.getPicture()))
                .orElse(attributes.toEntity());

        return userRepository.save(user);
    }
}
