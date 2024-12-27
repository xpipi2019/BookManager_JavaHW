package pers.utils;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class PreLoader {
    // 定义配置文件名常量 String
    // 用来保存一些功能设置，在每次打开时加载
    private static final String CONFIG_FILE = "config.ini";

    /**
     * 加载配置文件中的设置，并填充到相应的组件中
     * @param usernameField 用户名输入框
     * @param passwordField 密码输入框
     * @param userTypeComboBox 用户类型选择框
     * @param rememberMeBox 记住密码复选框
     * @return 加载的主题名称
     */
    public static String loadConfig(JTextField usernameField, JPasswordField passwordField, JComboBox<String> userTypeComboBox, JCheckBox rememberMeBox) {
        String theme = "";
        File configFile = new File(CONFIG_FILE);

        // 初次启动，无配置文件
        if (!configFile.exists()) {
            return "Light";
        }

        try (BufferedReader br = new BufferedReader(new FileReader(configFile))) {
            String line;
            String username = "";
            String password = "";
            String usertype = "";

            boolean rememberMeStatus = false;

            // BufferedReader文件流 读取配置文件内容
            while ((line = br.readLine()) != null) {
                if (line.startsWith("username=")) {
                    username = line.substring(line.indexOf('=') + 1).trim();
                } else if (line.startsWith("password=")) {
                    password = line.substring(line.indexOf('=') + 1).trim();
                } else if (line.startsWith("usertype=")) {
                    usertype = line.substring(line.indexOf('=') + 1).trim();
                } else if (line.startsWith("rememberMe=")) {
                    rememberMeStatus = Boolean.parseBoolean(line.substring(line.indexOf('=') + 1).trim());
                } else if (line.startsWith("theme=")) {
                    String themeValue = line.substring(line.indexOf('=') + 1).trim();
                    if (themeValue.equals("Dark")) {
                        FlatDarkLaf.setup();
                        theme = "Dark";
                        System.out.println("配置主题为Dark");
                    } else if (themeValue.equals("Light")) {
                        FlatLightLaf.setup();
                        theme = "Light";
                        System.out.println("配置主题为Light");
                    } else {
                        FlatDarculaLaf.setup();
                        theme = "Darcula";
                        System.out.println("配置主题为Darcula");
                    }
                }
            }

            // 填充配置文件内容
            usernameField.setText(username);
            passwordField.setText(password);
            userTypeComboBox.setSelectedItem(usertype);
            rememberMeBox.setSelected(rememberMeStatus);

        } catch (IOException e) {
            System.out.println("配置文件加载失败: " + e.getMessage());
        }
        return theme;
    }

    /**
     * 仅加载配置文件中的主题设置
     */
    public static void loadTheme() {
        File configFile = new File(CONFIG_FILE);

        // 初次启动，无配置文件
        if (!configFile.exists()) {
            FlatLightLaf.setup();
        }

        CustomizingTheme();

        try (BufferedReader br = new BufferedReader(new FileReader(configFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("theme=")) {
                    String themeValue = line.substring(line.indexOf('=') + 1).trim();
                    if (themeValue.equals("Dark")) {
                        FlatDarkLaf.setup();
                        System.out.println("配置主题为Dark");
                    } else if (themeValue.equals("Light")) {
                        FlatLightLaf.setup();
                        System.out.println("配置主题为Light");
                    } else {
                        FlatDarculaLaf.setup();
                        System.out.println("配置主题为Darcula");
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("配置文件加载失败: " + e.getMessage());
        }
    }

    private static void CustomizingTheme() {
    // 设置按钮的圆角半径为 999，使其接近圆形
    UIManager.put("Button.arc", 999);
    // 设置组件的圆角半径为 999，使其接近圆形
    UIManager.put("Component.arc", 999);
    // 设置进度条的圆角半径为 999，使其接近圆形
    UIManager.put("ProgressBar.arc", 999);
    // 设置文本组件的圆角半径为 999，使其接近圆形
    UIManager.put("TextComponent.arc", 999);

    // 设置组件的箭头类型为三角形
    UIManager.put("Component.arrowType", "triangle");

    // 设置滚动条轨道的圆角半径为 999，使其接近圆形
    UIManager.put("ScrollBar.trackArc", 999);
    // 设置滚动条滑块的圆角半径为 999，使其接近圆形
    UIManager.put("ScrollBar.thumbArc", 999);
    // 设置滚动条轨道的内边距
    UIManager.put("ScrollBar.trackInsets", new Insets(2, 4, 2, 4));
    // 设置滚动条滑块的内边距
    UIManager.put("ScrollBar.thumbInsets", new Insets(2, 2, 2, 2));
    // 设置滚动条轨道的颜色
    UIManager.put("ScrollBar.track", new Color(0xe0e0e0));
}

}
