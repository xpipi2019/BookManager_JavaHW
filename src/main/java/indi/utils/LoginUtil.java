package main.java.indi.utils;

import main.java.indi.dao.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginUtil {
    private static final Logger logger = LoggerFactory.getLogger(PreLoader.class);
    public static boolean verifyPassword(String username, String enteredPassword) {
        try (Connection connection = DBUtil.getConnection()) {
            String query = "SELECT password, salt FROM users WHERE username = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, username);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        String storedHash = resultSet.getString("password");
                        String storedSalt = resultSet.getString("salt");
                        logger.info("EntedPassword is " + enteredPassword);
                        String enteredHash = hashPassword(enteredPassword, storedSalt); // 使用相同的哈希算法和盐值
                        // System.out.println("storedHash: " + storedHash);
                        // System.out.println("enteredHash: " + enteredHash);
                        return storedHash.equals(enteredHash);
                    }
                }
            }
        } catch (SQLException | NoSuchAlgorithmException e) {
            logger.error("Failed to establish database connection.", e);
            return false;
        }
        return false;
    }

    // SHA-256 哈希函数
    public static String hashPassword(String password, String salt) throws NoSuchAlgorithmException {
        String saltedPassword = password + salt; // 加盐
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(saltedPassword.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hashBytes);
    }

    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16]; // 128 bits salt
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
}
