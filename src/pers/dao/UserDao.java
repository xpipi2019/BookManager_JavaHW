package pers.dao;

import pers.frames.User;
import pers.roles.Person;

import java.util.Map;

/**
 * 用户数据访问接口
 */
public interface UserDao {
    /**
     * 从数据库加载用户数据
     * @return 存储用户数据的 Map，键为用户名，值为 User 对象
     */
    Map<String, User> loadUserData();

    /**
     * 从数据库加载人员数据
     * @return 存储人员数据的 Map，键为人员名称，值为 Person 对象
     */
    Map<String, Person> loadPersonData();
}
