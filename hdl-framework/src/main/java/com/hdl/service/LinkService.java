package com.hdl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hdl.domain.ResponseResult;
import com.hdl.domain.entity.Link;


/**
 * 友链(Link)表服务接口
 *
 * @author makejava
 * @since 2024-07-03 09:20:44
 */
public interface LinkService extends IService<Link>{

    ResponseResult getAllLink();
}

