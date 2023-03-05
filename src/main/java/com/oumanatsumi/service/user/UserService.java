package com.oumanatsumi.service.user;

import com.oumanatsumi.pojo.User;

public interface UserService {
    // 用户登录
    public User login(String userCode, String userPassword);
}
