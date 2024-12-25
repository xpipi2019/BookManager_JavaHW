package pers.frames;

/**
 * @author XPIPI
 */
public class User {
    // 用户类型 type
    private String type;
    // 用户名 username
    private String username;
    // 密码 password
    private String password;
    // 对应了谁 isWho
    private String isWho;

    public User(String type, String username, String password, String isWho) {
        this.type = type;
        this.username = username;
        this.password = password;
        this.isWho = isWho;
    }

    String getType() {
        return type;
    }

    String getUsername() {
        return username;
    }

    String getPassword() {
        return password;
    }

    String getIsWho() {
        return isWho;
    }
}