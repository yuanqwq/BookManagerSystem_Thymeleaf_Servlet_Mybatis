package com.BookManagerSystem.operation;

import com.BookManagerSystem.entity.Book;
import com.BookManagerSystem.entity.Record;
import com.BookManagerSystem.entity.User;
import com.BookManagerSystem.mapper.DataBaseMapper;
import com.BookManagerSystem.util.MybatisUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.ibatis.session.SqlSession;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class RecordManage {
    public static List<Record> getRecords(){
        try(SqlSession session= MybatisUtil.getFactory().openSession(true)){
            DataBaseMapper mapper=session.getMapper(DataBaseMapper.class);
            return mapper.getRecords();
        }
    }
    public static List<Record> getRecordsByUid(int uid){
        try(SqlSession session= MybatisUtil.getFactory().openSession(true)){
            DataBaseMapper mapper=session.getMapper(DataBaseMapper.class);
            return mapper.getRecordsByUid(uid);
        }
    }
    public static String manage(HttpServletRequest request){
        Map<String,String[]> map=request.getParameterMap();
        User user= (User) request.getSession().getAttribute("user");
        String res="";
        if(map.containsKey("rid")&&map.containsKey("op")){
            int rid=Integer.parseInt(request.getParameter("rid"));
            String op=request.getParameter("op");
            try(SqlSession session= MybatisUtil.getFactory().openSession(false)){
                DataBaseMapper mapper=session.getMapper(DataBaseMapper.class);
                user=mapper.getUserByUid(user.getUid());
                request.getSession().setAttribute("user",user);
                Record record=mapper.getRecordByRid(rid);
                Book book;
                System.out.println(record);
                if(record==null){
                    res="此记录不存在";
                    session.rollback();
                }else if(op.equals("return")){
                    book=mapper.getBookByBid(record.getBook().getBid());
                    if(book!=null){
                        if(mapper.updateBookNum(book.getBid(),book.getNumber()+1)!=0){
                            if(mapper.deleteRecordByRid(rid)==0){
                                res="记录删除失败";
                                session.rollback();
                            }
                        }else{
                            res="book表更新失败";
                            session.rollback();
                        }
                    }else{
                        res="此书不存在";
                        session.rollback();
                    }
                }else if(op.equals("delete")){
                    if(user.isAdmin()) {
                        if (mapper.deleteRecordByRid(rid) == 0) {
                            res = "记录删除失败";
                            session.rollback();
                        }
                    }else{
                        res="权限不足";
                        session.rollback();
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
