package pers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * @author XPIPI
 */
public class GUI {
    // 创建JFrame对象
    private JFrame frame;
    // 创建bookList书籍对象
    private ArrayList<Book> bookList;
    // 定义书籍档案文件名(final常量)
    private static final String FILE_NAME = "books.txt";

    // 窗口显示方法 GUI()
    public GUI() {
        // 主窗口设定
        frame = new JFrame("图书借阅系统");
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout(10, 10));

        // 窗体退出事件
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });


        // 显示窗口
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
