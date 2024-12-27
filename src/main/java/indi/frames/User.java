package main.java.indi.frames;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// 用户类
public class User {
    private static final Logger logger = LoggerFactory.getLogger(User.class);

    // 用户类型
    private String type;
    // 用户名
    private String username;
    // 密码
    private String password;
    // 对应的用户标识
    private String isWho;


    /**
     * 构造函数
     *
     * @param type     用户类型
     * @param username 用户名
     * @param password 密码
     * @param isWho    用户标识
     */
    public User(String type, String username, String password, String isWho) {
        logger.debug("Creating new User instance with type: {}, username: {}, isWho: {}", type, username, isWho);
        this.setType(type);
        this.setUsername(username);
        this.setPassword(password);
        this.setIsWho(isWho);
    }

    // 获取用户类型
    /**
     * 获取用户类型
     *
     * @return 用户类型
     */
    public String getType() {
        logger.debug("Getting type: {}", type);
        return type;
    }

    // 设置用户类型
    /**
     * 设置用户类型
     *
     * @param type 用户类型
     */
    public void setType(String type) {
        if (type == null || type.isEmpty()) {
            logger.error("Attempt to set type to null or empty");
            throw new IllegalArgumentException("类型不能为空");
        }
        this.type = type;
        logger.debug("Set type to: {}", type);
    }

    // 获取用户名
    /**
     * 获取用户名
     *
     * @return 用户名
     */
    public String getUsername() {
        logger.debug("Getting username: {}", username);
        return username;
    }

    // 设置用户名
    /**
     * 设置用户名
     *
     * @param username 用户名
     */
    public void setUsername(String username) {
        if (username == null || username.isEmpty()) {
            logger.error("Attempt to set username to null or empty");
            throw new IllegalArgumentException("用户名不能为空");
        }
        this.username = username;
        logger.debug("Set username to: {}", username);
    }

    // 获取密码
    /**
     * 获取密码
     *
     * @return 密码
     */
    public String getPassword() {
        logger.debug("Getting password");
        return password;
    }

    // 设置密码
    /**
     * 设置密码
     *
     * @param password 密码
     */
    public void setPassword(String password) {
        if (password == null || password.isEmpty()) {
            logger.error("Attempt to set password to null or empty");
            throw new IllegalArgumentException("密码不能为空");
        }
        this.password = password;
        logger.debug("Set password");
    }


    // 获取用户标识
    /**
     * 获取用户标识
     *
     * @return 用户标识
     */
    public String getIsWho() {
        logger.debug("Getting isWho: {}", isWho);
        return isWho;
    }

    // 设置用户标识
    /**
     * 设置用户标识
     *
     * @param isWho 用户标识
     */
    public void setIsWho(String isWho) {
        if (isWho == null || isWho.isEmpty()) {
            logger.error("Attempt to set isWho to null or empty");
            throw new IllegalArgumentException("用户标识不能为空");
        }
        this.isWho = isWho;
        logger.debug("Set isWho to: {}", isWho);
    }

}
