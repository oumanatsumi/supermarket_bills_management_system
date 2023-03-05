package com.oumanatsumi.dao.user;

import com.oumanatsumi.dao.BaseDAO;
import com.oumanatsumi.pojo.User;

import java.sql.Connection;

public interface UserDAO {
    // 得到要登录的用户
    public User getLoginUser(Connection connection, String userCode) throws Exception;
}
