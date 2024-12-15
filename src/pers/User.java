package pers;

public class User {
    private String type;
    private String username;
    private String password;

    public User(String type, String username, String password) {
        this.type = type;
        this.username = username;
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}