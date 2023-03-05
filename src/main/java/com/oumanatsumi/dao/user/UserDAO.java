package com.oumanatsumi.dao.user;

import com.oumanatsumi.dao.BaseDAO;
import com.oumanatsumi.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;

public interface UserDAO {
    // 得到要登录的用户
    public User getLoginUser(Connection connection, String userCode) throws Exception;
    // 修改当前用户密码
    public int updatePwd(Connection connection,int id,String pwd) throws SQLException;
}
