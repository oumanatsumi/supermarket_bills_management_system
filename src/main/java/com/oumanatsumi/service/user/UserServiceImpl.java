package com.oumanatsumi.service.user;

import com.oumanatsumi.dao.BaseDAO;
import com.oumanatsumi.dao.user.UserDAO;
import com.oumanatsumi.dao.user.UserDAOImpl;
import com.oumanatsumi.pojo.User;
import org.junit.Test;

import java.sql.Connection;

public class UserServiceImpl implements UserService{
    private UserDAO userDAO;
    public UserServiceImpl(){
        userDAO = new UserDAOImpl();
    }

    @Override
    public User login(String userCode, String userPassword) {
        Connection connection = null;
        User user = null;

        try {
            connection = BaseDAO.getConnection();
            // 通过业务层调用对应的具体的数据库操作
            user = userDAO.getLoginUser(connection,userCode);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            BaseDAO.closeResult(connection,null,null);
        }

        // 匹配密码
        if(null != user){
            if(!user.getUserPassword().equals(userPassword))
                user = null;
        }

        return user;
    }

    @Test
    public void test(){
        UserServiceImpl userService = new UserServiceImpl();
        User admin = userService.login("admin", "1234567");
        System.out.println(admin.getUserPassword());
    }


}
