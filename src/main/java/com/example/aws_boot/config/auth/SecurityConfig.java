package com.example.aws_boot.config.auth;

import com.example.aws_boot.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOauth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.csrf().disable().headers().frameOptions().disable()//h2-console 을 사용하기 위해 disable처리
                .and()
                    .authorizeRequests()    //url별 권한관리 설정의 시작점 : 이게 선언되어야만 antMatcher 사용가능
                    .antMatchers("/","/css/**","/images/**","/js/**","/h2-console/**")  //권한관리대상 지정 옵션 : url,http별로 관리가능 - 현재 지정된 url에 전체열람권한 부여
                    .permitAll()
                    .antMatchers("/api/v1/**").hasRole(Role.USER.name())    // api/v1/ 의 경로에 접근할 때에는 User 권한가진 사람에게만 접근 가능하도록 권한 처리
                    .anyRequest().authenticated()   //anyRequest : 위에 지정한 url 이외의 나머지 url : 여기에서는 authenticated 를 통해 인증된 사용자만 접근 가능하도록 처리 - 인증된 사용자는 로그인한 사용자
                .and()
                    .logout()   //로그아웃 기능에 대한 여러 설정의 진입점
                        .logoutSuccessUrl("/")  //로그아웃 성공시 '/' 이 주소로 이동
                .and()
                    .oauth2Login()  //로그인 기능에 대한 여러 설정의 진입점
                        .userInfoEndpoint() //OAuth2 로그인 성공 이후 사용자 정보를 가져올 떄의 설정 담당
                            .userService(customOauth2UserService);
        //소셜 로그인 성공 시, 후속조치를 진행할 UserService 인터페이스의 구현체 등록
        // - 소셜 서비스에서 가져온 사용자 정보를 가져온 상태에서 추가로 진행하고자 하는 기능 명시 가능
    }
}
