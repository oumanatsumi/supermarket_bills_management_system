package com.oumanatsumi.dao.user;

import com.oumanatsumi.dao.BaseDAO;
import com.oumanatsumi.pojo.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAOImpl implements UserDAO {
    // 得到要登录的用户
    @Override
    public User getLoginUser(Connection connection, String userCode) throws Exception {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        User user = null;

        if (connection != null) {
            String sql = "select * from smbms_user where userCode=?";
            Object[] params = {userCode};

            rs = BaseDAO.execute(connection, pstm, rs, sql, params);

            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUserCode(rs.getString("userCode"));
                user.setUserName(rs.getString("userName"));
                user.setUserPassword(rs.getString("userPassword"));
                user.setGender(rs.getInt("gender"));
                user.setBirthday(rs.getDate("birthday"));
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("address"));
                user.setUserRole(rs.getInt("userRole"));
                user.setCreatedBy(rs.getInt("createdBy"));
                user.setCreationDate(rs.getTimestamp("creationDate"));
                user.setModifyBy(rs.getInt("modifyBy"));
                user.setModifyDate(rs.getTimestamp("modifyDate"));
            }
            BaseDAO.closeResult(null, pstm, rs);
        }

        return user;
    }

    // 修改当前用户密码
    @Override
    public int updatePwd(Connection connection, int id, String pwd) throws SQLException {
        PreparedStatement pstm = null;
        int ex = 0;
        if (connection != null) {
            String sql = "update smbms_user set userPassword= ? where id = ?";
            Object parms[] = {pwd, id};
            ex = BaseDAO.execute(connection, pstm, sql, parms);
            BaseDAO.closeResult(connection, pstm, null);
        }
        return ex;
    }
}
