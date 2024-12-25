package pers.util;

import pers.Book;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author XPIPI
 */
public class ReadBookDat {
    public static void main(String[] args) {
        final String BOOK_FILE = "books.dat";

        ArrayList<Book> books = new ArrayList<>();

        try {
            // 解密并读取书籍数据
            books = Base64Util.readDecryptedFile(BOOK_FILE);
        } catch (IOException e) {
            System.err.println("读取文件失败: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("反序列化失败: " + e.getMessage());
        }

        // 打印书籍信息
        if (books.isEmpty()) {
            System.out.println("书籍列表为空！");
        } else {
            System.out.println("书籍列表：");
            for (Book book : books) {
                System.out.println(book);
            }
        }
    }
}
