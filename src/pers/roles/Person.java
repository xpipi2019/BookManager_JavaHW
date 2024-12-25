package pers.roles;
import pers.dao.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * @author XPIPI
 */
// 抽象类：Person
public class Person {
    // 性别 name
    private String name;

    // 证件号 id
    private int id;

    // 性别 gender
    private String gender;

    // 存放已借阅书籍数量
    private int borrowedBooksCount;

    // 初始化构造函数 只在这个包使用
    public Person(String name, int id, String gender) {
        this.name = name;
        this.id = id;
        this.gender = gender;
    }

    // 返回人的属性的函数 以及修改人的属性的函数

    public String getName() {
        return name;
    }

    public int getId() { return id; }

    public String getGender() {
        return gender;
    }

    public int getBorrowedBooksCount() {
        return borrowedBooksCount;
    }

    public void setBorrowedBooksCount(int borrowedBooksCount) {
        this.borrowedBooksCount = borrowedBooksCount;
    }

    // 通过id来返回名字getNameById
    public String getNameById(int id) {
        String name = "无";
        String sql = "SELECT name FROM persons WHERE id =?";
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    name = resultSet.getString("name");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return name;
    }
}
