package com.oumanatsumi.servlet.user;

import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;
import com.oumanatsumi.pojo.Role;
import com.oumanatsumi.pojo.User;
import com.oumanatsumi.service.role.RoleService;
import com.oumanatsumi.service.role.RoleServiceImpl;
import com.oumanatsumi.service.user.UserService;
import com.oumanatsumi.service.user.UserServiceImpl;
import com.oumanatsumi.utils.Constants;
import com.oumanatsumi.utils.PageSupport;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getParameter("method");

        System.out.println("method----> " + method);

        if(method != null && method.equals("savepwd")){
            this.updatePwd(request, response);
        } else if(method.equals("pwdmodify") && method!=null){
            this.pwdModify(request,response);
        } else if(method != null && method.equals("query")) {
            this.query(request,response);
        } else if(method != null && method.equals("add")){
            this.add(request, response);
        } else if(method != null && method.equals("getrolelist")){
            this.getRoleList(request, response);
        } else if(method != null && method.equals("ucexist")){
            this.userCodeExist(request, response);
        } else if(method != null && method.equals("deluser")){
            this.delUser(request, response);
        } else if(method != null && method.equals("view")){
            this.getUserById(request, response,"userview.jsp");
        } else if(method != null && method.equals("modify")){
            this.getUserById(request, response,"usermodify.jsp");
        } else if(method != null && method.equals("modifyexe")) {
            this.modify(request, response);
        }
    }

    // ????????????
    public void updatePwd(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // ???Session??????ID
        Object o = request.getSession().getAttribute(Constants.USER_SESSION);
        String newpassword = request.getParameter("newpassword");

        System.out.println("UserServlet3:"+newpassword);

        boolean flag = false;

        if(o != null && !StringUtils.isNullOrEmpty(newpassword)){
            UserService userService = new UserServiceImpl();
            flag = userService.updatePwd(((User)o).getId(),newpassword);
            if(flag){
                request.setAttribute(Constants.SYS_MESSAGE, "??????????????????,??????????????????????????????????????????");
                // ?????????????????????session??????
                request.getSession().removeAttribute(Constants.USER_SESSION);
            }else{
                request.setAttribute(Constants.SYS_MESSAGE, "?????????????????????");
            }
        }else{
            request.setAttribute(Constants.SYS_MESSAGE, "?????????????????????");
        }
        request.getRequestDispatcher("pwdmodify.jsp").forward(request, response);
    }

    // ???????????????,session?????????????????????
    public void pwdModify(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // ???Session??????ID
        Object o = request.getSession().getAttribute(Constants.USER_SESSION);
        String oldpassword = request.getParameter("oldpassword");
        Map<String, String> resultMap = new HashMap<String, String>();

        // session??????
        if(null == o ){
            resultMap.put("result", "sessionerror");
        }else if(StringUtils.isNullOrEmpty(oldpassword)){
            // ?????????????????????
            resultMap.put("result", "error");
        }else{
            String sessionPwd = ((User)o).getUserPassword();
            if(oldpassword.equals(sessionPwd)){
                resultMap.put("result", "true");
            }else{
                // ????????????????????????
                resultMap.put("result", "false");
            }
        }

        response.setContentType("application/json");
        PrintWriter outWriter = response.getWriter();
        // JSONArray ???????????????JSON?????????
        outWriter.write(JSONArray.toJSONString(resultMap));
        outWriter.flush();
        outWriter.close();

    }

    // ????????????
    public void query(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // ??????????????????
        String queryUserName = request.getParameter("queryname");
        String temp = request.getParameter("queryUserRole");
        String pageIndex = request.getParameter("pageIndex");
        int queryUserRole = 0;
        UserService userService = new UserServiceImpl();
        List<User> userList = null;

        // ??????????????????
        int pageSize = Constants.pageSize;

        // ????????????
        int currentPageNo = 1;

        System.out.println("queryUserName servlet-------->"+queryUserName);
        System.out.println("queryUserRole servlet-------->"+queryUserRole);
        System.out.println("query pageIndex----------> " + pageIndex);

        if(queryUserName == null){
            queryUserName = "";
        }
        if(temp != null && !temp.equals("")){
            queryUserRole = Integer.parseInt(temp);
        }

        if(pageIndex != null){
            try{
                currentPageNo = Integer.valueOf(pageIndex);
            }catch(NumberFormatException e){
                response.sendRedirect("error.jsp");
            }
        }
        // ??????????????????
        int totalCount	= userService.getUserCount(queryUserName,queryUserRole);
        // ?????????
        PageSupport pages=new PageSupport();
        pages.setCurrentPageNo(currentPageNo);
        pages.setPageSize(pageSize);
        pages.setTotalCount(totalCount);

        int totalPageCount = pages.getTotalPageCount();

        // ?????????????????????
        if(currentPageNo < 1){
            currentPageNo = 1;
        }else if(currentPageNo > totalPageCount){
            currentPageNo = totalPageCount;
        }


        userList = userService.getUserList(queryUserName,queryUserRole,currentPageNo, pageSize);
        request.setAttribute("userList", userList);
        List<Role> roleList = null;
        RoleService roleService = new RoleServiceImpl();
        roleList = roleService.getRoleList();
        request.setAttribute("roleList", roleList);
        request.setAttribute("queryUserName", queryUserName);
        request.setAttribute("queryUserRole", queryUserRole);
        request.setAttribute("totalPageCount", totalPageCount);
        request.setAttribute("totalCount", totalCount);
        request.setAttribute("currentPageNo", currentPageNo);
        request.getRequestDispatcher("userlist.jsp").forward(request, response);
    }

    // ????????????
    public void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String userCode = request.getParameter("userCode");
        String userName = request.getParameter("userName");
        String userPassword = request.getParameter("userPassword");
        String gender = request.getParameter("gender");
        String birthday = request.getParameter("birthday");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String userRole = request.getParameter("userRole");

        User user = new User();
        user.setUserCode(userCode);
        user.setUserName(userName);
        user.setUserPassword(userPassword);
        user.setAddress(address);
        try {
            user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        user.setGender(Integer.valueOf(gender));
        user.setPhone(phone);
        user.setUserRole(Integer.valueOf(userRole));
        user.setCreationDate(new Date());
        user.setCreatedBy(((User)request.getSession().getAttribute(Constants.USER_SESSION)).getId());

        UserService userService = new UserServiceImpl();
        if(userService.addUser(user)){
            request.getRequestDispatcher("useradd.jsp").forward(request, response);
        }else {
            request.getRequestDispatcher("useradd.jsp").forward(request, response);
        }
    }

    // ??????????????????
    private void getRoleList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Role> roleList = null;
        RoleService roleService = new RoleServiceImpl();
        roleList = roleService.getRoleList();
        // ???roleList?????????json????????????
        response.setContentType("application/json");
        PrintWriter outPrintWriter = response.getWriter();
        outPrintWriter.write(JSONArray.toJSONString(roleList));
        outPrintWriter.flush();
        outPrintWriter.close();
    }

    // ????????????
    public void delUser(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String id = request.getParameter("uid");
        Integer delId = 0;
        try{
            delId = Integer.parseInt(id);
        }catch (Exception e) {
            delId = 0;
        }
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if(delId <= 0){
            resultMap.put("delResult", "notexist");
        }else{
            UserService userService = new UserServiceImpl();
            if(userService.deleteUserById(delId)){
                resultMap.put("delResult", "true");
            }else{
                resultMap.put("delResult", "false");
            }
        }

        // ???resultMap?????????json????????????
        response.setContentType("application/json");
        PrintWriter outPrintWriter = response.getWriter();
        outPrintWriter.write(JSONArray.toJSONString(resultMap));
        outPrintWriter.flush();
        outPrintWriter.close();
    }

    // ??????????????????
    public void modify(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("uid");
        String userName = request.getParameter("userName");
        String gender = request.getParameter("gender");
        String birthday = request.getParameter("birthday");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String userRole = request.getParameter("userRole");

        User user = new User();
        user.setId(Integer.valueOf(id));
        user.setUserName(userName);
        user.setGender(Integer.valueOf(gender));
        try {
            user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        user.setPhone(phone);
        user.setAddress(address);
        user.setUserRole(Integer.valueOf(userRole));
        user.setModifyBy(((User)request.getSession().getAttribute(Constants.USER_SESSION)).getId());
        user.setModifyDate(new Date());

        UserService userService = new UserServiceImpl();
        if(userService.modify(user)){
            response.sendRedirect(request.getContextPath()+"/jsp/user.do?method=query");
        }else{
            request.getRequestDispatcher("usermodify.jsp").forward(request, response);
        }

    }

    // ??????userId??????user
    public void getUserById(HttpServletRequest request, HttpServletResponse response,String url)
            throws ServletException, IOException {

        String id = request.getParameter("uid");
        if(!StringUtils.isNullOrEmpty(id)){
            // ????????????????????????user??????
            UserService userService = new UserServiceImpl();
            User user = userService.getUserById(id);
            request.setAttribute("user", user);
            request.getRequestDispatcher(url).forward(request, response);
        }

    }

    // ??????userCode?????????User
    private void userCodeExist(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // ??????????????????????????????
        String userCode = request.getParameter("userCode");

        HashMap<String, String> resultMap = new HashMap<String, String>();
        if(StringUtils.isNullOrEmpty(userCode)){
            // userCode == null || userCode.equals("")
            resultMap.put("userCode", "exist");
        }else{
            UserService userService = new UserServiceImpl();
            User user = userService.selectUserCodeExist(userCode);
            if(null != user){
                resultMap.put("userCode","exist");
            }else{
                resultMap.put("userCode", "notexist");
            }
        }

        // ???resultMap??????json????????????json???????????????
        // ??????????????????????????????
        response.setContentType("application/json");
        // ???response??????????????????????????????writer??????
        PrintWriter outPrintWriter = response.getWriter();
        // ???resultMap??????json????????? ??????
        outPrintWriter.write(JSONArray.toJSONString(resultMap));
        outPrintWriter.flush(); // ??????
        outPrintWriter.close(); // ?????????
    }
}
