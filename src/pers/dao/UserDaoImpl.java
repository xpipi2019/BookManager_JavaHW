package pers.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.frames.User;
import pers.roles.Person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class UserDaoImpl implements UserDao {
    private static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

    /**
     * 从数据库加载用户数据
     * @return 存储用户数据的 Map，键为用户名，值为 User 对象
     */
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
            logger.info("User data loaded successfully.");
        } catch (SQLException e) {
            logger.error("Failed to load user data from the database.", e);
        }
        return usersMap;
    }

    /**
     * 从数据库加载人员数据
     * @return 存储人员数据的 Map，键为人员名称，值为 Person 对象
     */
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
            logger.info("Person data loaded successfully.");
        } catch (SQLException e) {
            logger.error("Failed to load person data from the database.", e);
        }
        return personsMap;
    }
}
