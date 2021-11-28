package com.example.aws_boot.config.auth.dto;

import com.example.aws_boot.domain.user.User;
import lombok.Getter;

import java.io.Serializable;

//User 클래스를 그대로 사용못해서 sessionuser dto 를 새로 생성해서 사용
// user 정보를 직렬화해서 사용해야 오류가 안남
// user 클래스에 직렬화코드 넣어서 사용할 수는 있지만, 실제로 entity 는 다른 entity 와 관계를 맺고 있기떄문에 변수가 많다
// @OneToMany , @ManyToMany 등의 자식 엔티티를 갖고있다면,  직렬화 대상에 자식들까지 포함되버리기 때문에 성능이슈, 부수효과 발생확률 높아짐

@Getter
public class SessionUser implements Serializable {
    private String name;
    private String email;
    private String picture;

    public SessionUser(User user){
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
    }
}
