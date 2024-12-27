package main.java.indi.roles;

// 学生类，继承自Person类
public class Student extends Person {
    /**
     * 构造函数
     *
     * @param name   学生姓名
     * @param id     学生证件号
     * @param gender 学生性别
     */
    public Student(String name, int id, String gender) {
        super(name, id, gender);
    }
}
