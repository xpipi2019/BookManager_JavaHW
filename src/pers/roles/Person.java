package pers.roles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.dao.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// 抽象类：Person
public class Person {
    private static final Logger logger = LoggerFactory.getLogger(Person.class);

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
        logger.debug("Creating new Person instance with name: {}, id: {}, gender: {}", name, id, gender);
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
        logger.debug("Getting name: {}", name);
        return name;
    }

    /**
     * 设置姓名
     *
     * @param name 姓名
     */
    public void setName(String name) {
        logger.debug("Setting name to: {}", name);
        this.name = name;
    }

    /**
     * 获取证件号
     *
     * @return 证件号
     */
    public int getId() {
        logger.debug("Getting id: {}", id);
        return id;
    }

    /**
     * 设置证件号
     *
     * @param id 证件号
     */
    public void setId(int id) {
        logger.debug("Setting id to: {}", id);
        this.id = id;
    }

    /**
     * 获取性别
     *
     * @return 性别
     */
    public String getGender() {
        logger.debug("Getting gender: {}", gender);
        return gender;
    }

    /**
     * 设置性别
     *
     * @param gender 性别
     */
    public void setGender(String gender) {
        logger.debug("Setting gender to: {}", gender);
        this.gender = gender;
    }

    /**
     * 获取已借阅书籍数量
     *
     * @return 已借阅书籍数量
     */
    public int getBorrowedBooksCount() {
        logger.debug("Getting borrowedBooksCount: {}", borrowedBooksCount);
        return borrowedBooksCount;
    }

    /**
     * 设置已借阅书籍数量
     *
     * @param borrowedBooksCount 已借阅书籍数量
     */
    public void setBorrowedBooksCount(int borrowedBooksCount) {
        logger.debug("Setting borrowedBooksCount to: {}", borrowedBooksCount);
        this.borrowedBooksCount = borrowedBooksCount;
    }

    /**
     * 通过ID获取姓名
     *
     * @param id 证件号
     * @return 姓名
     */
    public String getNameById(int id) {
        logger.debug("Getting name by id: {}", id);
        String name = "无";
        String sql = "SELECT name FROM persons WHERE id =?";
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    name = resultSet.getString("name");
                    logger.debug("Found name: {} for id: {}", name, id);
                } else {
                    logger.debug("No name found for id: {}", id);
                }
            }
        } catch (SQLException e) {
            logger.error("SQL error while getting name by id: {}", id, e);
        }
        return name;
    }
}
