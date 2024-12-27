package pers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.frames.StartFrame;

import javax.swing.*;

import static pers.utils.PreLoader.loadTheme;

// 主GUI类
public class GUI {
    private static final Logger logger = LoggerFactory.getLogger(GUI.class);

    public GUI() {
        // 加载主题
        loadTheme();
        logger.debug("GUI Theme loaded (while start)");

        /*
            SwingUtilities.invokeLater()方法
            使事件派发线程上的可运行对象排队 当可运行对象排在事件派发队列的队首时，就调用其run方法。其效果是允许事件派发线程调用另一个线程中的任意一个代码
         */
        SwingUtilities.invokeLater(() -> {
            new StartFrame();
            logger.debug("StartFrame created and invoked");
        });
    }
}
