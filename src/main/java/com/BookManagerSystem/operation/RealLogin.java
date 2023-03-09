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

import java.util.Map;

public class RealLogin implements Login{
    @Override
    public User check(HttpServletRequest req,HttpServletResponse res) {
        Map<String,String[]> map=req.getParameterMap();
        if(map.containsKey("account")&&map.containsKey("password")){
            String account=req.getParameter("account");
            String password=req.getParameter("password");
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
                    System.out.println(account);
                    System.out.println(password);
                    return null;
                }else{
                    if(map.containsKey("remember")){
                        Cookie cookie_account=new Cookie("account",account);
                        Cookie cookie_password=new Cookie("password",password);
                        cookie_account.setMaxAge(180);
                        cookie_password.setMaxAge(180);
                        res.addCookie(cookie_account);
                        res.addCookie(cookie_password);
                    }
                    HttpSession httpSession=req.getSession();
                    User user=mapper.getUserByAccount(acc);
                    httpSession.setAttribute("user",user);
                    return user;
                }
            }
        }
        return null;
    }

}
