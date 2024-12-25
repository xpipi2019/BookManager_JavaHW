package pers.dao;
import java.util.Map;
import pers.frames.User;
import pers.roles.Person;

public interface UserDao {
    Map<String, User> loadUserData();
    Map<String, Person> loadPersonData();
}