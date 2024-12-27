package pers.frames;

// 用户类
public class User {
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
            throw new IllegalArgumentException("类型不能为空");
        }
        this.type = type;
    }

    // 获取用户名
    /**
     * 获取用户名
     *
     * @return 用户名
     */
    public String getUsername() {
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
            throw new IllegalArgumentException("用户名不能为空");
        }
        this.username = username;
    }

    // 获取密码
    /**
     * 获取密码
     *
     * @return 密码
     */
    public String getPassword() {
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
            throw new IllegalArgumentException("密码不能为空");
        }
        this.password = password;
    }

    // 获取用户标识
    /**
     * 获取用户标识
     *
     * @return 用户标识
     */
    public String getIsWho() {
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
            throw new IllegalArgumentException("用户标识不能为空");
        }
        this.isWho = isWho;
    }
}
