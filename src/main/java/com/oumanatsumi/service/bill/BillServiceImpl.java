package com.oumanatsumi.service.bill;

import com.oumanatsumi.dao.BaseDAO;
import com.oumanatsumi.dao.bill.BillDAO;
import com.oumanatsumi.dao.bill.BillDAOImpl;
import com.oumanatsumi.pojo.Bill;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class BillServiceImpl implements BillService {
    private BillDAO billDao;
    public BillServiceImpl(){
        billDao = new BillDAOImpl();
    }

    // 增加订单
    public boolean add(Bill bill) {
        boolean flag = false;
        Connection connection = null;
        try {
            connection = BaseDAO.getConnection();
            // 开启JDBC事务管理
            connection.setAutoCommit(false);
            if(billDao.add(connection,bill) > 0) {
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

    // 通过查询条件获取供应商列表-模糊查询-getBillList
    public List<Bill> getBillList(Bill bill) {
        Connection connection = null;
        List<Bill> billList = null;
        System.out.println("query productName ---- > " + bill.getProductName());
        System.out.println("query providerId ---- > " + bill.getProviderId());
        System.out.println("query isPayment ---- > " + bill.getIsPayment());

        try {
            connection = BaseDAO.getConnection();
            billList = billDao.getBillList(connection, bill);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            BaseDAO.closeResult(connection, null, null);
        }

        return billList;
    }

    // 通过delId删除Bill
    public boolean deleteBillById(String delId) {
        Connection connection = null;
        boolean flag = false;

        try {
            connection = BaseDAO.getConnection();
            if(billDao.deleteBillById(connection, delId) > 0) {
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            BaseDAO.closeResult(connection, null, null);
        }

        return flag;
    }

    // 通过billId获取Bill
    public Bill getBillById(String id) {
        Bill bill = null;
        Connection connection = null;

        try{
            connection = BaseDAO.getConnection();
            bill = billDao.getBillById(connection, id);
        }catch (Exception e) {
            e.printStackTrace();
            bill = null;
        }finally{
            BaseDAO.closeResult(connection, null, null);
        }

        return bill;
    }

    // 修改订单信息
    public boolean modify(Bill bill) {
        Connection connection = null;
        boolean flag = false;

        try {
            connection = BaseDAO.getConnection();
            if(billDao.modify(connection,bill) > 0) {
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
