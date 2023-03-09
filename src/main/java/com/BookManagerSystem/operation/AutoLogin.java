package com.BookManagerSystem.operation;

import com.BookManagerSystem.entity.Account;
import com.BookManagerSystem.entity.User;
import com.BookManagerSystem.mapper.DataBaseMapper;
import com.BookManagerSystem.util.MybatisUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.ibatis.session.SqlSession;

public class AutoLogin implements Login{
    @Override
    public User check(HttpServletRequest req,HttpServletResponse res) {
        Cookie[] cookies=req.getCookies();
        String account="";
        String password="";
        for(Cookie cookie:cookies){
            if(cookie.getName().equals("account"))
                account=cookie.getValue();
            if(cookie.getName().equals("password"))
                password=cookie.getValue();
        }
        try(SqlSession session= MybatisUtil.getFactory().openSession(true)){
            DataBaseMapper mapper=session.getMapper(DataBaseMapper.class);
            Account acc=mapper.loginCheck(account,password);
            if(acc==null){
                Cookie cookie_account=new Cookie("account",account);
                Cookie cookie_password=new Cookie("password",password);
                cookie_account.setMaxAge(0);
                cookie_password.setMaxAge(0);
                res.addCookie(cookie_account);
                res.addCookie(cookie_password);
                return null;
            }else{
                HttpSession httpSession=req.getSession();
                User user=mapper.getUserByAccount(acc);
                httpSession.setAttribute("user",user);
                return user;
            }
        }
    }
}
