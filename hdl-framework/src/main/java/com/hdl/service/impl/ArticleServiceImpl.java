package com.hdl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hdl.constants.SystemConstants;
import com.hdl.domain.ResponseResult;
import com.hdl.domain.entity.Article;
import com.hdl.domain.entity.Category;
import com.hdl.domain.vo.ArticleDetailVo;
import com.hdl.domain.vo.ArticleListVo;
import com.hdl.domain.vo.HotArticleVo;
import com.hdl.domain.vo.PageVo;
import com.hdl.mapper.ArticleMapper;
import com.hdl.service.ArticleService;
import com.hdl.service.CategoryService;
import com.hdl.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 分类表(Article)表服务接口
 * 获取热门文章接口
 * @author makejava
 * @since 2024-07-02 15:08:50
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private CategoryService categoryService;
    @Override
    public ResponseResult hotArticleList() {
        //查询热门文章 封装成ResponseResult返回
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //必须是正式文章
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        //按照浏览量进行排序
        queryWrapper.orderByDesc(Article::getViewCount);
        //最多只查询10条
        Page<Article> page = new Page(1,10);
        page(page,queryWrapper);

        List<Article> articles = page.getRecords();

        List<HotArticleVo> vs = BeanCopyUtils.copyBeanList(articles, HotArticleVo.class);
        return ResponseResult.okResult(vs);
    }

    /**
     * 查询文章接口
     * @param pageNum
     * @param pageSize
     * @param categoryId
     * @return
     */

    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        //查询条件
        LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 如果 有categoryId 就要 查询时要和传入的相同
        lambdaQueryWrapper.eq(Objects.nonNull(categoryId)&&categoryId>0 ,Article::getCategoryId,categoryId);
        // 状态是正式发布的
        lambdaQueryWrapper.eq(Article::getStatus,SystemConstants.ARTICLE_STATUS_NORMAL);
        // 对isTop进行降序
        lambdaQueryWrapper.orderByDesc(Article::getIsTop);

        //分页查询
        Page<Article> page = new Page<>(pageNum,pageSize);
        super.page(page, lambdaQueryWrapper);

        //查询categoryName
        List<Article> articles = page.getRecords();
        articles.stream()
                .map(article -> article.setCategoryName(categoryService.getById(article.getCategoryId()).getName()))
                .collect(Collectors.toList());

        // 封装查询结果
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(page.getRecords(), ArticleListVo.class);
        PageVo pageVo = new PageVo(articleListVos, page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    /**
     * 查询文章详情
     * @param id
     * @return
     */
    @Override
    public ResponseResult getArticleDetial(Long id) {
        //根据id查询文章
        Article article = getById(id);
        //从redis中获取viewCount
//        Integer viewCount = redisCache.getCacheMapValue("article:viewCount", id.toString());
//        article.setViewCount(viewCount.longValue());
        //转换成VO
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
        //根据分类id查询分类名
        Long categoryId = articleDetailVo.getCategoryId();
        Category category = categoryService.getById(categoryId);
        if(category!=null){
            articleDetailVo.setCategoryName(category.getName());
        }
        //封装响应返回
        return ResponseResult.okResult(articleDetailVo);
    }


}
