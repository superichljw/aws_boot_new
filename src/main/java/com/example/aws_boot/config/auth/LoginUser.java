package com.example.aws_boot.config.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//1. Target : 이 어노테이션이 생성될 수 있는 위치 지정
// parameter 로 지정했으니, 메소드의 파라미터로 선언된 객체에서만 사용가능
// 이외에도 클래스 선언부에 쓸수있는 TYPE 이 있음
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginUser {
    //@interface : 이 파일을 어노테이션 클리스로 지정
    // LoginUser 라는 이름을 가진 어노테이션이 생성되었다고 보면 된다
}
