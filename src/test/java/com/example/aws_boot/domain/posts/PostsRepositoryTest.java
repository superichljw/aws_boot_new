package com.example.aws_boot.domain.posts;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest

public class PostsRepositoryTest {

    //별짓을 다해도 이거 오류해결을 못하겠다. 인텔리제이상의 오류라는데, 뭘 어쩌라는건지...
    //AWS 책보고 만든 gradle 프로젝트 생성 > 부트로 변경하게되면 오토와이어드가 잘 안먹히는데,
    //spring initializer 를 통해 만드니 autowired가 잘 먹힌다. 이유는 잘 모르겠다
    //프로젝트 구조때문인지 뭔지...
    @Autowired
    PostsRepository postsRepository;

    @After
    public void cleanup(){
        postsRepository.deleteAll();
    }
    @Test
    public void 게시글저장_불러오기(){
        //given
        String title = "테스트 게시글";
        String content = "테스트 본문글";

        postsRepository.save(Posts.builder()
                .title(title)
                .content(content)
                .author("by LJW")
                .build());

        //when
        List<Posts> postsList = postsRepository.findAll();

        //then
        Posts posts = postsList.get(0);
        assertThat(posts.getTitle()).isEqualTo(title);
        assertThat(posts.getContent()).isEqualTo(content);
    }

    @Test
    public void BaseTimeEntity_등록(){
        //given
        LocalDateTime now = LocalDateTime.of(2021,11,21,20,10,10);
        postsRepository.save(Posts.builder()
                .title("title")
                .content("content")
                .author("author")
                .build());

        //when
        List<Posts> postsList = postsRepository.findAll();

        //then
        Posts posts = postsList.get(0);
        System.out.println(">>>>>>>>>> createdDate = " + posts.getCreatedDate() + " , modifiedDate = " + posts.getModifiedDate());

        assertThat(posts.getCreatedDate()).isAfter(now);
        assertThat(posts.getModifiedDate()).isAfter(now);
    }

}
