package com.BookManagerSystem.operation;

import com.BookManagerSystem.entity.Book;
import com.BookManagerSystem.entity.User;
import com.BookManagerSystem.mapper.DataBaseMapper;
import com.BookManagerSystem.util.MybatisUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.ibatis.session.SqlSession;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class BookManage {
    public static String manage(HttpServletRequest request){
        Map<String,String[]> map=request.getParameterMap();
        User user= (User) request.getSession().getAttribute("user");
        String res="";
        if(map.containsKey("bid")&&map.containsKey("op")){
            int bid=Integer.parseInt(request.getParameter("bid"));
            String op=request.getParameter("op");
            try(SqlSession session= MybatisUtil.getFactory().openSession(false)){
                DataBaseMapper mapper=session.getMapper(DataBaseMapper.class);
                user=mapper.getUserByUid(user.getUid());
                request.getSession().setAttribute("user",user);
                Book book=mapper.getBookByBid(bid);
                if(book==null){
                    res="此书不存在";
                    session.rollback();
                }else if(op.equals("borrow")){
                    if(book.getNumber()>=1){
                        if(mapper.updateBookNum(bid,book.getNumber()-1)!=0){
                            SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
                            if(mapper.addRecord(user,book,format.format(new Date()))==0){
                                res="记录插入失败";
                                session.rollback();
                            }
                        }else{
                            res="book表更新失败";
                            session.rollback();
                        }
                    }else{
                        res="余量不足";
                        session.rollback();
                    }
                }else if(op.equals("edit")){
                    if(user.isAdmin()){
                        if(map.containsKey("num")){
                            if(mapper.updateBookNum(bid,Integer.parseInt(request.getParameter("num")))==0){
                                res="book表更新失败";
                                session.rollback();
                            }
                        }else{
                            res="数量为空";
                        }
                    }else {
                        res="权限不足";
                        session.rollback();
                    }
                }else if(op.equals("delete")){
                    if(user.isAdmin()) {
                        if (mapper.deleteRecordByBid(bid) == 0&&mapper.getRecordsByBid(bid).size()!=0) {
                            res = "删除失败";
                            session.rollback();
                        } else if (mapper.deleteBookByBid(bid) == 0) {
                            res = "删除失败";
                            session.rollback();
                        }
                    }else {
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

    public static List<Book> getBooks(){
        try(SqlSession session= MybatisUtil.getFactory().openSession(true)){
            DataBaseMapper mapper=session.getMapper(DataBaseMapper.class);
            return mapper.getBooks();
        }
    }

    public static String add(HttpServletRequest request){
        Map<String,String[]> map=request.getParameterMap();
        String res="";
        Book book=new Book();
        int bid,number=0;
        double price=0;
        String name="",author="",publisher="";
        if(map.containsKey("bid")&&map.containsKey("name")){
            try {
                bid = Integer.parseInt(request.getParameter("bid"));
                name=request.getParameter("name");

                if(map.containsKey("author")){
                    author=request.getParameter("author");
                }
                if(map.containsKey("publisher")){
                    publisher=request.getParameter("publisher");
                }
                if(map.containsKey("price")){
                    price=Double.parseDouble(request.getParameter("price"));
                }
                if(map.containsKey("number")){
                    number=Integer.parseInt(request.getParameter("number"));
                }
            }catch (NumberFormatException e){
                e.printStackTrace();
                res="格式异常";
                return res;
            }
            book.setBid(bid).setName(name).setPublisher(publisher).setPrice(price).setAuthor(author).setNumber(number);
            System.out.println(book);
            try(SqlSession session=MybatisUtil.getFactory().openSession(false)){
                DataBaseMapper mapper=session.getMapper(DataBaseMapper.class);
                if(mapper.addBook(book)==0){
                    res="书籍插入错误";
                    session.rollback();
                }
                session.commit();
            }
        }else{
            res="参数异常";
        }
        return res;
    }
}
