package pers.dao;

import pers.Book;
import pers.utils.Base64Util;

import javax.swing.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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

    public static void deleteBookFromArrayById(ArrayList<Book> books, int id) {
        // 查找并删除目标书籍
        boolean removed = books.removeIf(book -> book.getBookId() == id);
        if (removed) {
            System.out.println("书号为 " + id + " 的书籍已成功删除！");
        } else {
            System.out.println("未找到书号为 " + id + " 的书籍！");
        }
    }

    public static void joinBookToArray(ArrayList<Book> books, Book book) {
        books.add(book);
    }

    public static void changeBookBorrowedStatus(ArrayList<Book> booksData, Book selectedBook, int studentId, JFrame frame) {
        if (booksData == null || selectedBook == null || frame == null) {
            JOptionPane.showMessageDialog(frame, "传入的参数不能为空，请检查后重新操作！",
                    "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        PreparedStatement preparedStatement = null;
        try (Connection connection = DBUtil.getConnection()) {
            if (selectedBook.isBorrowed()) {
                // 如果是归还操作，更新数据库中对应图书记录为未借出状态（将is_borrowed设置为0）
                preparedStatement = connection.prepareStatement("UPDATE books SET borrowed_by_id = NULL, is_borrowed = 0 WHERE book_id =?");
                preparedStatement.setInt(1, selectedBook.getBookId());
            } else {
                // 如果是借阅操作，更新数据库中对应图书记录为已借出状态，并设置借阅人ID（将is_borrowed设置为1）
                System.out.println("即将传递给更新语句的borrowed_by_id值: " + selectedBook.getBorrowedById());
                preparedStatement = connection.prepareStatement("UPDATE books SET borrowed_by_id =?, is_borrowed = 1 WHERE book_id =?");
                preparedStatement.setInt(1, studentId);
                preparedStatement.setInt(2, selectedBook.getBookId());
            }
            int rowsAffected = preparedStatement.executeUpdate();
            /*System.out.println("图书借阅/归还状态更新操作执行完毕，影响行数: " + rowsAffected + "，书号为: " + selectedBook.getBookId());*/
            if (rowsAffected == 0) {
                JOptionPane.showMessageDialog(frame, "图书借阅/归还状态更新失败，可能书号对应的记录不存在！",
                        "错误", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "图书借阅/归还状态更新失败: " + e.getMessage(),
                    "错误", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (preparedStatement!= null) {
                try {
                    preparedStatement.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }


    public static void saveBookData(ArrayList<Book> booksData, JFrame frame) {
        try {
            // 加密并写入书籍数据
            Base64Util.writeEncryptedFile(BOOK_FILE, booksData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}