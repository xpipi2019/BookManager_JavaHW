package pers.util;

import pers.Book;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author XPIPI
 */
public class CreateBookDat {
    public static void main(String[] args) {

        final String BOOK_FILE = "books.dat";

        ArrayList<Book> books = new ArrayList<Book>();

        Book book1 = new Book(1,"Java","Tang","BookType1");
        Book book2 = new Book(2,"DataStructure","Wang","BookType2");

        books.add(book1);
        books.add(book2);

        try {
            // 加密并写入书籍数据
            Base64Util.writeEncryptedFile(BOOK_FILE,books);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
