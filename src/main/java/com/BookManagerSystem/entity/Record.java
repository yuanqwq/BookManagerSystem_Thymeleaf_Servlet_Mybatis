package com.BookManagerSystem.entity;

import lombok.Data;

@Data
public class Record {
    private int rid;
    private User user;
    private Book book;
    private String date;
}
