package pers;

import pers.frames.*;

import javax.swing.*;
/**
 * @author XPIPI
 */
public class GUI {
    public GUI(){
        /*
            SwingUtilities.invokeLater()方法
            使事件派发线程上的可运行对象排队 当可运行对象排在事件派发队列的队首时，就调用其run方法。其效果是允许事件派发线程调用另一个线程中的任意一个代码
         */
        SwingUtilities.invokeLater(() -> new StartFrame());
    }
}
