package com.hdl.utils;

import com.hdl.domain.ResponseResult;
import com.hdl.domain.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author
 */
public class SecurityUtils
{

    /**
     * 获取用户
     **/
    public static User getLoginUser()
    {
        try {
            return (User) getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new RuntimeException("请先进行登录");
        }
    }

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static Boolean isAdmin(){
        Long id = getLoginUser().getId();
        return id != null && id.equals(1L);
    }

    public static Long getUserId() {
        return getLoginUser().getId();
    }
}