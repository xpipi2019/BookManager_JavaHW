package pers.frames;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigManager {
    private static final String CONFIG_FILE = "config.ini";

    public static void saveLoginInfo(String username, String password, String usertype, boolean rememberMe, String theme) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CONFIG_FILE))) {
            bw.write("username=" + (rememberMe ? username : "") + "\n");
            bw.write("password=" + (rememberMe ? password : "") + "\n");
            bw.write("usertype=" + usertype + "\n");
            bw.write("rememberMe=" + rememberMe + "\n");
            bw.write("theme=" + theme + "\n");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "保存登录信息失败: " + e.getMessage(),
                    "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
}
