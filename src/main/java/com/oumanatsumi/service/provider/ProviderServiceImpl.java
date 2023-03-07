package com.oumanatsumi.service.provider;

import com.oumanatsumi.dao.BaseDAO;
import com.oumanatsumi.dao.bill.BillDAO;
import com.oumanatsumi.dao.bill.BillDAOImpl;
import com.oumanatsumi.dao.provider.ProviderDAO;
import com.oumanatsumi.dao.provider.ProviderDAOImpl;
import com.oumanatsumi.pojo.Provider;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ProviderServiceImpl implements ProviderService{
    private ProviderDAO providerDao;
    private BillDAO billDao;

    public ProviderServiceImpl(){
        providerDao = new ProviderDAOImpl();
        billDao = new BillDAOImpl();
    }

    // 增加供应商
    public boolean add(Provider provider) {
        boolean flag = false;
        Connection connection = null;

        try {
            connection = BaseDAO.getConnection();
            // 开启JDBC事务管理
            connection.setAutoCommit(false);
            if(providerDao.add(connection,provider) > 0) {
                flag = true;
            }
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                System.out.println("rollback==================");
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }finally{
            // 在service层进行connection连接的关闭
            BaseDAO.closeResult(connection, null, null);
        }

        return flag;
    }

    // 通过供应商名称、编码获取供应商列表-模糊查询-providerList
    public List<Provider> getProviderList(String proName, String proCode) {
        Connection connection = null;
        List<Provider> providerList = null;

        System.out.println("query proName ---- > " + proName);
        System.out.println("query proCode ---- > " + proCode);

        try {
            connection = BaseDAO.getConnection();
            providerList = providerDao.getProviderList(connection, proName,proCode);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            BaseDAO.closeResult(connection, null, null);
        }

        return providerList;
    }

    // 通过proId删除Provider
    public int deleteProviderById(String delId) {
        Connection connection = null;
        int billCount = -1;

        try {
            connection = BaseDAO.getConnection();
            connection.setAutoCommit(false);
            billCount = billDao.getBillCountByProviderId(connection,delId);
            if(billCount == 0){
                providerDao.deleteProviderById(connection, delId);
            }
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
            billCount = -1;
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }finally{
            BaseDAO.closeResult(connection, null, null);
        }

        return billCount;
    }

    // 通过proId获取Provider
    public Provider getProviderById(String id) {
        Provider provider = null;
        Connection connection = null;

        try{
            connection = BaseDAO.getConnection();
            provider = providerDao.getProviderById(connection, id);
        }catch (Exception e) {
            e.printStackTrace();
            provider = null;
        }finally{
            BaseDAO.closeResult(connection, null, null);
        }

        return provider;
    }

    // 修改用户信息
    public boolean modify(Provider provider) {
        Connection connection = null;
        boolean flag = false;

        try {
            connection = BaseDAO.getConnection();
            if(providerDao.modify(connection,provider) > 0) {
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            BaseDAO.closeResult(connection, null, null);
        }

        return flag;
    }
}
