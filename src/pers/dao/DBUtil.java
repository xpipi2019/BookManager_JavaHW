package pers.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBUtil {
    // 数据库连接URL，根据实际情况修改主机地址、端口号（默认1521）和服务名（SID）
    private static final String URL = "jdbc:oracle:thin:@//39.106.47.167:1521/snorcl11g";
    // 数据库用户名
    private static final String USERNAME = "scott";
    // 数据库密码
    private static final String PASSWORD = "tiger";

    private static final Logger logger = LoggerFactory.getLogger(DBUtil.class);

    static {
        try {
            // 加载Oracle JDBC驱动
            Class.forName("oracle.jdbc.driver.OracleDriver");
            logger.info("Oracle JDBC Driver loaded successfully.");
        } catch (ClassNotFoundException e) {
            logger.error("Failed to load Oracle JDBC Driver.", e);
        }
    }

    /**
     * 获取数据库连接
     *
     * @return 数据库连接对象
     * @throws SQLException 如果连接失败，抛出 SQLException
     */
    public static Connection getConnection() throws SQLException {
        try {
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            logger.info("Database connection established successfully.");
            return connection;
        } catch (SQLException e) {
            logger.error("Failed to establish database connection.", e);
            throw e;
        }
    }
}
