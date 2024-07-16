package com.hdl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.hdl.constants.SystemConstants;
import com.hdl.domain.ResponseResult;
import com.hdl.domain.entity.Article;
import com.hdl.domain.entity.Category;
import com.hdl.domain.vo.CategoryVo;
import com.hdl.mapper.CategoryMapper;
import com.hdl.service.ArticleService;
import com.hdl.service.CategoryService;
import com.hdl.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 分类表(Category)表服务实现类
 * 获取文章分类
 * @author makejava
 * @since 2024-07-02 15:13:50
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private ArticleService articleService;
    @Override
    public ResponseResult getCategoryList() {
        // 查询文章表状态是已发布的文章
        LambdaQueryWrapper<Article> articleWrapper = new LambdaQueryWrapper<>();
        articleWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Article> articleList = articleService.list(articleWrapper);
        // 获取文章分类id,去重
        Set<Long> categoryIds = articleList.stream()
                .map(article -> article.getCategoryId())
                .collect(Collectors.toSet());
        // 查询分类表
        List<Category> categories = listByIds(categoryIds);
        categories.stream()
                .filter(category -> SystemConstants.STATUS_NORMAL.equals(category.getStatus()));
        // 封装VO
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categories, CategoryVo.class);
        return ResponseResult.okResult(categoryVos);
    }
}

