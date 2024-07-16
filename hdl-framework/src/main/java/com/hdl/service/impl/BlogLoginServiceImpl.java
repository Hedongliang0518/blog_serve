package com.hdl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hdl.domain.ResponseResult;
import com.hdl.domain.entity.User;
import com.hdl.domain.vo.BlogUserLoginVo;
import com.hdl.domain.vo.UserInfoVo;
import com.hdl.mapper.UserMapper;
import com.hdl.service.BlogLoginService;
import com.hdl.utils.BeanCopyUtils;
import com.hdl.utils.JwtUtil;
import com.hdl.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class BlogLoginServiceImpl implements BlogLoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    /**
     * 用户登录接口
     * @param user
     * @return
     */
    @Override
    public ResponseResult login(User user) {
        //根据用户名查询用户信息
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName,user.getUserName());
        User userInfo = userMapper.selectOne(queryWrapper);
        //判断是否查到用户  如果没查到抛出异常
        if(Objects.isNull(userInfo)){
            throw new RuntimeException("用户不存在");
        }
        String userId = userInfo.getId().toString();
        //把用户信息存入redis
        redisCache.setCacheObject("bloglogin:"+userId,userInfo);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (encoder.matches(user.getPassword(), userInfo.getPassword())){
            String jwt = JwtUtil.createJWT(userId);
            //把token和userinfo封装 返回
            //把User转换成UserInfoVo
            UserInfoVo userInfoVo = BeanCopyUtils.copyBean(userInfo, UserInfoVo.class);
            BlogUserLoginVo vo = new BlogUserLoginVo(jwt,userInfoVo);
            return ResponseResult.okResult(vo);
        }
        return ResponseResult.errorResult(505, "账号或密码错误");
    }

    /**
     * 用户退出接口
     * @return
     */
    @Override
    public ResponseResult logout() {
        //获取token 解析获取userid
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loginUser = (User) authentication.getPrincipal();
        //获取userid
        Long userId = loginUser.getId();
        //删除redis中的用户信息
        redisCache.deleteObject("bloglogin:"+userId);
        return ResponseResult.okResult();
    }
}
