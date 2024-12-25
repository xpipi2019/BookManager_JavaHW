package pers.frames;
import java.util.logging.Logger;
import pers.dao.DBUtil;
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
import java.io.*;
import java.util.HashMap;
import java.util.Map;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
    // 定义配置文件名常量 String
    // 用来保存一些功能设置，在每次打开时加载
    private final String CONFIG_FILE = "config.ini";
    // 定义用户件名常量 String
    private final String USERS_FILE = "users.txt";
    private final String PERSONS_FILE = "persons.txt";

    // 初始化登录界面
    public StartFrame() {
        Logger logger = Logger.getLogger(StartFrame.class.getName());
        logger.info("StartFrame 构造函数开始执行");
        loadUserData();
        loadPersonData();
        // 使用接口加载用户数据
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
        userTypeComboBox = new JComboBox<>(new String[]{"教师", "学生", "管理"});
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
        prefillLoginInfo();

        // 按钮点击事件
        loginButton.addActionListener(new LoginButtonListener());

        // 窗口居中
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // 加载用户数据 loadUserData()
    private void loadUserData() {
        Logger logger = Logger.getLogger(StartFrame.class.getName());
        usersMap = new HashMap<>();
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT type, username, password, is_who FROM users");
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                String type = resultSet.getString("type");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String isWho = resultSet.getString("is_who");
                usersMap.put(username, new User(type, username, password, isWho));
            }
            logger.info("用户数据加载成功");
        } catch (SQLException e) {
            logger.severe("用户数据从数据库加载失败：" + e.getMessage());
            JOptionPane.showMessageDialog(this, "用户数据从数据库加载失败: " + e.getMessage(),
                    "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadPersonData() {
        Logger logger = Logger.getLogger(StartFrame.class.getName());
        personsMap = new HashMap<>();
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT name, id, gender FROM persons");
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                int id = resultSet.getInt("id");
                String gender = resultSet.getString("gender");
                personsMap.put(name, new Person(name, id, gender));
            }
            logger.info("人员数据加载成功");
        } catch (SQLException e) {
            logger.severe("人员数据从数据库加载失败: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "人员数据从数据库加载失败: " + e.getMessage(),
                    "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 预填充保存的登录信息
    private void prefillLoginInfo() {
        File configFile = new File(CONFIG_FILE);
        // 初次启动，无配置文件
        if (!configFile.exists()) {
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(configFile))) {
            String line;
            String username = "";
            String password = "";
            boolean rememberMeStatus = false;

            // BufferedReader文件流 读取配置文件内容
            while ((line = br.readLine()) != null) {
                if (line.startsWith("username=")) {
                    username = line.substring(line.indexOf('=') + 1).trim();
                } else if (line.startsWith("password=")) {
                    password = line.substring(line.indexOf('=') + 1).trim();
                } else if (line.startsWith("rememberMe=")) {
                    rememberMeStatus = Boolean.parseBoolean(line.substring(line.indexOf('=') + 1).trim());
                }
            }

            // 填充配置文件内容
            usernameField.setText(username);
            passwordField.setText(password);
            rememberMeBox.setSelected(rememberMeStatus);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "配置文件加载失败: " + e.getMessage(),
                    "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 保存登录信息到配置文件 -在登录成功后调用-
    private void saveLoginInfo(String username, String password, boolean rememberMe) {
        String sql = "INSERT INTO login_history (id, username, login_time) VALUES (login_history_seq.nextval,?, SYSDATE)";
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, username);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(StartFrame.class.getName()).severe("保存登录信息到数据库失败，原因: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "保存登录信息到数据库失败: " + e.getMessage(),
                    "错误", JOptionPane.ERROR_MESSAGE);
        }
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
                // 用户 user
                User user = usersMap.get(username);
                // 用户对应的人 person
                Person person = personsMap.get(user.getIsWho());

                // 获得person对象的所有信息
                if (person!= null) {
                    String person_name = person.getName();
                    int person_id = person.getId();
                    String person_gender = person.getGender();if (user.getPassword().equals(password) && user.getType().equals(selectedType)) {
                        JOptionPane.showMessageDialog(StartFrame.this, "登录成功！欢迎 " + user.getType() + " " + username);

                        // 保存登录信息 -在登录成功后调用-
                        saveLoginInfo(username, password, rememberMeStatus);

                        // 打开对应用户界面 -在登录成功后调用-
                        if ("教师".equals(selectedType)) {
                            new TeacherFrame(new Teacher(person_name, person_id, person_gender));
                        } else if ("学生".equals(selectedType)) {
                            new StudentFrame(new Student(person_name, person_id, person_gender));
                        } else {
                            new ManageFrame();
                        }
                    /*
                        该窗口不再需要使用，所以不用setVisible方法隐藏窗口
                        使用dispose方法关闭窗口，可以释放一部分资源
                     */
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(StartFrame.this, "用户名或密码错误！", "登录失败", JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(StartFrame.this, "获取人员信息失败，对应的Person对象为null，请检查数据！", "错误", JOptionPane.ERROR_MESSAGE);
                }


            } else {
                JOptionPane.showMessageDialog(StartFrame.this, "用户名不存在！", "登录失败", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
}