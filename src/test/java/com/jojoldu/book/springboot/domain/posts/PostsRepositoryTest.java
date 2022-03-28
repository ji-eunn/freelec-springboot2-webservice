package com.jojoldu.book.springboot.domain.posts;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class PostsRepositoryTest { // PostsRepositoryTest에서는 save,findAll 기능을 테스트 한다.

    @Autowired
    PostsRepository postsRepository;

    @AfterEach // Junit에서 단위 테스트가 끝날 때마다 수행되는 메소드를 지정
           // 보통은 배포 전 전체 테스트를 수행할 때 테스트간 데이터 침범을 막기 위해 사용한다.
           // 여러 테스트가 동시에 수행되면 테스트용 데이터베이스인 H2에 데이터가 그대로 남아 있어 다음 테스트 실행 시 테스트가 실패할 수 있다.
    public void cleanup(){
        postsRepository.deleteAll();
    }

    @Test
    public void 게시글저장_불러오기(){ // application.properties 파일에서 출력되는 쿼리를 MYSQL 버전으로 변경하는 부분은 적용하지 못함(에러발생)
        //given
        String title = "테스트 게시글";
        String content = "테스트 본문";

        postsRepository.save(Posts.builder() // postsRepository.save : 테이블 posts에 insert/update 쿼리를 실행한다 -> id값이 있따면 update, 없다면 insert 쿼리가 실행된다.
                       .title(title)
                       .content(content)
                       .author("jojoldu@gmail.com")
                       .build());
        //when
        List<Posts> postsList = postsRepository.findAll(); // postsRepository.findAl : 테이블에 있는 모든 데이터를 조회해오는 메소드

        //then
        Posts posts = postsList.get(0);
        assertThat(posts.getTitle()).isEqualTo(title);
        assertThat(posts.getContent()).isEqualTo(content);
    } // 별다른 설정 없이 @SpringBootTest를 사용할 경우 H2 데이터베이스를 자동으로 실행해 준다.
      // 이 테스트 역시 실행할 경우 H2가 자동으로 실행된다.
      // "실제로 실행된 쿼리는 어떤 형태일까?", "실행된 쿼리를 로그로 볼 수는 없을까요?"
      // 물론 쿼리 로그를 ON/OFF할 수 있는 설정이 있다. 다만, 이런 설정들을 Java 클래스로 구현할 수 있으나
      // 스프링 부트에서는 application.properties, application.yml 등의 파일로 한 줄의 코드로 설정할 수 있도록 지원하고 권장하니 이를 사용하면 된다.
      // src/main/resources 디렉토리 아래에 application.properties 파일을 생성한다.

    @Test
    public void BaseTimeEntity_등록() {
        //given
        LocalDateTime now = LocalDateTime.of(2019, 6, 4, 0, 0, 0);
        postsRepository.save(Posts.builder()
                .title("title")
                .content("content")
                .author("author")
                .build());
        //when

        List<Posts> postsList = postsRepository.findAll();

        //then
        Posts posts = postsList.get(0);

        System.out.println(">>>>>>>>> createDate=" + posts.getCreatedDate() + ", modifiedDate=" + posts.getModifiedDate());

        assertThat(posts.getCreatedDate()).isAfter(now);
        assertThat(posts.getModifiedDate()).isAfter(now);
    }
}
