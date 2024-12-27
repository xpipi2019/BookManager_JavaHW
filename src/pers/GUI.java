package pers;

import pers.frames.StartFrame;

import javax.swing.*;

import static pers.utils.PreLoader.loadTheme;

// 主GUI类
public class GUI {
    public GUI(){
        // 加载主题
        loadTheme();
        /*
            SwingUtilities.invokeLater()方法
            使事件派发线程上的可运行对象排队 当可运行对象排在事件派发队列的队首时，就调用其run方法。其效果是允许事件派发线程调用另一个线程中的任意一个代码
         */
        SwingUtilities.invokeLater(() -> new StartFrame());
    }
}
