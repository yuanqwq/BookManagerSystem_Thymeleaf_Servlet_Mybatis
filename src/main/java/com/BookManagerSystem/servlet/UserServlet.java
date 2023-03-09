package com.BookManagerSystem.servlet;

import com.BookManagerSystem.entity.Record;
import com.BookManagerSystem.entity.User;
import com.BookManagerSystem.entity.User_p;
import com.BookManagerSystem.operation.RecordManage;
import com.BookManagerSystem.operation.UserManage;
import com.BookManagerSystem.util.ThymeleafUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "UserServlet", value = "/user")
public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<User_p> list;
        User user= (User) request.getSession().getAttribute("user");
        list= UserManage.getUsers_p();
        System.out.println(list);
        Context context=new Context();
        context.setVariable("users_p",list);
        context.setVariable("admin",user.isAdmin());
        context.setVariable("hasMsg","hidden");
        context.setVariable("username",user.getUsername());
        context.setVariable("uid",user.getUid());
        ThymeleafUtil.getEngine().process("user.html",context, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String notice= UserManage.manage(request);
        List<User_p> list;
        User user= (User) request.getSession().getAttribute("user");

        list= UserManage.getUsers_p();
        System.out.println(list);
        Context context=new Context();
        context.setVariable("hasMsg","visible");
        context.setVariable("message",notice);
        context.setVariable("users_p",list);
        context.setVariable("admin",user.isAdmin());
        context.setVariable("username",user.getUsername());
        context.setVariable("uid",user.getUid());
        ThymeleafUtil.getEngine().process("user.html",context,response.getWriter());
    }
}
