package com.hdl.service;

import com.hdl.domain.ResponseResult;
import com.hdl.domain.entity.User;

public interface BlogLoginService {

    ResponseResult login(User user);
    ResponseResult logout();
}
