package main.java.indi.frames;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import main.java.indi.Book;
import main.java.indi.dao.UserDao;
import main.java.indi.dao.UserDaoImpl;
import main.java.indi.roles.Person;
import main.java.indi.roles.Student;
import main.java.indi.roles.Teacher;
import main.java.indi.utils.ConfigManager;
import main.java.indi.utils.PreLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Map;

import static com.formdev.flatlaf.FlatLaf.updateUI;
import static main.java.indi.constant.Constants.aboutMessage;
import static main.java.indi.dao.BookOperate.readBookData;

/**
 * 登录界面 StartFrame
 */
public class StartFrame extends JFrame {
    private static final Logger logger = LoggerFactory.getLogger(StartFrame.class);
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

    /**
     * 初始化登录界面
     */
    public StartFrame() {
        // 加载用户数据 loadUserData() loadPersonData()
        UserDao userDao = new UserDaoImpl();
        usersMap = userDao.loadUserData();
        personsMap = userDao.loadPersonData();

        // 设置窗口信息，布局、标题、大小
        setDefaultLookAndFeelDecorated(true);
        setSize(350, 225);
        setTitle("图书借阅系统");
        // 添加 JLabel 欢迎语
        JLabel titleLabel = new JLabel("-Welcome-", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(titleLabel, 0);
        setLayout(new GridLayout(5, 2, 5, 5));

        // 窗体退出事件 登陆前退出：异常退出，返回-1
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int confirm = JOptionPane.showConfirmDialog(StartFrame.this, "是否确定要退出本系统？", "确认退出", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    logger.info("Exit before login.");
                    System.exit(-1);
                } else{
                    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                }
            }
        });

        // 组件创建以及布局
        // 第一行：用户名输入 userPanel
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel userLabel = new JLabel("账号：");
        usernameField = new JTextField(15);
        userPanel.add(userLabel);
        userPanel.add(usernameField);

        // 第二行：密码输入 passPanel
        JPanel passPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel passLabel = new JLabel("密码：");
        passwordField = new JPasswordField(15);
        passPanel.add(passLabel);
        passPanel.add(passwordField);

        // 第三行：用户类型 userTypePanel
        JPanel userTypePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel userTypeLabel = new JLabel("用户类型:");
        userTypeComboBox = new JComboBox<>(new String[]{"教师", "学生", "管理"});
        rememberMeBox = new JCheckBox("记住密码", true);
        userTypePanel.add(userTypeLabel);
        userTypePanel.add(userTypeComboBox);
        userTypePanel.add(rememberMeBox);

        // 第四行：操作按钮 actionPanel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton loginButton = new JButton("登录");
        actionPanel.add(loginButton);

        // 将四个部分组件添加到窗体中
        add(userPanel);
        add(passPanel);
        add(userTypePanel);
        add(actionPanel);

        // 如果保存了登录信息在配置文件中，则预填充用户名和密码信息
        theme = PreLoader.loadConfig(usernameField, passwordField, userTypeComboBox, rememberMeBox);

        // 按钮点击事件
        loginButton.addActionListener(new LoginButtonListener());

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
                logger.info("Darcula theme enabled");
                updateUI();
                theme = "Darcula";
            }
        });

        lightThemeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FlatLightLaf.setup();
                logger.info("Light theme enabled");
                updateUI();
                theme = "Light";
            }
        });

        darkThemeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FlatDarkLaf.setup();
                logger.info("Dark theme enabled");
                updateUI();
                theme = "Dark";
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
                JOptionPane.showMessageDialog(StartFrame.this, aboutMessage, "关于", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // 将对于系统菜单项添加到关于菜单
        aboutMenu.add(aboutItem);

        // 将设置菜单和对于菜单添加到菜单栏
        menuBar.add(settingsMenu);
        menuBar.add(aboutMenu);

        // 设置菜单栏到窗口
        setJMenuBar(menuBar);

        // 窗口居中
        setLocationRelativeTo(null);
        setVisible(true);

        logger.info("StartFrame initialized successfully.");
    }

    /**
     * 登录按钮监听器
     */
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
                    logger.info("Login successful for user: {}", username);

                    // 保存登录信息 -在登录成功后调用-
                    ConfigManager.saveLoginInfo(username, password, selectedType, rememberMeStatus, theme);

                    // 打开对应用户界面 -在登录成功后调用-
                    openUserInterface(selectedType, person);

                    dispose();
                } else {
                    JOptionPane.showMessageDialog(StartFrame.this, "用户名或密码错误！", "登录失败", JOptionPane.WARNING_MESSAGE);
                    logger.warn("Incorrect username or password for user: {}", username);
                }
            } else {
                JOptionPane.showMessageDialog(StartFrame.this, "用户名不存在！", "登录失败", JOptionPane.WARNING_MESSAGE);
                logger.warn("Username does not exist: {}", username);
            }
        }

        /**
         * 打开对应用户界面
         *
         * @param selectedType 用户类型
         * @param person       用户信息
         */
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
