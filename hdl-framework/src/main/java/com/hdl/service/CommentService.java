package com.hdl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hdl.domain.ResponseResult;
import com.hdl.domain.entity.Comment;


/**
 * 评论表(Comment)表服务接口
 *
 * @author makejava
 * @since 2024-07-16 23:49:33
 */
public interface CommentService extends IService<Comment> {

    ResponseResult commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize);

    ResponseResult addComment(Comment comment);
}

