package pers;

import java.util.ArrayList;

/**
 * @author XPIPI
 */
public class Student extends Person implements Operation {
    // 数组ArrayList 存放已借阅书籍信息
    private ArrayList<Integer> borrowedBooks;
    // 定义学生最大可借阅书数量（final常量）
    private static final int MAX_BORROW_LIMIT = 3;

    // 匹配父类Person的super构造方法
    public Student(String name, int id, String gender) {
        super(name, id, gender);
        this.borrowedBooks = new ArrayList<>();
    }

    @Override
    public boolean borrowBook(int bookId) {
        return false;
    }

    @Override
    public boolean returnBook(int bookId) {
        return false;
    }

}
