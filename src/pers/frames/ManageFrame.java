package pers.frames;

import pers.Student;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author XPIPI
 */
// 界面四：管理界面 ManageFrame
public class ManageFrame extends JFrame {
    private Student student;

    public ManageFrame(Student student) {
        this.student = student;

        // 设置窗口信息
        setTitle("图书借阅系统--管理系统");
        setLayout(new BorderLayout());

        // 窗体退出事件 登陆后退出：正常退出，返回0
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        // 设置窗口属性
        setSize(400, 300);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}