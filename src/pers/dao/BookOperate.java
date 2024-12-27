package pers.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.Book;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BookOperate {
    private static final Logger logger = LoggerFactory.getLogger(BookOperate.class);

    /**
     * 从数据库读取图书数据并填充到 booksData 列表中
     *
     * @param booksData 存储图书数据的 ArrayList
     * @param frame     父窗口，用于显示错误信息
     */
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
            logger.info("Successfully read book data from the database.");
        } catch (SQLException e) {
            logger.error("Failed to read book data from the database: " + e.getMessage(), e);
            JOptionPane.showMessageDialog(frame, "图书数据从数据库读取失败: " + e.getMessage(),
                    "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace(); // 打印堆栈信息以便调试
        }
    }

    /**
     * 更改图书的借阅状态
     *
     * @param booksData    存储图书数据的 ArrayList
     * @param selectedBook 要更改借阅状态的图书对象
     * @param studentId    借阅人的ID，如果是归还操作则为0
     * @param frame        父窗口，用于显示错误信息
     */
    public static void changeBookBorrowedStatus(ArrayList<Book> booksData, Book selectedBook, int studentId, JFrame frame) {
        // 检查传入的参数是否为空
        if (booksData == null || selectedBook == null || frame == null) {
            logger.error("传入的参数不能为空，请检查后重新操作！");
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
                preparedStatement = connection.prepareStatement("UPDATE books SET borrowed_by_id =?, is_borrowed = 1 WHERE book_id =?");
                preparedStatement.setInt(1, studentId);
                preparedStatement.setInt(2, selectedBook.getBookId());
            }
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                logger.error("图书借阅/归还状态更新失败，可能书号对应的记录不存在！");
                JOptionPane.showMessageDialog(frame, "图书借阅/归还状态更新失败，可能书号对应的记录不存在！",
                        "错误", JOptionPane.ERROR_MESSAGE);
            } else {
                logger.info("Successfully updated book borrow status for book ID: {}", selectedBook.getBookId());
            }
        } catch (SQLException e) {
            logger.error("图书借阅/归还状态更新失败: " + e.getMessage(), e);
            JOptionPane.showMessageDialog(frame, "图书借阅/归还状态更新失败: " + e.getMessage(),
                    "错误", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException ex) {
                    logger.error("Failed to close PreparedStatement: " + ex.getMessage(), ex);
                    ex.printStackTrace();
                }
            }
        }
    }
}
    