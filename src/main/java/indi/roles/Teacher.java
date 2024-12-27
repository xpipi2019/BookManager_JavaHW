package main.java.indi.roles;

// 教师类，继承自Person类
public class Teacher extends Person {
    /**
     * 构造函数
     *
     * @param name   教师姓名
     * @param id     教师证件号
     * @param gender 教师性别
     */
    public Teacher(String name, int id, String gender) {
        super(name, id, gender);
    }
}
