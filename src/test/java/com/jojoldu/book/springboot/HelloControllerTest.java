package com.jojoldu.book.springboot;

import com.jojoldu.book.springboot.config.auth.SecurityConfig;
import com.jojoldu.book.springboot.web.HelloController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class) // 1. 테스트 진행할 때 JUnit에 내장된 실행자 외에 다른 실행자를 실행시킨다. 여기서는 SpringExtension라는 스프링 실행자를 사용한다. 즉, 스프링 부트 테스트와 JUnit 사이에 연결자 역할을 한다.
@WebMvcTest(controllers = HelloController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class) // 2. 여러 스프링 테스트 어노테이션 중, Web(Spring MVC)에 집중할 수 있는 어노테이션이다. 선언할 경우, @Controller, @ControllerAdvice 등을 사용할 수 있다. 단, @Service, @Component, @Repository 등은 사용할 수 없다. 여기서는 컨트롤러만 사용하기 때문에 선언한다.
        }
)
    public class HelloControllerTest {

    @Autowired // 스프링이 관리하는 빈(Bean)을 주입 받는다.
    private MockMvc mvc; // 웹 API를 테스트할 때 사용한다.
                         // 스프링 MVC테스트의 시작점이다.
                         // 이 클래스를 통해 HTTP GET, POST 등에 대한 API 테스트를 할 수 있다.

    @WithMockUser(roles="USER") // 가짜로 인증된 사용자 생성
    @Test
    public void hello가_리턴된다() throws Exception {
        String hello = "hello";

        // import 하면 get이라는 static 메서드가 여러 라이브러리들에 흔하게 있어 MockMvcRequestBuilders.get 이 목록에 안뜨는데..setting - auto import에서 직접 import 할 일 없는 것들 제외처리 해줘도 마찬가지다..우선 책에있는 내용을 직접 작성하여 import했다. 참고: https://junho85.pe.kr/1598
        mvc.perform(get("/hello"))         // MockMvc를 통해 /hello 주소로 HTTP GET 요청을 한다. 체이닝이 지원되어 아래와 같이 여러 검증 기능을 이어서 선언할 수 있다.
                .andExpect(status().isOk())          // mvc.perform의 결과를 검증한다. HTTP Header의 Status를 검증한다. 우리가 흔히 알고있는 200, 404, 500 등의 상태를 검증한다. 여기선 OK 즉, 200인지 아닌지를 검증한다.
                .andExpect(content().string(hello)); // mvc.perform의 결과를 검증한다. 응답 본문의 내용을 검증한다. Controller 에서 "hello" 를 리턴하기 때문에 이 값이 맞는지 검증한다.
    }

    @WithMockUser(roles="USER") // 가짜로 인증된 사용자 생성
    @Test
    public void helloDto가_리턴된다() throws Exception {
        String name = "hello";
        int amount = 100;

        mvc.perform(
                get("/hello/dto")
                        .param("name", name)                        // param : API 테스트할 때 사용될 요청 파라미터를 설정한다.
                        .param("amount", String.valueOf(amount)))   // 단, 값은 String만 허용된다 -> 숫자/날짜 등의 데이터도 등록할 때는 문자열로 변경해야만 가능하다.
                        .andExpect(status().isOk())                       // is 메소드 import 시 관련 목록이 뜨지 않아 책을 참고하여 import static org.hamcrest.Matchers.is; 를 수기로 직접 입력해줬다.
                        .andExpect(jsonPath("$.name", is(name))) // jsonPath : JSON 응답값을 필드별로 검증할 수 있는 메소드이다. $를 기준으로 필드명을 명시한다.
                        .andExpect(jsonPath("$.amount", is(amount))); // 여기서는 name과 amount를 검증하니 $.name, $.amount로 검증한다.

    }


}

