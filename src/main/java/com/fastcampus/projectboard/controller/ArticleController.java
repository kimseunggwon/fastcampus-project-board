package com.fastcampus.projectboard.controller;

import com.fastcampus.projectboard.response.ArticleResponse;
import com.fastcampus.projectboard.response.ArticleWithCommentResponse;
import com.fastcampus.projectboard.service.ArticleService;
import com.fastcampus.projectboard.type.SearchType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/articles")
@Controller
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping
    public String articles(
            @RequestParam(required = false) SearchType searchType, // 검색어 받아오기
            @RequestParam(required = false) String searchValue,
            @PageableDefault(size = 10,sort = "createdAt",direction = Sort.Direction.DESC) Pageable pageable, // 시간순 + 내림차순
            ModelMap map) {

        // ArticleResponse = 게시글에 대한 내용
        map.addAttribute("articles", articleService.searchArticles(searchType,searchValue,pageable).map(ArticleResponse::from));

        return "articles/index";
    }

    /** 상세 페이지 ( 단건 조회 ) 게시글 페이지 보여주기
     */
    @GetMapping("/{articleId}")
    public String article(@PathVariable Long articleId,ModelMap map) {

        // ArticleWithCommentResponse 으로 바꿔서 보내주도록 코드를 작성
        // ArticleWithCommentResponse => dto 받아서 치환을 한다.
        ArticleWithCommentResponse article = ArticleWithCommentResponse.from(articleService.getArticle(articleId));
        map.addAttribute("article" ,article);
        map.addAttribute("articleComments",article.articleCommentResponses());

        return "articles/detail";
    }

}
