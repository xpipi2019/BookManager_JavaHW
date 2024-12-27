package pers.dao;

import pers.frames.User;
import pers.roles.Person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class UserDaoImpl implements UserDao {
    private static final Logger logger = Logger.getLogger(UserDaoImpl.class.getName());

    @Override
    public Map<String, User> loadUserData() {
        Map<String, User> usersMap = new HashMap<>();
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT type, username, password, is_who FROM users");
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                String type = resultSet.getString("type");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String isWho = resultSet.getString("is_who");
                usersMap.put(username, new User(type, username, password, isWho));
            }
        } catch (SQLException e) {
            logger.severe("用户数据从数据库加载失败：" + e.getMessage());
        }
        return usersMap;
    }

    @Override
    public Map<String, Person> loadPersonData() {
        Map<String, Person> personsMap = new HashMap<>();
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT name, id, gender FROM persons");
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                int id = resultSet.getInt("id");
                String gender = resultSet.getString("gender");
                personsMap.put(name, new Person(name, id, gender));
            }
        } catch (SQLException e) {
            logger.severe("人员数据从数据库加载失败：" + e.getMessage());
        }
        return personsMap;
    }
}
