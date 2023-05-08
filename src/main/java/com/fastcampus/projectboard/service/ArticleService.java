package com.fastcampus.projectboard.service;

import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.dto.ArticleDto;
import com.fastcampus.projectboard.dto.ArticleUpdateDto;
import com.fastcampus.projectboard.dto.ArticleWithCommentsDto;
import com.fastcampus.projectboard.repository.ArticleRepository;
import com.fastcampus.projectboard.type.SearchType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class ArticleService {

    private final ArticleRepository articleRepository;

    /** 각각 어떤 cast 검색시 기사 찾기
     */
    @Transactional(readOnly = true)
    public Page<ArticleDto> searchArticles(SearchType searchType, String searchKeyword, Pageable pageable) {
        if (searchKeyword == null || searchKeyword.isBlank()) {
            return articleRepository.findAll(pageable).map(ArticleDto::from);
        }

        // 바로 return 할수있어서 case 에 break 가 필요없음
        return switch (searchType) {
            case TITLE -> articleRepository.findByTitleContaining(searchKeyword,pageable).map(ArticleDto::from);
            case CONTENT -> articleRepository.findByContentContaining(searchKeyword,pageable).map(ArticleDto::from);
            case ID-> articleRepository.findByUserAccount_UserIdContaining(searchKeyword,pageable).map(ArticleDto::from);
            case NICKNAME -> articleRepository.findByUserAccount_NicknameContaining(searchKeyword,pageable).map(ArticleDto::from);
            case HASHTAG -> articleRepository.findByHashtag("#" + searchKeyword,pageable).map(ArticleDto::from);
                                                                                                 //mapping =>  dto 변환
        };
    }

    /**
     * 단건 조회
     */
    @Transactional(readOnly = true)
    public ArticleWithCommentsDto getArticle(Long articleId) {
        return articleRepository.findById(articleId)
                .map(ArticleWithCommentsDto::from)
                //.map(article -> ArticleWithCommentsDto.from(article))
                .orElseThrow(()-> new EntityNotFoundException("게시글이 없습니다 - articleId: " + articleId));
    }


    public void saveArticle(ArticleDto dto) {
        articleRepository.save(dto.toEntity()); //toEntity dto 정보로 부터 entity 하나 만들어서 save 하는것
    }

    public void updateArticle(ArticleUpdateDto dto) {

        try {
            Article article = articleRepository.getReferenceById(dto.id()); // ReferenceById entity에 대한 기본 키 식별자를 통해 조회하기 위해
                                                                            // entity 전체 업데이트가 아닌, 일부 필드만 업데이트

            if (dto.title() != null) {article.setTitle(dto.title());}      //Java ArticleUpdateDto record ==> 알아서 get,set 생성해줌
            if (dto.content() != null) {article.setTitle(dto.content());}
            article.setHashtag(dto.hashtag()); // null field
        } catch (EntityNotFoundException e) {
            log.warn("게시글 업데이트 실패. 게시글을 찾을 수 없습니다. - dto: {}",dto);
        }
        // @Transactional 커밋되는 시점에 entity 변경된거를 기반으로 update 쿼리를 DB에 날리므로, 따로 저장을 안해도 된다.
    }

    public void deleteArticle(long articleId) {
        articleRepository.deleteById(articleId);
    }

}
