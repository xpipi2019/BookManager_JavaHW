package pers.roles;

/**
 * @author XPIPI
 */
public class Student extends Person {
    // 匹配父类Person的super构造方法
    public Student(String name, int id, String gender) {
        super(name, id, gender);
    }
}
