package main.java.indi.frames;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import main.java.indi.Book;
import main.java.indi.dao.DBUtil;
import main.java.indi.roles.Person;

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
import java.util.Map;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import static main.java.indi.utils.LoginUtil.generateSalt;
import static main.java.indi.utils.LoginUtil.hashPassword;


/**
 * 管理者使用界面 ManageFrame
 */
public class ManageFrame extends JFrame {
    private static final Logger logger = LoggerFactory.getLogger(ManageFrame.class);
    private Map<String, User> usersMap;
    private Map<String, Person> personsMap;
    private ArrayList<Book> books;

    /**
     * 构造函数，初始化管理界面
     *
     * @param usersMap   存储用户数据的Map
     * @param personsMap 存储Person数据的Map
     * @param books      存储书籍数据的ArrayList
     */
    public ManageFrame(Map<String, User> usersMap, Map<String, Person> personsMap, ArrayList<Book> books) {
        this.usersMap = usersMap;
        this.personsMap = personsMap;
        this.books = books;

        // 设置窗口信息
        setTitle("图书借阅系统--管理系统");
        setLayout(new BorderLayout(10, 10));
        setPreferredSize(new Dimension(400, 300));

        // 窗体退出事件 登陆前退出：异常退出，返回-1
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int confirm = JOptionPane.showConfirmDialog(ManageFrame.this, "是否确定要退出本系统？", "确认退出", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    System.exit(-1);
                } else{
                    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                }
            }
        });

        // 创建主面板
        JPanel mainPanel = new JPanel(new GridLayout(3, 1, 10, 10));

        // 按钮1：修改用户数据
        JButton modifyUserButton = new JButton("修改用户数据");
        modifyUserButton.addActionListener(e -> openModifyUserDialog());
        mainPanel.add(modifyUserButton);

        // 按钮2：查看用户借阅书籍列表
        JButton viewUserBooksButton = new JButton("查看用户借阅的书籍");
        viewUserBooksButton.addActionListener(e -> openViewUserBooksDialog());
        mainPanel.add(viewUserBooksButton);

        // 按钮3：修改书籍信息
        JButton modifyBookButton = new JButton("修改书籍信息");
        modifyBookButton.addActionListener(e -> openModifyBookDialog());
        mainPanel.add(modifyBookButton);

        // 添加主面板到窗口
        add(mainPanel, BorderLayout.CENTER);

        // 设置窗口属性
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        logger.info("ManageFrame initialized successfully.");
    }

    /**
     * 打开修改用户数据的对话框
     */
    private void openModifyUserDialog() {
        JDialog dialog = new JDialog(this, "修改用户数据", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setPreferredSize(new Dimension(600, 400));

        // 用户选择列表
        DefaultListModel<String> userListModel = new DefaultListModel<>();
        for (User user : usersMap.values()) {
            userListModel.addElement(user.getIsWho()); // 显示用户名而不是对象引用
        }
        JList<String> userList = new JList<>(userListModel);
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane userScrollPane = new JScrollPane(userList);
        userScrollPane.setPreferredSize(new Dimension(200, 300));

        // 编辑面板
        JPanel editPanel = new JPanel(new GridLayout(7, 2, 10, 20));
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField(); // 使用 JPasswordField
        // 创建 JRadioButton
        JRadioButton adminRadioButton = new JRadioButton("管理");
        JRadioButton teacherRadioButton = new JRadioButton("教师");
        JRadioButton studentRadioButton = new JRadioButton("学生");

        // 创建 ButtonGroup
        ButtonGroup typeButtonGroup = new ButtonGroup();
        typeButtonGroup.add(adminRadioButton);
        typeButtonGroup.add(teacherRadioButton);
        typeButtonGroup.add(studentRadioButton);

        JTextField isWhoField = new JTextField();

        editPanel.add(new JLabel("用户名:"));
        editPanel.add(usernameField);
        editPanel.add(new JLabel("密码:"));
        editPanel.add(passwordField);
        editPanel.add(new JLabel("用户类型:"));
        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // 使用 JPanel 容纳单选按钮
        radioPanel.add(adminRadioButton);
        radioPanel.add(teacherRadioButton);
        radioPanel.add(studentRadioButton);
        editPanel.add(radioPanel); // 将 JPanel 添加到 editPanel
        editPanel.add(new JLabel("对应人:"));
        editPanel.add(isWhoField);

        // 确定按钮
        JButton saveButton = new JButton("保存修改");
        saveButton.setPreferredSize(new Dimension(100, 30));
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedUsername = userList.getSelectedValue();
                if (selectedUsername != null) {
                    User selectedUser = getUserByIswho(selectedUsername);
                    String newUsername = usernameField.getText();
                    char[] newPasswordChars = passwordField.getPassword(); // 获取密码字符数组
                    String newType = null;
                    if (adminRadioButton.isSelected()) {
                        newType = "管理";
                    } else if (teacherRadioButton.isSelected()) {
                        newType = "教师";
                    } else if (studentRadioButton.isSelected()) {
                        newType = "学生";
                    }
                    String newIsWho = isWhoField.getText();

                    try (Connection connection = DBUtil.getConnection()) {
                        String salt = generateSalt();
                        String hashedPassword = hashPassword(new String(newPasswordChars), salt); // 加密密码
                        Arrays.fill(newPasswordChars, '0');//清空密码数组，防止密码泄露
                        // 更新用户数据的SQL语句，存储加密后的密码
                        // 更新用户数据的SQL语句
                        String updateUserQuery = "UPDATE users SET username =?, password =?, type =?, is_who =?, salt =? WHERE username =?";
                        try (PreparedStatement userPreparedStatement = connection.prepareStatement(updateUserQuery)) {
                            userPreparedStatement.setString(1, newUsername);
                            userPreparedStatement.setString(2, hashedPassword);
                            userPreparedStatement.setString(3, newType);
                            userPreparedStatement.setString(4, newIsWho);
                            userPreparedStatement.setString(5, salt);
                            userPreparedStatement.setString(6, selectedUser.getUsername());
                            int userRowsAffected = userPreparedStatement.executeUpdate();

                            if (userRowsAffected > 0) {
                                // 更新成功后，同步更新本地缓存的 usersMap 数据
                                selectedUser.setUsername(newUsername);
                                selectedUser.setPassword(hashedPassword);
                                selectedUser.setType(newType);
                                selectedUser.setIsWho(newIsWho);

                                // 更新 persons 表中对应人员的数据
                                Person relatedPerson = personsMap.get(selectedUser.getIsWho());
                                if (relatedPerson != null) {
                                    String updatePersonQuery = "UPDATE persons SET name =? WHERE id =?";
                                    try (PreparedStatement personPreparedStatement = connection.prepareStatement(updatePersonQuery)) {
                                        personPreparedStatement.setString(1, newIsWho);
                                        personPreparedStatement.setInt(2, relatedPerson.getId());
                                        int personRowsAffected = personPreparedStatement.executeUpdate();
                                        if (personRowsAffected > 0) {
                                            relatedPerson.setName(newIsWho);
                                            JOptionPane.showMessageDialog(dialog, "用户数据和相关人员数据已成功修改！");
                                            logger.info("User data and related person data updated successfully for user: {}", newUsername);
                                        } else {
                                            JOptionPane.showMessageDialog(dialog, "修改人员数据失败，可能人员不存在或其他原因，请检查！", "错误", JOptionPane.ERROR_MESSAGE);
                                            logger.error("Failed to update person data for user: {}", newUsername);
                                        }
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(dialog, "未找到相关人员信息，请检查！", "错误", JOptionPane.ERROR_MESSAGE);
                                    logger.error("Related person information not found for user: {}", newUsername);
                                }
                            } else {
                                JOptionPane.showMessageDialog(dialog, "修改用户数据失败，可能用户不存在或其他原因，请检查！", "错误", JOptionPane.ERROR_MESSAGE);
                                logger.error("Failed to update user data for user: {}", newUsername);
                            }
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(dialog, "修改用户数据出现数据库错误: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                        logger.error("Database error occurred while updating user data for user: {}", newUsername, ex);
                    } catch (NoSuchAlgorithmException ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(dialog, "请选择一个用户！");
                    logger.warn("No user selected for modification.");
                }
            }
        });

        // 用户选择监听器
        userList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedIswho = userList.getSelectedValue();
                if (selectedIswho != null) {
                    User selectedUser = getUserByIswho(selectedIswho);
                    if (selectedUser != null) {
                        usernameField.setText(selectedUser.getUsername());
                        //passwordField.setText(selectedUser.getPassword()); // 不显示密码
                        if (selectedUser.getType().equals("管理")) {
                            adminRadioButton.setSelected(true);
                        } else if (selectedUser.getType().equals("教师")) {
                            teacherRadioButton.setSelected(true);
                        } else if (selectedUser.getType().equals("学生")) {
                            studentRadioButton.setSelected(true);
                        }
                        isWhoField.setText(selectedUser.getIsWho());
                    }
                }
            }
        });

        // 布局
        dialog.add(userScrollPane, BorderLayout.WEST);
        dialog.add(editPanel, BorderLayout.CENTER);
        dialog.add(saveButton, BorderLayout.SOUTH);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }


    /**
     * 根据用户名从 usersMap 中获取用户对象
     *
     * @param is_who 用户对应的Person名称
     * @return 用户对象
     */
    private User getUserByIswho(String is_who) {
        for (User user : usersMap.values()) {
            if (user.getIsWho().equals(is_who)) {
                return user;
            }
        }
        return null;
    }

    /**
     * 打开查看用户借阅书籍的对话框
     */
    private void openViewUserBooksDialog() {
        JDialog dialog = new JDialog(this, "查看用户借阅的书籍", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setPreferredSize(new Dimension(600, 400));

        // 用户选择列表
        DefaultListModel<String> userListModel = new DefaultListModel<>();
        for (User user : usersMap.values()) {
            userListModel.addElement(user.getIsWho());
        }
        JList<String> userList = new JList<>(userListModel);
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane userScrollPane = new JScrollPane(userList);
        userScrollPane.setPreferredSize(new Dimension(200, 300));

        // 显示借阅书籍列表
        JTextArea borrowedBooksArea = new JTextArea();
        borrowedBooksArea.setEditable(false);
        JScrollPane booksScrollPane = new JScrollPane(borrowedBooksArea);
        booksScrollPane.setPreferredSize(new Dimension(350, 300));

        userList.addListSelectionListener(e -> {
            String selectedIswho = userList.getSelectedValue();
            if (selectedIswho != null) {
                borrowedBooksArea.setText(""); // 清空当前内容
                User selectedUser = getUserByIswho(selectedIswho);
                if (selectedUser != null) {
                    Person person = personsMap.get(selectedUser.getIsWho());
                    try (Connection connection = DBUtil.getConnection()) {
                        String query = "SELECT book_id, title, book_type FROM books WHERE borrowed_by_id =?";
                        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                            preparedStatement.setInt(1, person.getId());
                            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                                while (resultSet.next()) {
                                    int bookId = resultSet.getInt("book_id");
                                    String title = resultSet.getString("title");
                                    String bookType = resultSet.getString("book_type");
                                    borrowedBooksArea.append("书号: " + bookId + " 书名: 《" + title + "》 类型: " + bookType + "\n");
                                }
                            }
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(dialog, "查询用户借阅书籍信息出现数据库错误: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                        logger.error("Database error occurred while querying user borrowed books for user: {}", selectedIswho, ex);
                    }
                } else {
                    borrowedBooksArea.setText("找不到对应的用户信息！");
                    logger.warn("User information not found for user: {}", selectedIswho);
                }
            }
        });

        // 布局
        dialog.add(userScrollPane, BorderLayout.WEST);
        dialog.add(booksScrollPane, BorderLayout.CENTER);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    /**
     * 打开修改书籍信息的对话框
     */
    private void openModifyBookDialog() {
        JDialog dialog = new JDialog(this, "修改书籍信息", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setPreferredSize(new Dimension(600, 400));

        // 书籍选择列表
        DefaultListModel<Book> bookListModel = new DefaultListModel<>();
        for (Book book : books) {
            bookListModel.addElement(book);
        }
        JList<Book> bookList = new JList<>(bookListModel);
        bookList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane bookScrollPane = new JScrollPane(bookList);
        bookScrollPane.setPreferredSize(new Dimension(200, 300));

        // 编辑面板
        JPanel editPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField bookTypeField = new JTextField();

        editPanel.add(new JLabel("书名:"));
        editPanel.add(titleField);
        editPanel.add(new JLabel("作者:"));
        editPanel.add(authorField);
        editPanel.add(new JLabel("类别:"));
        editPanel.add(bookTypeField);

        // 确定按钮
        JButton saveButton = new JButton("保存修改");
        saveButton.setPreferredSize(new Dimension(100, 30));
        saveButton.addActionListener(e -> {
            Book selectedBook = bookList.getSelectedValue();
            if (selectedBook != null) {
                String newTitle = titleField.getText();
                String newAuthor = authorField.getText();
                String newBookType = bookTypeField.getText();
                try (Connection connection = DBUtil.getConnection()) {
                    // 更新书籍信息的SQL语句
                    String updateQuery = "UPDATE books SET title =?, author =?, book_type =? WHERE book_id =?";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                        preparedStatement.setString(1, newTitle);
                        preparedStatement.setString(2, newAuthor);
                        preparedStatement.setString(3, newBookType);
                        preparedStatement.setInt(4, selectedBook.getBookId());
                        int rowsAffected = preparedStatement.executeUpdate();
                        if (rowsAffected > 0) {
                            // 更新成功后，同步更新本地缓存的books数据
                            selectedBook.setTitle(newTitle);
                            selectedBook.setAuthor(newAuthor);
                            selectedBook.setBookType(newBookType);
                            JOptionPane.showMessageDialog(dialog, "书籍信息已成功修改！");
                            logger.info("Book information updated successfully for book ID: {}", selectedBook.getBookId());
                        } else {
                            JOptionPane.showMessageDialog(dialog, "修改书籍信息失败，可能书籍不存在或其他原因，请检查！", "错误", JOptionPane.ERROR_MESSAGE);
                            logger.error("Failed to update book information for book ID: {}", selectedBook.getBookId());
                        }
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(dialog, "修改书籍信息出现数据库错误: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                    logger.error("Database error occurred while updating book information for book ID: {}", selectedBook.getBookId(), ex);
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "请选择一本书！");
                logger.warn("No book selected for modification.");
            }
        });

        // 书籍选择监听器
        bookList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Book selectedBook = bookList.getSelectedValue();
                if (selectedBook != null) {
                    titleField.setText(selectedBook.getTitle());
                    authorField.setText(selectedBook.getAuthor());
                    bookTypeField.setText(selectedBook.getBookType());
                }
            }
        });

        // 布局
        dialog.add(bookScrollPane, BorderLayout.WEST);
        dialog.add(editPanel, BorderLayout.CENTER);
        dialog.add(saveButton, BorderLayout.SOUTH);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
}
