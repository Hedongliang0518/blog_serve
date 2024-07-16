package com.hdl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hdl.domain.entity.UserRole;
import com.hdl.mapper.UserRoleMapper;
import com.hdl.service.UserRoleService;
import org.springframework.stereotype.Service;

/**
 * @Author
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {
}
