package pers.frames;

import pers.Book;
import pers.dao.UserDao;
import pers.dao.UserDaoImpl;
import pers.roles.Person;
import pers.roles.Student;
import pers.roles.Teacher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Map;

import static pers.dao.BookOperate.readBookData;

/**
 * @author XPIPI
 */
// 界面一：登录界面 StartFrame
public class StartFrame extends JFrame {
    // 用户类型 JComboBox
    private final JComboBox<String> userTypeComboBox;
    // 用户名 JTextField
    private JTextField usernameField;
    // 密码 JPasswordField
    private JPasswordField passwordField;
    // 存储所有用户 HashMap<K,V>
    private Map<String, User> usersMap;
    // 存储所有用户 HashMap<K,V>
    private Map<String, Person> personsMap;
    // 记住密码选择框 JCheckBox
    private JCheckBox rememberMeBox;
    // 定义主题名 String
    private String theme;


    // 初始化登录界面
    public StartFrame() {
        // 加载用户数据 loadUserData() loadPersonData()
        UserDao userDao = new UserDaoImpl();
        usersMap = userDao.loadUserData();
        personsMap = userDao.loadPersonData();


        // 设置窗口信息，布局、标题、大小
        setDefaultLookAndFeelDecorated(true);
        setSize(250, 200);
        setTitle("欢迎使用");
        setLayout(new GridLayout(4, 2, 5, 5));

        // 窗体退出事件 登陆前退出：异常退出，返回-1
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(-1);
            }
        });

        // 组件创建以及布局
        // 第一行：用户类型 userTypePanel
        JPanel userTypePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel userTypeLabel = new JLabel("用户类型:");
        userTypeComboBox = new JComboBox<>(new String[]{"教师", "学生","管理"});
        userTypePanel.add(userTypeLabel);
        userTypePanel.add(userTypeComboBox);

        // 第二行：用户名输入 userPanel
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel userLabel = new JLabel("账号：");
        usernameField = new JTextField(15);
        userPanel.add(userLabel);
        userPanel.add(usernameField);

        // 第三行：密码输入 passPanel
        JPanel passPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel passLabel = new JLabel("密码：");
        passwordField = new JPasswordField(15);
        passPanel.add(passLabel);
        passPanel.add(passwordField);

        // 第四行：操作按钮 actionPanel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rememberMeBox = new JCheckBox("记住密码", true);
        JButton loginButton = new JButton("确定");
        actionPanel.add(rememberMeBox);
        actionPanel.add(loginButton);

        // 将四个部分组件添加到窗体中
        add(userTypePanel);
        add(userPanel);
        add(passPanel);
        add(actionPanel);

        // 如果保存了登录信息在配置文件中，则预填充用户名和密码信息
        theme = PreLoader.loadConfig(usernameField, passwordField, userTypeComboBox, rememberMeBox);

        // 按钮点击事件
        loginButton.addActionListener(new LoginButtonListener());

        // 窗口居中
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // 登录按钮监听器
    private class LoginButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedType = (String) userTypeComboBox.getSelectedItem();
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            boolean rememberMeStatus = rememberMeBox.isSelected();

            // 登录验证
            if (usersMap.containsKey(username)) {
                User user = usersMap.get(username);
                Person person = personsMap.get(user.getIsWho());

                if (user.getPassword().equals(password) && user.getType().equals(selectedType)) {
                    JOptionPane.showMessageDialog(StartFrame.this, "登录成功！欢迎 " + user.getType() + " " + username);

                    // 保存登录信息 -在登录成功后调用-
                    ConfigManager.saveLoginInfo(username, password, selectedType, rememberMeStatus,theme);

                    // 打开对应用户界面 -在登录成功后调用-
                    openUserInterface(selectedType, person);

                    dispose();
                } else {
                    JOptionPane.showMessageDialog(StartFrame.this, "用户名或密码错误！", "登录失败", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(StartFrame.this, "用户名不存在！", "登录失败", JOptionPane.WARNING_MESSAGE);
            }
        }

        private void openUserInterface(String selectedType, Person person) {
            if ("教师".equals(selectedType)) {
                new TeacherFrame(new Teacher(person.getName(), person.getId(), person.getGender()));
            } else if ("学生".equals(selectedType)) {
                new StudentFrame(new Student(person.getName(), person.getId(), person.getGender()));
            } else {
                ArrayList<Book> booksData = new ArrayList<>();
                readBookData(booksData, StartFrame.this);
                new ManageFrame(usersMap, personsMap, booksData);
            }
        }
    }

}