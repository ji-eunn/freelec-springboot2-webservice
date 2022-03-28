package com.jojoldu.book.springboot.domain.posts;

import com.jojoldu.book.springboot.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

// 롬복 어노테이션
@Getter             // 클래스 내 모든 필드의 Getter 메소드를 자동생성
@NoArgsConstructor  // 기본 생성자 자동 추가 - public Posts() {} 와 같은 효과

// JPA 어노테이션
@Entity // 1. 테이블과 링크될 클래스임을 나타낸다. 기본값으로 클래스의 카멜케이스 이름을 언더스코어 네이밍(_)으로 테이블 이름을 매칭한다. ex)SalesManager.java -> sales_manager table
public class Posts extends BaseTimeEntity { // Posts클래스는 실제 DB 테이블과 매칭될 클래스이며 보통 Entity 클래스라고도 한다.
                     // JPA를 사용하면 DB 데이터에 작업할 경우 실제 쿼리를 날리기보다는, 이 Entity 클래스의 수정을 통해 작업한다.

    @Id // 2. 해당 테이블의 PK를 나타낸다.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // PK의 생성 규칙을 나타낸다. 스프링 부트 2.0 에서는 GenerationType.IDENTITY 옵션을 추가해야만 auto_increment가 된다.
    private Long id;                                    // 왠만하면 Entity의 PK는 Long 타입의 Auto_increment를 추천한다.
                                                        // 주민등록번호, 복합키 등은 PK로 잡을 경우 아래와 같이 난감한 상황이 종종 발생한다.
                                                        // (1) FK를 맺을 때 다른 테이블에서 복합키 전부를 갖고 있거나, 중간 테이블을 하나 더 둬야 하는 상황이 발생한다.
                                                        // (2) 인덱스에 좋은 영향을 끼치지 못한다.
                                                        // (3) 유니크한 조건이 변경될 경우 PK 전체를 수정해야 하는 일이 발생한다.
                                                        // 따라서, 주민등록번호와 복합키 등은 유니크 키로 별도로 추가하는 것을 추천한다.

    @Column(length = 500, nullable = false) // 테이블의 칼럼을 나타내며 굳이 선언하지 않더라도 해당 클래스의 필드는 모두 컬럼이 된다. 사용하는 이유는, 기본값 외에 추갈 변경이 필요한 옵션이 있으면 사용한다.
    private String title;                   // 문자열의 경우 VARCHAR(255)가 기본값인데, 사이즈를 500으로 늘리고 싶거나(ex:title),

    @Column(columnDefinition = "TEXT", nullable = false) //  타입을 TEXT로 변경하고 싶거나(ex:content) 등의 경우에 사용된다.
    private String content;

    private String author;

    // 롬복 어노테이션
    @Builder // 해당 클래스의 빌더 패턴 클래스를 생성. 생성자 상단에 선언 시 생성자에 포함된 필드만 빌더에 포함
    public Posts(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }  // 서비스 초기 구축 단계에선 테이블 설계(여기선 Entity 설계)가 빈번하게 변경되는데, 이떄 롬복의 어노테이션들은 코드 변경량을 최소화시켜 주기 때문에 적극적으로 사용한다.

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}

// 이 Posts 클래스에는 한 가지 특이점이 있는데, Setter 메소드가 없다는 것이다.
// 자바빈 규약을 생각하면서 getter/setter를 무작정 생성하는 경우가 있는데 이렇게 되면 해당 클래스의 인스턴스 값들이 언제 어디서 변해야 하는지 코드상으로 명확하게 구분할 수가 없어,
// 차후 기능 변경 시 정말 복잡해진다. 그래서 Entity 클래스에서는 절대 Setter 메소드를 만들지 않는다. 대신, 해당 필드의 값 변경이 필요하면 명확히 그 목적과 의도를 나타낼 수 있는 메소드를 추가해야만 한다.
// 예를 들어 주문 취소 메소드를 만든다고 가정하면 다음 코드로 비교해 보면 된다.

/* 잘못된 사용 예 */
//public class Order {
//    public void setStatus(boolean status){
//        this.status = status
//    }
//}
//
//public void 주문서비스의_취소이벤트(){
//    order.setStatus(false);
//}

/* 올바른 사용 예 */
//public class Order {
//    public void cancelOrder(){
//        this.status = false;
//    }
//}
//
//    public void 주문서비스의_취소이벤트(){
//        order.cancelOrder();
//    }

// 그럼, Setter가 없는 상황에서 어떻게 값을 채워 DB에 삽입(insert)해야 할까?
// 기본적인 구조는 생성자를 통해 최종값을 채운 후 DB에 삽입하는 것이며, 값 변경이 필요한 경우 해당 이벤트에 맞는 public 메소드를 호출하여 변경하는 것을 전제로 한다.
// 이 책에서는 생성자 대신에 @Builder를 통해 제공되는 빌더 클래스를 사용하는데, 생성자나 빌더나 생성 시점에 값을 채워주는 역할은 똑같다.
// 다만, 생성자의 경우 지금 채워야 할 필드가 무엇인지 명확히 지정할 수가 없다.
// 예를 들어 다음과 같이 생성자가 있다면 개발자가 new Example(b,a)처럼 a와 b의 위치를 변경해도 코드를 실행하기 전까지는 문제를 찾을 수가 없다.
// public Example(String a, String b){
//      this.a = a;
//      this.b = b;
// }
// 하지만, 빌더를 사용하게 되면 다음과 같이 어느 필드에 어떤 값을 채워야 할지 명확하게 인지할 수 있다.
// Example.builder()
//      .a(a)
//      .b(b)
//      .build();
