package pers;

import java.io.Serializable;
/*
   序列化接口：
   当你想把的内存中的对象状态保存到一个文件中或者数据库中，以便可以在以后重新创建精确的副本
 */

/**
 * @author XPIPI
 */
public class Book implements Serializable {
    // 重写toString
    @Override
    public String toString() {
        return "书号: " + bookId +
                ", 书名: " + title +
                ", 作者: " + author +
                ", 类别: " + type +
                ", 状态: " + (isBorrowed ? "已借出" : "未借出");
    }

    // 书号 int
    private int bookId;

    // 书名 String
    private String title;

    // 作者 String
    private String author;

    // 类别 String
    private String type;

    // 是否被借出 boolean
    // True->被借出   False->未被借出
    private boolean isBorrowed;

    // 初始化构造函数
    public Book(int bookId, String title, String author, String type) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.type = type;
        // 默认状态为 False->未被借出
        this.isBorrowed = false;
    }

    // 返回书的属性的函数 以及修改书的属性的函数

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isBorrowed() {
        return isBorrowed;
    }

    public void setBorrowed(boolean nowStatus) {
        this.isBorrowed = nowStatus;
    }
}
