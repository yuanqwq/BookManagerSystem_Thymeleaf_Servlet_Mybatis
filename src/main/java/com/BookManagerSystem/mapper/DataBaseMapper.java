package com.BookManagerSystem.mapper;


import com.BookManagerSystem.entity.*;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface DataBaseMapper {

    @Select("select * from account where account=#{account} and password=#{password}")
    Account loginCheck(@Param("account")String account, @Param("password")String password);

    @Results({
            @Result(id = true,column = "uid",property = "uid"),
            @Result(column = "username",property = "username"),
            @Result(column = "name",property = "name"),
            @Result(column = "sex",property = "sex"),
            @Result(column = "email",property = "email"),
            @Result(column = "isAdmin",property = "isAdmin")
    })
    @Select("select * from account acc inner join user_to_account ua on acc.account=ua.account " +
            "inner join user on ua.uid=user.uid " +
            "where acc.account=#{account.account} and acc.password=#{account.password}")
    User getUserByAccount(@Param("account") Account account);

    @Select("select * from book")
    List<Book> getBooks();

    @Select("select * from book where bid=#{bid}")
    Book getBookByBid(int bid);

    @Update("update book set number=#{num} where bid=#{bid}")
    int updateBookNum(@Param("bid") int bid,@Param("num") int num);

    @Insert("insert into record(uid,bid,date) values(#{User.uid},#{Book.bid},#{Date})")
    int addRecord(@Param("User") User user,@Param("Book") Book book,@Param("Date") String date);

    @Delete("delete from book where bid=#{bid}")
    int deleteBookByBid(int bid);

    @Delete("delete from record where bid=#{bid}")
    int deleteRecordByBid(int bid);

    @Results({
            @Result(id = true,column = "rid",property = "rid"),
            @Result(column = "date",property = "date"),
            @Result(column = "uid",property = "user",one =
            @One(select = "getUserByUid")
            ),
            @Result(column = "bid",property = "book",one =
            @One(select = "getBookByBid")
            )
    })
    @Select("select * from record")
    List<Record> getRecords();


    @Results({
            @Result(id = true,column = "rid",property = "rid"),
            @Result(column = "date",property = "date"),
            @Result(column = "uid",property = "user",one =
            @One(select = "getUserByUid")
            ),
            @Result(column = "bid",property = "book",one =
            @One(select = "getBookByBid")
            )
    })
    @Select("select * from record where uid=#{uid}")
    List<Record> getRecordsByUid(int uid);


    @Results({
            @Result(id = true,column = "rid",property = "rid"),
            @Result(column = "date",property = "date"),
            @Result(column = "uid",property = "user",one =
                @One(select = "getUserByUid")
            ),
            @Result(column = "bid",property = "book",one =
                @One(select = "getBookByBid")
            )
    })
    @Select("select * from user inner join record on user.uid=record.uid inner join book on record.bid=book.bid " +
            "where rid=#{rid}")
    Record getRecordByRid(int rid);

    @Delete("delete from record where rid=#{rid}")
    int deleteRecordByRid(int rid);

    @Select("select * from user where uid=#{uid}")
    User getUserByUid(int uid);

    @Select("select * from user")
    List<User> getUsers();

    @Update("update user set isAdmin=#{admin} where user.uid=#{uid}")
    int updateUserAdmin(@Param("uid") int uid,@Param("admin") boolean admin);

    @Delete("delete from user where user.uid=#{uid}")
    int deleteUserByUid(int uid);

    @Select("select * from account where account.account=#{acc}")
    Account getAccountByAcc(String acc);

    @Results({
            @Result(column = "uid",property = "user",one =
            @One(select = "getUserByUid")
            ),
            @Result(column = "account",property = "account",one =
            @One(select = "getAccountByAcc")
            )
    })
    @Select("select * from user inner join user_to_account ua on user.uid=ua.uid " +
            "inner join account acc on ua.account=acc.account ")
    List<User_p> getUsers_p();


    @Delete("delete from user_to_account where uid=#{uid}")
    int deleteUAByUid(int uid);

    @Delete("delete from account where account.account=#{acc}")
    int deleteAccountByAcc(String acc);

    @Select("select * from account inner join user_to_account ua on account.account=ua.account where ua.uid=#{uid}")
    Account getAccountByUid(int uid);

    @Insert("insert into book values(#{book.bid},#{book.name},#{book.author},#{book.publisher},#{book.price},#{book.number})")
    int addBook(@Param("book") Book book);

    @Select("select * from record where bid=#{bid}")
    List<Record> getRecordsByBid(int bid);

}
