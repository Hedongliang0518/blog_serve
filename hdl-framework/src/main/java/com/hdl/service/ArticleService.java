package com.hdl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hdl.domain.ResponseResult;
import com.hdl.domain.entity.Article;
public interface ArticleService extends IService<Article> {
    ResponseResult hotArticleList();

    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult getArticleDetial(Long id);
}
