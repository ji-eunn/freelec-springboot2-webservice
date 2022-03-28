package com.jojoldu.book.springboot.service;

import com.jojoldu.book.springboot.domain.posts.Posts;
import com.jojoldu.book.springboot.domain.posts.PostsRepository;
import com.jojoldu.book.springboot.web.dto.PostsListResponseDto;
import com.jojoldu.book.springboot.web.dto.PostsResponseDto;
import com.jojoldu.book.springboot.web.dto.PostsSaveRequestDto;
import com.jojoldu.book.springboot.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id=" + id));

        posts.update(requestDto.getTitle(), requestDto.getContent());
        // 여기서 JPA의 영속성 컨텍스트(엔티티를 영구 저장하는 환경)로
        // 만약, JPA의 엔티티 매니저가 활성화된 상태로 트랜잭션 안에서 데이터베이스안의 데이터를 가져오면
        // 이 데이터는 영속성 컨텍스트가 유지된 상태이다.
        // 이 상태에서 해당 데이터 값을 변경하면 트랜잭션이 끝나는 시점에 해당 테이블에 변경분을 반영한다.
        // 즉, Entity 객체으 ㅣ값만 변경하면 별도로 Update 쿼리를 날릴 필요가 없다는 것이다. 이 개념이 더티 체킹(Dirty Checking)이다.
        return id;
    }

    @Transactional(readOnly = true)
    public PostsResponseDto findById(Long id) {
        Posts entity = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        return new PostsResponseDto(entity);
    }

    @Transactional(readOnly = true) // (readOnly = true) 옵션을 줄 경우, 트랜잭션 범위는 유지하되
                                    // 조회 기능만 남겨두어 조회 속도가 개선되기 때문에 등록,수정,삭제 기능이 전혀 없는
                                    // 서비스 메소드에서 사용하는 것을 추천
    public List<PostsListResponseDto> findAllDesc() {
        return postsRepository.findAllDesc().stream()
                .map(PostsListResponseDto::new) // .map(posts -> new PostsListResponseDto(posts))
                                                // postRepository 결과로 넘어온 Posts의 Stream을
                                                // map을 통해 PostsListReponseDto 변환 -> List로 반환하는 메소드
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete (Long id) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
            postsRepository.delete(posts); // JpaRepository에서 지원하고 있는 delete 메소드 활용
                                           // 엔티티를 파라미터로 삭제할 수도 있고, deleteById 메소드를 이용하면 id로 삭제할 수도 있음
                                           // 존재하는 Posts인지 확인을 위해 엔티티 조회 후 그대로 삭제
    }
}
