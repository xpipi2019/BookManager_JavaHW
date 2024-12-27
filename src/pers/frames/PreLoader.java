package pers.frames;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.io.*;

/**
 * @author XPIPI
 */
public class PreLoader {
    // 定义配置文件名常量 String
    // 用来保存一些功能设置，在每次打开时加载
    private static final String CONFIG_FILE = "config.ini";

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
                } else if (line.startsWith("theme=")){
                    if (line.substring(line.indexOf('=') + 1).trim().equals("Dark")){
                        FlatDarkLaf.setup();
                        theme = "Dark";
                        System.out.println("配置主题为Dark");
                    } else if(line.substring(line.indexOf('=') + 1).trim().equals("Light")){
                        FlatLightLaf.setup();
                        theme = "Light";
                        System.out.println("配置主题为Light");
                    } else{
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

    public static void loadTheme(){
        File configFile = new File(CONFIG_FILE);
        try (BufferedReader br = new BufferedReader(new FileReader(configFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("theme=")){
                    if (line.substring(line.indexOf('=') + 1).trim().equals("Dark")){
                        FlatDarkLaf.setup();
                        System.out.println("配置主题为Dark");
                    } else if(line.substring(line.indexOf('=') + 1).trim().equals("Light")){
                        FlatLightLaf.setup();
                        System.out.println("配置主题为Light");
                    } else{
                        FlatDarculaLaf.setup();
                        System.out.println("配置主题为Darcula");
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("配置文件加载失败: " + e.getMessage());
        }
    }
}
