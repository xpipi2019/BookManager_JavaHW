package pers.dao;

import pers.frames.User;
import pers.roles.Person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class UserDaoImpl implements UserDao {
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
            // 这里可以考虑抛出自定义异常或者使用日志框架记录异常，便于统一处理
            e.printStackTrace();
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
            e.printStackTrace();
        }
        return personsMap;
    }
}