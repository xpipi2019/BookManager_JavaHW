package pers.dao;

import pers.Book;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BookOperate {
    // 书籍数据库文件 BOOK_FILE
    static final String BOOK_FILE = "books.dat";


    public static void readBookData(ArrayList<Book> booksData, JFrame frame) {
        booksData.clear(); // 清空原有数据
        String sql = "SELECT book_id, title, author, book_type, borrowed_by_id, is_borrowed FROM books";
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                int bookId = resultSet.getInt("book_id");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                String bookType = resultSet.getString("book_type");
                int borrowedById = resultSet.getInt("borrowed_by_id");
                boolean isBorrowed = resultSet.getBoolean("is_borrowed");
                Book book = new Book(bookId, title, author, bookType);
                book.setBorrowed(isBorrowed);
                book.setBorrowedById(borrowedById);
                booksData.add(book);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "图书数据从数据库读取失败: " + e.getMessage(),
                    "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace(); // 打印堆栈信息以便调试
        }
    }

    /*public static void deleteBookFromArrayById(ArrayList<Book> books,int id) {
        // 查找并删除目标书籍
        boolean removed = books.removeIf(book -> book.getBookId() == id);
        if (removed) {
            System.out.println("书号为 " + id + " 的书籍已成功删除！");
        } else {
            System.out.println("未找到书号为 " + id + " 的书籍！");
        }
    }*/

 /*   public static void joinBookToArray(ArrayList<Book> books,Book book) {
        books.add(book);
    }
*/
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
             /*System.out.println("即将传递给更新语句的borrowed_by_id值: " + selectedBook.getBorrowedById());*/
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

    /*public static void saveBookData(ArrayList<Book> booksData, JFrame frame) {
        try {
            // 加密并写入书籍数据
            Base64Util.writeEncryptedFile(BOOK_FILE,booksData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }*/
}
