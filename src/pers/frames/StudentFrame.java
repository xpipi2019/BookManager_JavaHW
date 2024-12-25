package pers.frames;

import pers.Book;
import pers.roles.Student;
import pers.dao.BookOperate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import pers.dao.DBUtil;

/**
 * @author XPIPI
 */
// 界面二：学生使用界面 StudentFrame
public class StudentFrame extends JFrame {
    // 学生数据 student
    private Student student;
    // JList对象 bookList
    private JList<Book> bookList;
    // 用一个数组来临时存储读取文件中的图书数据 booksData
    ArrayList<Book> booksData = new ArrayList<>();
    // 最大可借书数量 MAX_BORROW_LIMIT
    private final int MAX_BORROW_LIMIT = 3;
    // 图书信息文本域 bookInfoArea
    private JTextArea bookInfoArea;

    // 窗体初始化
    public StudentFrame(Student student) {
        this.student = student;

        // 设置窗口信息
        setTitle("图书借阅系统--学生");
        setLayout(new BorderLayout());

        // 窗体退出事件 登陆后退出：正常退出，返回0
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        // 顶部显示学生信息 topPanel
        JPanel topPanel = new JPanel(new BorderLayout());

        // 学生信息标签 studentInfoLabel
        JLabel studentInfoLabel = new JLabel(
                "姓名: " + student.getName() + " | 学号: " + student.getId() + " | 性别: " + student.getGender(),
                JLabel.CENTER);

        // topPanel -> studentInfoLabel 居中
        topPanel.add(studentInfoLabel, BorderLayout.CENTER);


        // 中间书籍选择和详细信息 middlePanel
        JPanel middlePanel = new JPanel(new GridLayout(1, 2, 10, 10));

        // 创建管理列表数据模型 bookListModel
        DefaultListModel<Book> bookListModel = new DefaultListModel<>();

        // 左侧：带滚动条的书籍列表 bookScrollPane
        // 构造一个JList从指定显示元素从非空的model bookList
        bookList = new JList<>(bookListModel);
        // 设置Jlist选择模式：单选
        bookList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // 设置Jlist事件监听器：选中时触发
        bookList.addListSelectionListener(e -> updateBookInfo());
        // 创建JScrollPane滚动条，bookList是组件对象 bookScrollPane
        JScrollPane bookScrollPane = new JScrollPane(bookList);
        // topPanel -> bookScrollPane
        middlePanel.add(bookScrollPane);

        // 右侧：选中书籍的详细信息（不可编辑）
        // 创建一个用于显示图书信息的文本域 bookInfoArea
        bookInfoArea = new JTextArea();
        // 设置JTextArea编辑模式：不可编辑
        bookInfoArea.setEditable(false);
        // 创建JScrollPane滚动条，bookInfoArea是组件对象 infoScrollPane
        JScrollPane infoScrollPane = new JScrollPane(bookInfoArea);
        // topPanel -> infoScrollPane
        middlePanel.add(infoScrollPane);


        // 底部按钮 bottomPanel
        JPanel bottomPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        // 刷新书籍按钮 refreshButton
        JButton refreshButton = new JButton("刷新书籍");
        // 借阅书籍按钮 borrowButton
        JButton borrowButton = new JButton("借阅书籍");
        // 归还书籍按钮 returnButton
        JButton returnButton = new JButton("归还书籍");
        // 退出程序按钮 exitButton
        JButton exitButton = new JButton("退出程序");

        // bottomPanel -> refreshButton
        bottomPanel.add(refreshButton);
        // bottomPanel -> borrowButton
        bottomPanel.add(borrowButton);
        // bottomPanel -> returnButton
        bottomPanel.add(returnButton);
        // bottomPanel -> exitButton
        bottomPanel.add(exitButton);

        // 添加组件到窗口
        setLayout(new BorderLayout(10, 10));
        // StudentFrame -> topPanel 布局位置:NORTH
        add(topPanel, BorderLayout.NORTH);
        // StudentFrame -> middlePanel 布局位置:CENTER
        add(middlePanel, BorderLayout.CENTER);
        // StudentFrame -> bottomPanel 布局位置:SOUTH
        add(bottomPanel, BorderLayout.SOUTH);

        // 按钮功能实现
        refreshButton.addActionListener(e -> refreshBooks(bookListModel));
        borrowButton.addActionListener(e -> borrowBook(bookListModel));
        returnButton.addActionListener(e -> returnBook(bookListModel));
        exitButton.addActionListener(e -> dispose());

        // 设置窗口属性
        setSize(600, 400);
        setLocationRelativeTo(null);
        setVisible(true);

        // 初始刷新书籍列表
        refreshBooks(bookListModel);
    }

    private void refreshBooks(DefaultListModel<Book> bookListModel) {
        bookListModel.clear();
        booksData.clear();
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT book_id, title, author, book_type, borrowed_by_id, is_borrowed FROM books");
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                int bookId = resultSet.getInt("book_id");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                String bookType = resultSet.getString("book_type");
                int borrowedById = resultSet.getInt("borrowed_by_id");
                /*System.out.println("借阅人：" + borrowedById);*/
                boolean isBorrowed = resultSet.getBoolean("is_borrowed");
                Book book = new Book(bookId, title, author, bookType);
                book.setBorrowed(isBorrowed);
                book.setBorrowedById(borrowedById);
                bookListModel.addElement(book);
                booksData.add(book);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "图书数据从数据库读取失败: " + e.getMessage(),
                    "错误", JOptionPane.ERROR_MESSAGE);
        }
        setStudentBorrowCount(student);
    }

    public void setStudentBorrowCount(Student student) {
        int Count = 0;
        for(Book book : booksData) {
            if(book.getBorrowedById() == student.getId()) {
                Count++;
            }
        }
        student.setBorrowedBooksCount(Count);
    }

    private void updateBookInfo() {
        // 用选中的图书来创建对象 selectedBook
        Book selectedBook = bookList.getSelectedValue();
        if (selectedBook != null) {
            // 通过借阅人ID号来找到借阅人姓名
            String whoBorrowedName;
            if (selectedBook.getBorrowedById() == 0) {
                whoBorrowedName = "未借出（无借阅人）";
            } else {
                whoBorrowedName = student.getNameById(selectedBook.getBorrowedById());
            }
            bookInfoArea.setText(
                    "书号: " + selectedBook.getBookId() + "\n" +
                    "书名: " + selectedBook.getTitle() + "\n" +
                    "作者: " + selectedBook.getAuthor() + "\n" +
                    "类别: " + selectedBook.getBookType() + "\n" +
                    "状态: " + (selectedBook.isBorrowed() ? "已借出" : "未借出") + "\n" +
                    "借阅人：" + whoBorrowedName

            );
        } else { // 未选中时置空
            bookInfoArea.setText("");
        }
    }

    private void borrowBook(DefaultListModel<Book> bookListModel) {
        Book selectedBook = bookList.getSelectedValue();
        // 用选中的图书来创建对象 selectedBook
        // 未选中时弹出提示
        if (selectedBook == null) {
            JOptionPane.showMessageDialog(this, "请选择一本书！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 判断选中的书是否可借出
        if (selectedBook.isBorrowed()) {
            JOptionPane.showMessageDialog(this, "这本书已经被借出！", "提示", JOptionPane.WARNING_MESSAGE);
        } else if(student.getBorrowedBooksCount() >= MAX_BORROW_LIMIT){
            JOptionPane.showMessageDialog(this, "您最多同时借三本书！", "提示", JOptionPane.WARNING_MESSAGE);
        } else{
            /*System.out.println("即将传递给changeBookBorrowedStatus的student id值: " + student.getId());*/
            BookOperate.changeBookBorrowedStatus(booksData,selectedBook,student.getId(),StudentFrame.this);
            JOptionPane.showMessageDialog(this, "成功借阅书籍：" + selectedBook.getTitle(), "提示", JOptionPane.INFORMATION_MESSAGE);
            refreshBooks(bookListModel);
            updateBookInfo();
        }
    }

    private void returnBook(DefaultListModel<Book> bookListModel) {
        Book selectedBook = bookList.getSelectedValue();
        if (selectedBook == null) {
            JOptionPane.showMessageDialog(this, "请选择一本书！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!selectedBook.isBorrowed()) {
            JOptionPane.showMessageDialog(this, "这本书没有被借出！", "提示", JOptionPane.WARNING_MESSAGE);
        } else if(selectedBook.getBorrowedById() == student.getId()) {
            BookOperate.changeBookBorrowedStatus(booksData,selectedBook,student.getId(),StudentFrame.this);
            JOptionPane.showMessageDialog(this, "成功归还书籍：" + selectedBook.getTitle(), "提示", JOptionPane.INFORMATION_MESSAGE);
            refreshBooks(bookListModel);
            updateBookInfo();
        } else {
            JOptionPane.showMessageDialog(this, "哎呀，这本书好像不是您借的！", "提示", JOptionPane.WARNING_MESSAGE);
        }

    }
}