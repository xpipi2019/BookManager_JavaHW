package pers;

/**
 * @author XPIPI
 */
interface Operation {
    // 借阅图书 在抽象类Student/Teacher中实现方法
    boolean borrowBook(int bookId);

    // 归还图书 在抽象类Student/Teacher中实现方法
    boolean returnBook(int bookId);
}
