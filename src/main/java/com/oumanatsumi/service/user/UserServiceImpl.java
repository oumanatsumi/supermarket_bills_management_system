package com.oumanatsumi.service.user;

import com.oumanatsumi.dao.BaseDAO;
import com.oumanatsumi.dao.role.RoleDAO;
import com.oumanatsumi.dao.role.RoleDAOImpl;
import com.oumanatsumi.dao.user.UserDAO;
import com.oumanatsumi.dao.user.UserDAOImpl;
import com.oumanatsumi.pojo.Role;
import com.oumanatsumi.pojo.User;
import org.junit.Test;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class UserServiceImpl implements UserService{

    private UserDAO userDAO;
    public UserServiceImpl()
    {
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

    @Override
    public boolean updatePwd(int id, String pwd) {
        System.out.println("UserServlet2:" + pwd);

        boolean flag = false;
        Connection connection = null;
        try{
            connection = BaseDAO.getConnection();
            if(userDAO.updatePwd(connection,id,pwd) > 0) {
                flag = true;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally{
            BaseDAO.closeResult(connection, null, null);
        }
        return flag;
    }

    @Override
    public int getUserCount(String queryUserName, int queryUserRole) {
        int result = 0;
        Connection connection = null;
        try{
            connection = BaseDAO.getConnection();
            result = userDAO.getUserCount(connection,queryUserName,queryUserRole);
        }catch (Exception e) {
            e.printStackTrace();
        }finally{
            BaseDAO.closeResult(connection, null, null);
        }
        return result;
    }

    @Override
    public List<User> getUserList(String queryUserName, int queryUserRole, int currentPageNo, int pageSize) {
        List<User> userList = new ArrayList<>();
        Connection connection = null;
        try{
            connection = BaseDAO.getConnection();
            userList = userDAO.getUserList(connection,queryUserName,queryUserRole,currentPageNo,pageSize);
        }catch (Exception e) {
            e.printStackTrace();
        }finally{
            BaseDAO.closeResult(connection, null, null);
        }
        return userList;
    }

    @Override
    public boolean addUser(User user) {
        boolean flag = false;
        Connection connection = null;
        try {
            connection = BaseDAO.getConnection();
            if(userDAO.add(connection,user) > 0){
                flag = true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            BaseDAO.closeResult(connection,null,null);
        }
        return flag;
    }

    @Test
    public void test(){
        UserServiceImpl userService = new UserServiceImpl();
        int count = userService.getUserCount(null,0);
        System.out.println(count);
    }


}
