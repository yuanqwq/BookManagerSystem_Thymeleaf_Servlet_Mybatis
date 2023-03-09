package com.BookManagerSystem.operation;

import com.BookManagerSystem.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface Login {
    public User check(HttpServletRequest req, HttpServletResponse res);
}
