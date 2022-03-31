package com.jojoldu.book.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// @EnableJpaAuditing // JPA Auditing을 모두 활성화 할 수 있도록 활성화 어노테이션인 @EnableJpaAuditing 추가
@SpringBootApplication // 스프링 부트의 자동 설정, 스프링 Bean 읽기와 생성을 모두 자동으로 해주는 어노테이션
                       // @SpringBootApplication 이 있는 위치부터 설정을 읽어가기 때문에 이 클래스는 항상 프로젝트 최상단에 위치해야 한다.
public class Application {
    public static void main(String[] args) { // 1. main 메소드에서
        SpringApplication.run(Application.class, args); // 2. SpringApplication 실행하면 -> 내장 WAS(Web Application Server)가 실행된다.
//                                                        내장 WAS란 별도로 외부에 WAS를 두지 않고 애플리케이션을 실행할 내부에서 WAS를 실행하는 것을 의미한다.
//                                                        이렇게 되면 항상 서버에 톰캣을 설치할 필요가 없게 되고, 스프링 부트로 만들어진 Jar 파일(실행 가능한 Java 패키징 파일)로 실행하면 된다.
//                                                        꼭 스프링 부트에서만 내장 WAS를 사용할 수 있는 것은 아니지만, 스프링 부트에서는 내장 WAS를 사용하는 것을 권장하는데
//                                                        그 이유는 '언제 어디서나 같은 환경에서 스프링 부트를 배포'할 수 있기 때문이다.
//                                                        외장 WAS를 쓴다고 하면 모든 서버는 WAS의 종류와 버전, 설정을 일치시켜야만 한다.
//                                                        새로운 서버가 추가되면 모든 서버거 같은 WAS 환경을 구축해야만 하는데 1대면 다행이지만, 30대의 서버에 설치된 WAS의 버전을 올린다고 하면..??
//                                                        실수할 여지도 많고, 시간도 많이 필요한 큰 작업이 될 수도있다. 하지만 이렇게 내장 WAS를 사용 할 경우 이 문제를 모두 해결할 수 있다.
//                                                        그래서 많은 회사에서 내장 WAS를 사용하도록 전환하고 있다
//                                                        간혹 내장 WAS를 쓰면 성능상 이슈가 있지 않냐고 하시는 분들이 계신데, <스프링 부트와 AWS로 혼자 구현하는 웹 서비스>의 저자이신 이동욱님이
//                                                        근무했던 2곳 모두 누구나 알만큼 높은 트래픽의 서비스를 하고 있지만, 이 회사들 모두 스프링 부트로 큰 문제없이 운영하고 있다고 한다.
//                                                        또한, 대표적인 WAS인 톰캣 역시 서블릿으로 이루어진 자바 애플리케이션이고, 똑같은 코드를 사용하고 있으므로 성능상 이슈는 크게 고려하지 않아도 된다.

    }
}
