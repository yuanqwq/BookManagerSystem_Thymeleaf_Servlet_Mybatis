package com.BookManagerSystem.servlet;

import com.BookManagerSystem.entity.User;
import com.BookManagerSystem.operation.AutoLogin;
import com.BookManagerSystem.operation.Login;
import com.BookManagerSystem.operation.RealLogin;
import com.BookManagerSystem.util.ThymeleafUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.context.Context;

import java.io.IOException;


@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cookie[] cookies=request.getCookies();
        String account="";
        String password="";
        for(Cookie cookie:cookies){
            if(cookie.getName().equals("account"))
                account=cookie.getValue();
            if(cookie.getName().equals("password"))
                password=cookie.getValue();
        }
        Cookie cookie_account=new Cookie("account",account);
        Cookie cookie_password=new Cookie("password",password);
        cookie_account.setMaxAge(0);
        cookie_password.setMaxAge(0);
        response.addCookie(cookie_account);
        response.addCookie(cookie_password);
        response.sendRedirect("login");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
