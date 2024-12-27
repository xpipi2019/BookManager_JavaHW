package pers.roles;

import pers.dao.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// 抽象类：Person
public class Person {
    // 姓名
    private String name;

    // 证件号
    private int id;

    // 性别
    private String gender;

    // 存放已借阅书籍数量
    private int borrowedBooksCount;

    /**
     * 初始化构造函数 只在这个包使用
     *
     * @param name   姓名
     * @param id     证件号
     * @param gender 性别
     */
    public Person(String name, int id, String gender) {
        this.name = name;
        this.id = id;
        this.gender = gender;
    }

    // 返回人的属性的函数 以及修改人的属性的函数

    /**
     * 获取姓名
     *
     * @return 姓名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置姓名
     *
     * @param name 姓名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取证件号
     *
     * @return 证件号
     */
    public int getId() {
        return id;
    }

    /**
     * 设置证件号
     *
     * @param id 证件号
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * 获取性别
     *
     * @return 性别
     */
    public String getGender() {
        return gender;
    }

    /**
     * 设置性别
     *
     * @param gender 性别
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * 获取已借阅书籍数量
     *
     * @return 已借阅书籍数量
     */
    public int getBorrowedBooksCount() {
        return borrowedBooksCount;
    }

    /**
     * 设置已借阅书籍数量
     *
     * @param borrowedBooksCount 已借阅书籍数量
     */
    public void setBorrowedBooksCount(int borrowedBooksCount) {
        this.borrowedBooksCount = borrowedBooksCount;
    }

    /**
     * 通过ID获取姓名
     *
     * @param id 证件号
     * @return 姓名
     */
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
