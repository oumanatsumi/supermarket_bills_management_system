package com.oumanatsumi.dao.role;

import com.oumanatsumi.pojo.Role;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface RoleDAO {

    // 获取角色列表
    public List<Role> getRoleList(Connection connection) throws SQLException;

}
