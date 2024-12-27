package pers.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    // 数据库连接URL，根据实际情况修改主机地址、端口号（默认1521）和服务名（SID）
    private static final String URL = "jdbc:oracle:thin:@//39.106.47.167:1521/snorcl11g";
    // 数据库用户名
    private static final String USERNAME = "scott";
    // 数据库密码
    private static final String PASSWORD = "tiger";

    static {
        try {
            // 加载Oracle JDBC驱动
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取数据库连接
     * @return 数据库连接对象
     * @throws SQLException 如果连接失败，抛出 SQLException
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}
