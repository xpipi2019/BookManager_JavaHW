package main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.GUI;

public class main {
    private static final Logger logger = LoggerFactory.getLogger(main.class);

    public static void main(String[] args) {
        logger.info("Starting the Book Manager application.");
        // 调用GUI方法
        new GUI();
        logger.info("GUI initialized successfully.");
    }
}
    