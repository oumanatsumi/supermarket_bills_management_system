package com.oumanatsumi.dao.role;

import com.oumanatsumi.dao.BaseDAO;
import com.oumanatsumi.pojo.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleDAOImpl implements RoleDAO{

    @Override
    public List<Role> getRoleList(Connection connection) throws SQLException {
        PreparedStatement pstm = null;
        ResultSet rs = null;

        List<Role> roleList = new ArrayList<>();
        Object[] params = {};
        if(connection!=null){
            String sql = "select * from smbms_role";
            rs = BaseDAO.execute(connection,pstm,rs,sql,params);

            while (rs.next()){
                Role _role = new Role();
                _role.setId(rs.getInt("id"));
                _role.setRoleName(rs.getString("roleName"));
                _role.setRoleCode(rs.getString("roleCode"));
                roleList.add(_role);
            }
            BaseDAO.closeResult(connection,pstm,rs);
        }
        return roleList;
    }
}
