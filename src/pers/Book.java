package pers;

import java.io.Serializable;

/**
 * @author XPIPI
 */
// 书籍类，实现Serializable接口以支持序列化
public class Book implements Serializable {
    // 书号
    private int bookId;

    // 书名
    private String title;

    // 作者
    private String author;

    // 类别
    private String bookType;

    // 是否被借出
    // true -> 被借出, false -> 未被借出
    private boolean isBorrowed;

    // 借阅人ID
    private int borrowedById;

    /**
     * 初始化构造函数
     *
     * @param bookId   书号
     * @param title    书名
     * @param author   作者
     * @param bookType 类别
     */
    public Book(int bookId, String title, String author, String bookType) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.bookType = bookType;
        // 默认状态为未借出，借阅人ID为0
        this.isBorrowed = false;
        this.borrowedById = 0;
    }

    // 重写toString方法
    @Override
    public String toString() {
        return bookId +
                "|《 " + title +
                "》-- " + author;
    }

    // 返回书的属性的函数 以及修改书的属性的函数

    /**
     * 获取书号
     *
     * @return 书号
     */
    public int getBookId() {
        return bookId;
    }

    /**
     * 设置书号
     *
     * @param bookId 书号
     */
    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    /**
     * 获取书名
     *
     * @return 书名
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置书名
     *
     * @param title 书名
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取作者
     *
     * @return 作者
     */
    public String getAuthor() {
        return author;
    }

    /**
     * 设置作者
     *
     * @param author 作者
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * 获取类别
     *
     * @return 类别
     */
    public String getBookType() {
        return bookType;
    }

    /**
     * 设置类别
     *
     * @param bookType 类别
     */
    public void setBookType(String bookType) {
        this.bookType = bookType;
    }

    /**
     * 获取借出状态
     *
     * @return 是否被借出
     */
    public boolean isBorrowed() {
        return isBorrowed;
    }

    /**
     * 设置借出状态
     *
     * @param nowStatus 借出状态
     */
    public void setBorrowed(boolean nowStatus) {
        this.isBorrowed = nowStatus;
    }

    /**
     * 获取借阅人ID
     *
     * @return 借阅人ID
     */
    public int getBorrowedById() {
        return borrowedById;
    }

    /**
     * 设置借阅人ID
     *
     * @param borrowedById 借阅人ID
     */
    public void setBorrowedById(int borrowedById) {
        this.borrowedById = borrowedById;
    }
}
