package main.java;

import main.java.indi.dao.DBUtil;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

public class PasswordMigration {

    public static void migratePasswords(Connection connection) throws SQLException, NoSuchAlgorithmException {
        String selectQuery = "SELECT username, password FROM users";
        String updateQuery = "UPDATE users SET password = ?, salt = ? WHERE username = ?";

        try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
             ResultSet resultSet = selectStatement.executeQuery();
             PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {

            while (resultSet.next()) {
                String username = resultSet.getString("username");
                String plainPassword = resultSet.getString("password");

                // 生成盐
                String salt = generateSalt();

                // 哈希密码
                String hashedPassword = hashPassword(plainPassword, salt);

                // 更新数据库
                updateStatement.setString(1, hashedPassword);
                updateStatement.setString(2, salt);
                updateStatement.setString(3, username);
                updateStatement.executeUpdate();
            }
            connection.commit();//提交事务
        }
    }

    // 哈希函数（与之前的示例相同）
    public static String hashPassword(String password, String salt) throws NoSuchAlgorithmException {
        String saltedPassword = password + salt; // 加盐
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(saltedPassword.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hashBytes);
    }

    // 生成盐（与之前的示例相同）
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16]; // 128 bits salt
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public static void main(String[] args) throws SQLException, NoSuchAlgorithmException {
        Connection connection = DBUtil.getConnection();//获取数据库连接
        migratePasswords(connection);
        System.out.println("密码迁移完成！");
    }
}