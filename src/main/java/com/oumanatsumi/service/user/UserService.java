package com.oumanatsumi.service.user;

import com.oumanatsumi.dao.role.RoleDAO;
import com.oumanatsumi.pojo.Role;
import com.oumanatsumi.pojo.User;

import java.util.List;

public interface UserService {
    // 用户登录
    public User login(String userCode, String userPassword);

    // 根据user修改密码
    public boolean updatePwd(int id,String pwd);

    // 根据条件查询出用户表记录数
    public int getUserCount(String queryUserName, int queryUserRole);

    // 根据条件查询出用户列表
    public List<User> getUserList(String queryUserName, int queryUserRole, int currentPageNo, int pageSize);

    // 添加用户
    public boolean addUser(User user);
}
