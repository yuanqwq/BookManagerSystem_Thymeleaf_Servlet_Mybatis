package com.BookManagerSystem.servlet;



import com.BookManagerSystem.entity.User;
import com.BookManagerSystem.operation.Login;
import com.BookManagerSystem.operation.AutoLogin;
import com.BookManagerSystem.operation.RealLogin;
import com.BookManagerSystem.util.ThymeleafUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.thymeleaf.context.Context;

import java.io.IOException;


@WebServlet(name = "LoginServlet", value = "/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Login login=new AutoLogin();
        if(login.check(request,response)!=null){
           User user= (User) request.getSession().getAttribute("user");
           if(user !=null){
               //System.out.println(user);
                response.sendRedirect("index");
           }
           return;
        }
        Context context=new Context();
        context.setVariable("accerr","hidden");
        ThymeleafUtil.getEngine().process("login.html",context, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Login login=new RealLogin();
        if(login.check(request,response)!=null){
            User user= (User) request.getSession().getAttribute("user");
            if(user !=null){
                //System.out.println(user);
                response.sendRedirect("index");

            }
            return;
        }
        Context context=new Context();
        context.setVariable("accerr","visible");
        ThymeleafUtil.getEngine().process("login.html",context, response.getWriter());
    }
}
