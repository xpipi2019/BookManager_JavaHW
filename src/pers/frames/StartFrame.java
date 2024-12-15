package pers.frames;

import pers.Student;
import pers.Teacher;
import pers.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

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
    // 记住密码选择框 JCheckBox
    private JCheckBox rememberMeBox;
    // 定义配置文件名常量 String
    // 用来保存一些功能设置，在每次打开时加载
    private final String CONFIG_FILE = "config.ini";

    // 初始化登录界面
    public StartFrame() {
        // 加载用户数据 loadUserData()
        loadUserData();

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
        userTypeComboBox = new JComboBox<>(new String[]{"教师", "学生"});
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
        usersMap = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String type = parts[0];
                    String username = parts[1];
                    String password = parts[2];
                    usersMap.put(username, new User(type, username, password));
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "用户数据文件加载失败: " + e.getMessage(),
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
        // BufferedWriter文件流 写入配置文件内容
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CONFIG_FILE))) {
            bw.write("username=" + (rememberMe ? username : "") + "\n");
            bw.write("password=" + (rememberMe ? password : "") + "\n");
            bw.write("rememberMe=" + rememberMe + "\n");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "保存登录信息失败: " + e.getMessage(),
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
                User user = usersMap.get(username);
                if (user.getPassword().equals(password) && user.getType().equals(selectedType)) {
                    JOptionPane.showMessageDialog(StartFrame.this, "登录成功！欢迎 " + user.getType() + " " + username);

                    // 保存登录信息 -在登录成功后调用-
                    saveLoginInfo(username, password, rememberMeStatus);

                    // 打开对应用户界面 -在登录成功后调用-
                    if (selectedType.equals("教师")) {
                        new TeacherFrame(new Teacher(username, 1001, "男"));
                    } else {
                        new StudentFrame(new Student(username, 1002, "女"));
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
                JOptionPane.showMessageDialog(StartFrame.this, "用户名不存在！", "登录失败", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
}