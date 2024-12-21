package pers.util;

import pers.Book;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;

public class BookOperate {
    // 书籍数据库文件 BOOK_FILE
    static final String BOOK_FILE = "books.dat";

    public static void readBookData(ArrayList<Book> booksData, JFrame frame) {
        ArrayList<Book> books = new ArrayList<>();

        // 解密并读取书籍数据
        try {
            books = Base64Util.readDecryptedFile(BOOK_FILE);
        } catch (IOException | ClassNotFoundException e) {
            // 出现读取异常 或 反序列化失败 的错误
            JOptionPane.showMessageDialog(frame, "无法打开图书数据文件！", "错误", JOptionPane.WARNING_MESSAGE);
        }

        for (Book book : books) {
            booksData.add(book);
        }
    }

    public static void deleteBookFromArrayById(ArrayList<Book> books,int id) {
        // 查找并删除目标书籍
        boolean removed = books.removeIf(book -> book.getBookId() == id);
        if (removed) {
            System.out.println("书号为 " + id + " 的书籍已成功删除！");
        } else {
            System.out.println("未找到书号为 " + id + " 的书籍！");
        }
    }

    public static void joinBookToArray(ArrayList<Book> books,Book book) {
        books.add(book);
    }

    public static void changeBookBorrowedStatus(ArrayList<Book> books,Book book, JFrame frame) {
        deleteBookFromArrayById(books,book.getBookId());
        book.setBorrowed(!book.isBorrowed());
        joinBookToArray(books,book);
        saveBookData(books,frame);
    }

    public static void saveBookData(ArrayList<Book> booksData, JFrame frame) {
        try {
            // 加密并写入书籍数据
            Base64Util.writeEncryptedFile(BOOK_FILE,booksData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
