package com.jojoldu.book.springboot.web.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat; // JUnit의 기본 assertThat이 아닌 assertj의 assertThat을 사용했는데,
                                                          // assertj 역시 JUnit에서 자동으로 라이브러리 등록을 해준다.
                                                          // JUnit과 비교하여 assertj의 장점은 다음과 같다.
                                                          // 1. CoreMatchers와 달리 추가적으로 라이브러리가 필요하지 않다.
                                                          //    JUnit의 assertThat을 쓰게 되면 is()와 같이 CoreMatchers 라이브러리가 필요하다
                                                          // 2. 자동완성이 좀 더 확실하게 지원된다.
                                                          //    IDE에서는 CoreMatchers와 같은 Matcher라이브러리의 자동완성 지원이 약하다.
public class HelloResponseDtoTest {

    @Test
    public void 롬복_기능_테스트() {
        //given
        String name = "test";
        int amount = 1000;

        //when
        HelloResponseDto dto = new HelloResponseDto(name, amount);

        //then
        assertThat(dto.getName()).isEqualTo(name);      // assertThat : assertj라는 테스트 검증 라이브러리의 검증 메소드
                                                        //              검증하고 싶은 대상을 메소드 인자로 받는다.
                                                        //              메소드 체이닝이 지원되어 isEqualTo와 같이 메소드를 이어서 사용할 수 있다.
        assertThat(dto.getAmount()).isEqualTo(amount);  // isEqualTo  : assertj의 동등 비교 메소드
                                                        //              assertThat에 있는 값과 isEqualTo의 값을 비교해서 같을 때만 성공이다.
    }
}
