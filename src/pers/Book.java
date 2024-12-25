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
        return bookId +
                "|《 " + title +
                "》-- " + author;
    }

    // 书号 bookId
    private int bookId;

    // 书名 title
    private String title;

    // 作者 author
    private String author;

    // 类别 bookType
    private String bookType;

    // 是否被借出 isBorrowed
    // True->被借出   False->未被借出
    private boolean isBorrowed;

    // 借阅人ID borrowedById
    private int borrowedById;

    // 初始化构造函数
    public Book(int bookId, String title, String author, String type) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.bookType = type;
        // 默认状态为 借出状态->False 借阅人->0
        this.isBorrowed = false;
        this.borrowedById = 0;
    }

    // 返回书的属性的函数 以及修改书的属性的函数

    public int getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getBookType() {
        return bookType;
    }

    public boolean isBorrowed() {
        return isBorrowed;
    }

    public void setBorrowed(boolean nowStatus) {
        this.isBorrowed = nowStatus;
    }

    public int getBorrowedById() {
        return borrowedById;
    }

    public void setBorrowedById(int borrowedById) {
        this.borrowedById = borrowedById;
    }
}
