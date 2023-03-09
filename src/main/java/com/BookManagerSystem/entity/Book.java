package com.BookManagerSystem.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Book {
    private int bid;
    private String name;
    private String author;
    private String publisher;
    private double price;
    private int number;

}
