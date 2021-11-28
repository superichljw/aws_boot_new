package com.example.aws_boot.config.auth.dto;

import com.example.aws_boot.domain.user.Role;
import com.example.aws_boot.domain.user.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {
    private Map<String,Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;

    @Builder
    public OAuthAttributes(Map<String,Object> attributes, String nameAttributeKey, String name, String email, String picture){
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }
    // of() : OAuth2User 에서 반환하는 사용자 정보는 Map 타입
    // 그렇기 때문에 값 하나하나를 변환해야 한다
    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String,Object> attributes){
        return ofGoogle(userNameAttributeName, attributes);
    }

    //ofGoogle : 가져온 사용자 정보(Map 타입) 을 구글에 맞게 변환
    public static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String,Object> attributes){
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    // toEntity :  User Entity 생성
    // OAuthAttributes 에서 엔티티를 생성하는 시점은 처음 가입할 때.
    // 가입당시 기본권한을 게스트로 부여
    public User toEntity(){
        return User.builder()
                .name(name)
                .email(email)
                .picture(picture)
                .role(Role.GUEST)
                .build();
    }
}
