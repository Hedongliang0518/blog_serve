package com.hdl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hdl.domain.ResponseResult;
import com.hdl.domain.entity.Category;

/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2024-07-02 15:08:50
 */
public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();
}

