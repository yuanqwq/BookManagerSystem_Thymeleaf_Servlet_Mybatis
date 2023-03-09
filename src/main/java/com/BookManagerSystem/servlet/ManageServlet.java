package com.BookManagerSystem.servlet;

import com.BookManagerSystem.entity.User;
import com.BookManagerSystem.operation.BookManage;
import com.BookManagerSystem.util.ThymeleafUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.thymeleaf.context.Context;

import java.io.IOException;

@WebServlet(name = "ManageServlet", value = "/manage")
public class ManageServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user= (User) request.getSession().getAttribute("user");
        Context context=new Context();
        context.setVariable("username",user.getUsername());
        context.setVariable("uid",user.getUid());
        ThymeleafUtil.getEngine().process("manage.html",context, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String notice= BookManage.add(request);
        User user= (User) request.getSession().getAttribute("user");
        Context context=new Context();
        context.setVariable("username",user.getUsername());
        context.setVariable("uid",user.getUid());
        ThymeleafUtil.getEngine().process("manage.html",context, response.getWriter());
    }
}
