package com.jojoldu.book.springboot.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

// @Repository를 추가할 필요도 없다.
// 하지만 주의할 점은, Entity클래스와 기본 Entity Repository는 함께 위치해야한다는 것이다.
// 둘은 아주 밀접한 관계이기 때문에 Entity클래스는 기본 Repository 없이는 제대로 역할을 할 수가 없다.
// 나중에 프로젝트가 커져 도메인별로 프로젝트를 분리해야한다면
// 이때 Entity클래스와 기본 Repository는 함께 움직여야 하므로 도메인 패키지에서 함께 관리한다.
public interface PostsRepository extends JpaRepository<Posts, Long>{ // Posts 클래스로 Database를 접근하게 해줄 JpaRepository
                                                                     // 보통, Mybatis 에서 Dao라고 불리는 DB Layer 접근자이다.
                                                                     // JPA에선 Repository라고 부르며 인터페이스로 생성한다.
                                                                     // 단순히 인터페이스를 생성 후, JpaRepository<Entity클래스, PK타입>를 상속하면
                                                                     // 기본적인 CRUD 메소드가 자동으로 생성된다.

    // SpringDataJpa에서 제공하지 않는 메소드는 @Query 를 사용하여 쿼리로 작성해도 된다.
    // 실제로 아래 코드는 SpringDataJpa에서 제공하는 기본 메소드만으로 해결이 가능하나, @Query가 훨씬 가독성이 좋으니 선택해서 사용하면 된다.
    @Query("SELECT p FROM Posts p ORDER BY p.id DESC")
    List<Posts> findAllDesc();
}
