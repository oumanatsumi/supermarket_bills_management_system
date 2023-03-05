package com.oumanatsumi.service.user;

import com.oumanatsumi.pojo.User;

public interface UserService {
    // 用户登录
    public User login(String userCode, String userPassword);

    // 根据user修改密码
    public boolean updatePwd(int id,String pwd);
}
