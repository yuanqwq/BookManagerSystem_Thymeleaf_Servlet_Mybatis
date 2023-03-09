package com.BookManagerSystem.servlet;

import com.BookManagerSystem.entity.Book;
import com.BookManagerSystem.entity.Record;
import com.BookManagerSystem.entity.User;
import com.BookManagerSystem.operation.BookManage;
import com.BookManagerSystem.operation.RecordManage;
import com.BookManagerSystem.util.ThymeleafUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "RecordServlet", value = "/record")
public class RecordServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Record> list;
        User user= (User) request.getSession().getAttribute("user");
        if(user.isAdmin())
            list= RecordManage.getRecords();
        else
            list= RecordManage.getRecordsByUid(user.getUid());
        //System.out.println(list);
        Context context=new Context();
        context.setVariable("records",list);
        context.setVariable("admin",user.isAdmin());
        context.setVariable("hasMsg","hidden");
        context.setVariable("username",user.getUsername());
        context.setVariable("uid",user.getUid());
        ThymeleafUtil.getEngine().process("record.html",context, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String notice= RecordManage.manage(request);
        List<Record> list;
        User user= (User) request.getSession().getAttribute("user");
        if(user.isAdmin())
            list= RecordManage.getRecords();
        else
            list= RecordManage.getRecordsByUid(user.getUid());
        //System.out.println(list);
        Context context=new Context();
        context.setVariable("hasMsg","visible");
        context.setVariable("message",notice);
        context.setVariable("records",list);
        context.setVariable("admin",user.isAdmin());
        context.setVariable("username",user.getUsername());
        context.setVariable("uid",user.getUid());
        ThymeleafUtil.getEngine().process("record.html",context,response.getWriter());
    }
}
