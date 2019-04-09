package com.javalearn.xml.entity;

import lombok.Data;

@Data
public class Book {

    private int numberOfPages;
    private String name;
    private String publisher;
    private Author author;
}
