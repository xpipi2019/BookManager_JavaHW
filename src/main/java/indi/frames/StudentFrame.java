package main.java.indi.frames;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import main.java.indi.Book;
import main.java.indi.dao.DBUtil;
import main.java.indi.roles.Student;
import main.java.indi.dao.BookOperate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static com.formdev.flatlaf.FlatLaf.updateUI;
import static main.java.indi.constant.Constants.aboutMessage;
import static main.java.indi.utils.ConfigManager.saveTheme;

/**
 * 学生使用界面 StudentFrame
 */
public class StudentFrame extends JFrame {
    private static final Logger logger = LoggerFactory.getLogger(StudentFrame.class);
    // 学生数据
    private Student student;
    // JList对象
    private JList<Book> bookList;
    // 临时存储读取文件中的图书数据
    ArrayList<Book> booksData = new ArrayList<>();
    // 最大可借书数量
    private final int MAX_BORROW_LIMIT = 3;
    // 图书信息文本域
    private JTextArea bookInfoArea;

    /**
     * 窗体初始化
     *
     * @param student 学生对象
     */
    public StudentFrame(Student student) {
        this.student = student;

        // 设置窗口信息
        setTitle("图书借阅系统--学生");
        setLayout(new BorderLayout());

        // 窗体退出事件 登陆后退出：正常退出，返回0
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int confirm = JOptionPane.showConfirmDialog(StudentFrame.this, "是否确定要退出本系统？", "确认退出", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    logger.info("Exit after login.");
                    System.exit(0);
                } else{
                    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                }
            }
        });

        // 顶部显示学生信息
        JPanel topPanel = new JPanel(new BorderLayout());

        // 学生信息标签
        JLabel studentInfoLabel = new JLabel(
                "姓名: " + student.getName() + " | 学号: " + student.getId() + " | 性别: " + student.getGender(),
                JLabel.CENTER);

        // topPanel -> studentInfoLabel 居中
        topPanel.add(studentInfoLabel, BorderLayout.CENTER);

        // 中间书籍选择和详细信息
        JPanel middlePanel = new JPanel(new GridLayout(1, 2, 10, 10));

        // 创建管理列表数据模型
        DefaultListModel<Book> bookListModel = new DefaultListModel<>();

        // 左侧：带滚动条的书籍列表
        bookList = new JList<>(bookListModel);
        bookList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookList.addListSelectionListener(e -> updateBookInfo());
        JScrollPane bookScrollPane = new JScrollPane(bookList);
        middlePanel.add(bookScrollPane);

        // 右侧：选中书籍的详细信息（不可编辑）
        bookInfoArea = new JTextArea();
        bookInfoArea.setEditable(false);
        JScrollPane infoScrollPane = new JScrollPane(bookInfoArea);
        middlePanel.add(infoScrollPane);

        // 底部按钮
        JPanel bottomPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        JButton refreshButton = new JButton("刷新书籍");
        JButton borrowButton = new JButton("借阅书籍");
        JButton returnButton = new JButton("归还书籍");
        JButton exitButton = new JButton("退出程序");

        bottomPanel.add(refreshButton);
        bottomPanel.add(borrowButton);
        bottomPanel.add(returnButton);
        bottomPanel.add(exitButton);

        // 创建菜单栏
        JMenuBar menuBar = new JMenuBar();

        // 创建设置菜单
        JMenu settingsMenu = new JMenu("设置");

        // 创建主题选择子菜单
        JMenu themeMenu = new JMenu("主题");

        // 创建主题选项
        JMenuItem darculaThemeItem = new JMenuItem("Darcula");
        JMenuItem lightThemeItem = new JMenuItem("Light");
        JMenuItem darkThemeItem = new JMenuItem("Dark");

        darculaThemeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FlatDarculaLaf.setup();
                System.out.println("Darcula主题已启用");
                updateUI();
                saveTheme("Darcula");
            }
        });

        lightThemeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FlatLightLaf.setup();
                System.out.println("Light主题已启用");
                updateUI();
                saveTheme("Light");
            }
        });

        darkThemeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FlatDarkLaf.setup();
                System.out.println("Dark主题已启用");
                updateUI();
                saveTheme("Dark");
            }
        });

        // 将主题选项添加到主题菜单
        themeMenu.add(darculaThemeItem);
        themeMenu.add(lightThemeItem);
        themeMenu.add(darkThemeItem);

        // 将主题菜单添加到设置菜单
        settingsMenu.add(themeMenu);

        // 创建关于菜单
        JMenu aboutMenu = new JMenu("关于");

        // 创建关于系统菜单项
        JMenuItem aboutItem = new JMenuItem("关于系统");

        // 添加关于系统事件监听器
        aboutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(StudentFrame.this, aboutMessage, "关于", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // 将对于系统菜单项添加到关于菜单
        aboutMenu.add(aboutItem);

        // 将设置菜单和对于菜单添加到菜单栏
        menuBar.add(settingsMenu);
        menuBar.add(aboutMenu);

        // 设置菜单栏到窗口
        setJMenuBar(menuBar);

        // 添加组件到窗口
        setLayout(new BorderLayout(10, 10));
        add(topPanel, BorderLayout.NORTH);
        add(middlePanel, BorderLayout.CENTER);
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

        logger.info("StartFrame initialized successfully.");
    }

    /**
     * 刷新书籍列表
     *
     * @param bookListModel 书籍列表模型
     */
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
            logger.error("Failed to retrieve book data from database: " + e.getMessage(), e);
        }
        setStudentBorrowCount(student);
    }

    /**
     * 设置学生借书数量
     *
     * @param student 学生对象
     */
    public void setStudentBorrowCount(Student student) {
        int count = 0;
        for (Book book : booksData) {
            if (book.getBorrowedById() == student.getId()) {
                count++;
            }
        }
        student.setBorrowedBooksCount(count);
    }

    /**
     * 更新选中书籍的信息
     */
    private void updateBookInfo() {
        Book selectedBook = bookList.getSelectedValue();
        if (selectedBook != null) {
            String whoBorrowedName;
            if (selectedBook.getBorrowedById() == 0) {
                whoBorrowedName = "未借出";
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
        } else {
            bookInfoArea.setText("");
        }
    }

    /**
     * 借阅书籍
     *
     * @param bookListModel 书籍列表模型
     */
    private void borrowBook(DefaultListModel<Book> bookListModel) {
        Book selectedBook = bookList.getSelectedValue();
        if (selectedBook == null) {
            JOptionPane.showMessageDialog(this, "请选择一本书！", "提示", JOptionPane.WARNING_MESSAGE);
            logger.warn("No book selected.");
            return;
        }

        if (selectedBook.isBorrowed()) {
            JOptionPane.showMessageDialog(this, "这本书已经被借出！", "提示", JOptionPane.WARNING_MESSAGE);
            logger.warn("Book already borrowed.");
        } else if (student.getBorrowedBooksCount() >= MAX_BORROW_LIMIT) {
            JOptionPane.showMessageDialog(this, "您最多同时借三本书！", "提示", JOptionPane.WARNING_MESSAGE);
            logger.warn("Maximum borrow limit reached.");
        } else {
            BookOperate.changeBookBorrowedStatus(booksData, selectedBook, student.getId(), StudentFrame.this);
            JOptionPane.showMessageDialog(this, "成功借阅书籍：" + selectedBook.getTitle(), "提示", JOptionPane.INFORMATION_MESSAGE);
            logger.info("Book borrowed successfully: {}", selectedBook.getTitle());
            refreshBooks(bookListModel);
            updateBookInfo();
        }
    }

    /**
     * 归还书籍
     *
     * @param bookListModel 书籍列表模型
     */
    private void returnBook(DefaultListModel<Book> bookListModel) {
        Book selectedBook = bookList.getSelectedValue();
        if (selectedBook == null) {
            JOptionPane.showMessageDialog(this, "请选择一本书！", "提示", JOptionPane.WARNING_MESSAGE);
            logger.warn("No book selected.");
            return;
        }

        if (!selectedBook.isBorrowed()) {
            JOptionPane.showMessageDialog(this, "这本书没有被借出！", "提示", JOptionPane.WARNING_MESSAGE);
            logger.warn("Book not borrowed.");
        } else if (selectedBook.getBorrowedById() == student.getId()) {
            BookOperate.changeBookBorrowedStatus(booksData, selectedBook, student.getId(), StudentFrame.this);
            JOptionPane.showMessageDialog(this, "成功归还书籍：" + selectedBook.getTitle(), "提示", JOptionPane.INFORMATION_MESSAGE);
            refreshBooks(bookListModel);
            logger.info("Book returned successfully: {}", selectedBook.getTitle());
            updateBookInfo();
        } else {
            JOptionPane.showMessageDialog(this, "哎呀，这本书好像不是您借的！", "提示", JOptionPane.WARNING_MESSAGE);
            logger.warn("Incorrect username or password for user: {}", student.getId());
        }
    }
}