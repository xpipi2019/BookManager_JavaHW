package pers.frames;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigManager {
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
        } catch (IOException e) {
            // 显示错误信息对话框
            JOptionPane.showMessageDialog(null, "保存登录信息失败: " + e.getMessage(),
                    "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
}
