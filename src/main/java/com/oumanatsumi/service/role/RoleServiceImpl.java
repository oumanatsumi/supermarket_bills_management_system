package com.oumanatsumi.service.role;

import com.oumanatsumi.dao.BaseDAO;
import com.oumanatsumi.dao.role.RoleDAO;
import com.oumanatsumi.dao.role.RoleDAOImpl;
import com.oumanatsumi.pojo.Role;

import java.sql.Connection;
import java.util.List;

public class RoleServiceImpl implements RoleService{
    private RoleDAO roleDao;

    public RoleServiceImpl(){
        roleDao = new RoleDAOImpl();
    }

    public List<Role> getRoleList() {
        Connection connection = null;
        List<Role> roleList = null;
        try {
            connection = BaseDAO.getConnection();
            roleList = roleDao.getRoleList(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            BaseDAO.closeResult(connection, null, null);
        }
        return roleList;
    }
}