package com.hdl.controller;

import com.hdl.domain.ResponseResult;
import com.hdl.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired  // 自动注入Service
    private ArticleService articleService;

    /**
     * 查询热门文章
     * @return
     */
    @GetMapping("/hotArticleList")
    public ResponseResult hotArticleList() {
        ResponseResult result = articleService.hotArticleList();
        return result;
    }

    /**
     * 查询文章列表
     * @return
     */
    @GetMapping("/articleList")
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        return articleService.articleList(pageNum, pageSize, categoryId);
    }

    /**
     * 查询文章详情
     * @return
     */
    @GetMapping("/{id}")
    public ResponseResult getArticleDetial(@PathVariable("id") Long id) {
        return articleService.getArticleDetial(id);
    }
}
