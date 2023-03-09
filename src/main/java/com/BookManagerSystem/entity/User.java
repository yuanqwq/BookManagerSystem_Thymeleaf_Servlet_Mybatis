package com.BookManagerSystem.entity;

import lombok.Data;

@Data
public class User {
    private int uid;
    private String username;
    private String name;
    private String sex;
    private String email;
    private boolean isAdmin;
}
