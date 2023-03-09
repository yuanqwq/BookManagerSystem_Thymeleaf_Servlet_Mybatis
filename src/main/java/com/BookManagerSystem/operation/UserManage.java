package com.BookManagerSystem.operation;

import com.BookManagerSystem.entity.*;
import com.BookManagerSystem.mapper.DataBaseMapper;
import com.BookManagerSystem.util.MybatisUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.ibatis.session.SqlSession;

import java.util.List;
import java.util.Map;

public class UserManage {
    public static List<User_p> getUsers_p(){
        try(SqlSession session= MybatisUtil.getFactory().openSession(true)){
            DataBaseMapper mapper=session.getMapper(DataBaseMapper.class);
            return mapper.getUsers_p();
        }
    }

    public static String manage(HttpServletRequest request){
        Map<String,String[]> map=request.getParameterMap();
        User curuser= (User) request.getSession().getAttribute("user");
        String res="";
        if(map.containsKey("uid")&&map.containsKey("op")){
            int uid=Integer.parseInt(request.getParameter("uid"));
            String op=request.getParameter("op");
            try(SqlSession session= MybatisUtil.getFactory().openSession(false)){
                DataBaseMapper mapper=session.getMapper(DataBaseMapper.class);
                curuser=mapper.getUserByUid(curuser.getUid());
                request.getSession().setAttribute("user",curuser);
                User user=mapper.getUserByUid(uid);
                if(user==null){
                    res="此用户不存在";
                    session.rollback();
                }else if(op.equals("admin")){
                    if(curuser.isAdmin()) {
                        if (mapper.updateUserAdmin(uid, !user.isAdmin()) == 0) {
                            res = "user表更新失败";
                            session.rollback();
                        }
                    }else{
                        res="权限不足";
                        session.rollback();
                    }
                }else if(op.equals("delete")){
                    Account acc=mapper.getAccountByUid(uid);
                    if(acc==null){
                        res="此账号不存在";
                        session.rollback();
                    }else {
                        if (curuser.isAdmin()) {
                            if (mapper.deleteUAByUid(uid)!=0) {
                                if (mapper.deleteUserByUid(uid) != 0) {
                                    if(mapper.deleteAccountByAcc(acc.getAccount())==0){
                                        res = "账号删除失败";
                                        session.rollback();
                                    }
                                }else{
                                    res = "用户删除失败";
                                    session.rollback();
                                }
                            }else{
                                res = "记录删除失败";
                                session.rollback();
                            }
                        } else {
                            res = "权限不足";
                            session.rollback();
                        }
                    }
                }
                session.commit();
            }

        }else{
            res="参数异常";
        }
        return  res;
    }
}
