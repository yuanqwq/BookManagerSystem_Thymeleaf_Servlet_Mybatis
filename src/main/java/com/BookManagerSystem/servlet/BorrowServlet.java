package com.BookManagerSystem.servlet;

import com.BookManagerSystem.entity.Book;
import com.BookManagerSystem.entity.User;
import com.BookManagerSystem.operation.BookManage;
import com.BookManagerSystem.util.ThymeleafUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "BorrowServlet", value = "/borrow")
public class BorrowServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Book> list= BookManage.getBooks();
        User user= (User) request.getSession().getAttribute("user");
        Context context=new Context();
        context.setVariable("books",list);
        context.setVariable("admin",user.isAdmin());
        context.setVariable("hasMsg","hidden");
        context.setVariable("username",user.getUsername());
        context.setVariable("uid",user.getUid());
        ThymeleafUtil.getEngine().process("borrow.html",context, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String notice= BookManage.manage(request);
        List<Book> list= BookManage.getBooks();
        User user= (User) request.getSession().getAttribute("user");
        Context context=new Context();
        context.setVariable("hasMsg","visible");
        context.setVariable("message",notice);
        context.setVariable("books",list);
        context.setVariable("admin",user.isAdmin());
        context.setVariable("username",user.getUsername());
        context.setVariable("uid",user.getUid());
        ThymeleafUtil.getEngine().process("borrow.html",context,response.getWriter());
    }
}
