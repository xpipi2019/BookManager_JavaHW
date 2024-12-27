package pers.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.io.*;

public class ConfigManager {
    private static final Logger logger = LoggerFactory.getLogger(ConfigManager.class);

    // 定义配置文件名常量
    private static final String CONFIG_FILE = "config.ini";

    /**
     * 保存登录信息到配置文件
     * @param username 用户名
     * @param password 密码
     * @param usertype 用户类型
     * @param rememberMe 是否记住密码
     * @param theme 主题设置
     */
    public static void saveLoginInfo(String username, String password, String usertype, boolean rememberMe, String theme) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CONFIG_FILE))) {
            // 写入用户名，如果记住密码则写入实际用户名，否则写入空字符串
            bw.write("username=" + (rememberMe ? username : "") + "\n");
            // 写入密码，如果记住密码则写入实际密码，否则写入空字符串
            bw.write("password=" + (rememberMe ? password : "") + "\n");
            // 写入用户类型
            bw.write("usertype=" + usertype + "\n");
            // 写入是否记住密码的状态
            bw.write("rememberMe=" + rememberMe + "\n");
            // 写入主题设置
            bw.write("theme=" + theme + "\n");
            logger.debug("Saved login info: username={}, rememberMe={}, theme={}", username, rememberMe, theme);
        } catch (IOException e) {
            logger.error("Failed to save login info", e);
            // 显示错误信息对话框
            JOptionPane.showMessageDialog(null, "保存登录信息失败: " + e.getMessage(),
                    "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void saveTheme(String theme) {
        File configFile = new File(CONFIG_FILE);

        String themeTosave = theme;
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
                }
            }

            saveLoginInfo(username, password, usertype, rememberMeStatus, themeTosave);
            logger.debug("Saved theme: {}", themeTosave);
        } catch (IOException e) {
            logger.error("Failed to load config file", e);
            System.out.println("配置文件加载失败: " + e.getMessage());
        }
    }
}
