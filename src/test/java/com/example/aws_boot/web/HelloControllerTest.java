package com.example.aws_boot.web;

import com.example.aws_boot.config.auth.SecurityConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
//webmvctest 는 customoauth2userservice를 스캔하지 않기에, 테스트 oauth 값을 넣어져도 정상작동이 안된다
//webmvctest > @ControllerAdvice, @Controller 를 읽지만, @Repository, @Service, @Component 는 스캔대상이 아니다!!
//그래서 excludeFilters 로 SecurityConfig 를 제외시킨다!
// 이렇게 해도, Error creating bean with name 'jpaAuditingHandler' 이런 오류가 발생한다
// jpaAuditing 은 최소 하나의 @Entity 클래스가 필요한데, 테스트다보니 엔티티를 만들지 않았다
// 그래서 AwsBootApplication.java 에서 @EnableJpaAUditing 을 삭제 후, jpaConfig를 생성하여 2개를 분리한다
@WebMvcTest(controllers = HelloController.class,
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)})
public class HelloControllerTest {

    @Autowired
    private MockMvc mvc;


    @Test
    @WithMockUser(roles = "USER")
    public void hello_return() throws Exception{
        String hello = "안녕 난 재우야";

        mvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string(hello));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void helloDto가_리턴된다() throws Exception{
        String name = "이선우";
        int amount = 10000;

        mvc.perform(get("/hello/dto")
                        .param("name",name)
                        .param("amount",String.valueOf(amount)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name",is(name)))
                .andExpect(jsonPath("$.amount",is(amount)));
    }
}
