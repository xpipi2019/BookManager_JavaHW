package pers.frames;

import pers.Book;
import pers.roles.Person;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Map;

public class ManageFrame extends JFrame {
    private Map<String, User> usersMap; // 存储用户数据
    private Map<String, Person> personsMap; // 存储Person数据
    private ArrayList<Book> books; // 存储书籍数据

    public ManageFrame(Map<String, User> usersMap, Map<String, Person> personsMap, ArrayList<Book> books) {
        this.usersMap = usersMap;
        this.personsMap = personsMap;
        this.books = books;

        // 设置窗口信息
        setTitle("图书借阅系统--管理系统");
        setLayout(new BorderLayout());

        // 窗体退出事件
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
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
        setSize(400, 300);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // 打开修改用户数据的对话框
    private void openModifyUserDialog() {
        JDialog dialog = new JDialog(this, "修改用户数据", true);
        dialog.setLayout(new BorderLayout());

        // 用户选择列表
        DefaultListModel<User> userListModel = new DefaultListModel<>();
        for (User user : usersMap.values()) {
            userListModel.addElement(user);
        }
        JList<User> userList = new JList<>(userListModel);
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane userScrollPane = new JScrollPane(userList);

        // 编辑面板
        JPanel editPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        JTextField usernameField = new JTextField();
        JTextField passwordField = new JTextField();
        JTextField typeField = new JTextField();
        JTextField isWhoField = new JTextField();

        editPanel.add(new JLabel("用户名:"));
        editPanel.add(usernameField);
        editPanel.add(new JLabel("密码:"));
        editPanel.add(passwordField);
        editPanel.add(new JLabel("用户类型:"));
        editPanel.add(typeField);
        editPanel.add(new JLabel("对应人:"));
        editPanel.add(isWhoField);

        // 确定按钮
        JButton saveButton = new JButton("保存修改");
        saveButton.addActionListener(e -> {
            User selectedUser = userList.getSelectedValue();
            if (selectedUser != null) {
                selectedUser.setUsername(usernameField.getText());
                selectedUser.setPassword(passwordField.getText());
                selectedUser.setType(typeField.getText());
                JOptionPane.showMessageDialog(dialog, "用户数据已成功修改！");
            } else {
                JOptionPane.showMessageDialog(dialog, "请选择一个用户！");
            }
        });

        // 布局
        dialog.add(userScrollPane, BorderLayout.WEST);
        dialog.add(editPanel, BorderLayout.CENTER);
        dialog.add(saveButton, BorderLayout.SOUTH);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    // 打开查看用户借阅书籍的对话框
    private void openViewUserBooksDialog() {
        JDialog dialog = new JDialog(this, "查看用户借阅的书籍", true);
        dialog.setLayout(new BorderLayout());

        // 用户选择列表
        DefaultListModel<User> userListModel = new DefaultListModel<>();
        for (User user : usersMap.values()) {
            userListModel.addElement(user);
        }
        JList<User> userList = new JList<>(userListModel);
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane userScrollPane = new JScrollPane(userList);

        // 显示借阅书籍列表
        JTextArea borrowedBooksArea = new JTextArea();
        borrowedBooksArea.setEditable(false);
        JScrollPane booksScrollPane = new JScrollPane(borrowedBooksArea);

        userList.addListSelectionListener(e -> {
            User selectedUser = userList.getSelectedValue();
            if (selectedUser != null) {
                borrowedBooksArea.setText(""); // 清空当前内容
                Person person = personsMap.get(selectedUser.getIsWho());
                if (person != null) {
                    for (Book book : books) {
                        if (book.getBorrowedById() == person.getId()) {
                            borrowedBooksArea.append("书号: " + book.getBookId() +
                                    " 书名: " + book.getTitle() + "\n");
                        }
                    }
                } else {
                    borrowedBooksArea.setText("找不到对应的用户信息！");
                }
            }
        });

        // 布局
        dialog.add(userScrollPane, BorderLayout.WEST);
        dialog.add(booksScrollPane, BorderLayout.CENTER);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    // 打开修改书籍信息的对话框
    private void openModifyBookDialog() {
        JDialog dialog = new JDialog(this, "修改书籍信息", true);
        dialog.setLayout(new BorderLayout());

        // 书籍选择列表
        DefaultListModel<Book> bookListModel = new DefaultListModel<>();
        for (Book book : books) {
            bookListModel.addElement(book);
        }
        JList<Book> bookList = new JList<>(bookListModel);
        bookList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane bookScrollPane = new JScrollPane(bookList);

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
        saveButton.addActionListener(e -> {
            Book selectedBook = bookList.getSelectedValue();
            if (selectedBook != null) {
                selectedBook.setTitle(titleField.getText());
                selectedBook.setAuthor(authorField.getText());
                selectedBook.setBookType(bookTypeField.getText());
                JOptionPane.showMessageDialog(dialog, "书籍信息已成功修改！");
            } else {
                JOptionPane.showMessageDialog(dialog, "请选择一本书！");
            }
        });

        // 布局
        dialog.add(bookScrollPane, BorderLayout.WEST);
        dialog.add(editPanel, BorderLayout.CENTER);
        dialog.add(saveButton, BorderLayout.SOUTH);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
}
