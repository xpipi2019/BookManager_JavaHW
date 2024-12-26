package pers.dao;

import pers.frames.User;
import pers.roles.Person;

import java.util.Map;

public interface UserDao {
    Map<String, User> loadUserData();
    Map<String, Person> loadPersonData();
}