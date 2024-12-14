package pers;

/**
 * @author XPIPI
 */
// 抽象类：Person
abstract class Person {
    // 性别 String
    private String name;

    // 证件号 int
    private int id;

    // 性别 String
    private String gender;

    // 初始化构造函数 只在这个包使用
    Person(String name, int id, String gender) {
        this.name = name;
        this.id = id;
        this.gender = gender;
    }

    // 返回人的属性的函数 以及修改人的属性的函数 只在这个包使用

    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }

    String getGender() {
        return gender;
    }

    void setGender(String gender) {
        this.gender = gender;
    }
}
