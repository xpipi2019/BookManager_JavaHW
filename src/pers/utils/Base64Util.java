package pers.utils;
import pers.Book;
import java.io.*;
import java.util.ArrayList;
import java.util.Base64;

public class Base64Util {

    // 加密并写入文件
    public static void writeEncryptedFile(String fileName, ArrayList<Book> books) throws IOException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
             FileWriter fileWriter = new FileWriter(fileName)) {

            // 将对象写入字节数组输出流
            objectOutputStream.writeObject(books);
            objectOutputStream.flush();

            // 使用 Base64 加密字节数组
            String encodedData = Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());

            // 将加密后的数据写入文件
            fileWriter.write(encodedData);
        }
    }

    // 从文件读取并解密
    @SuppressWarnings("unchecked")
    public static ArrayList<Book> readDecryptedFile(String fileName) throws IOException, ClassNotFoundException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {

            // 读取文件中的 Base64 数据
            String encodedData = bufferedReader.readLine();

            // 解密 Base64 数据为字节数组
            byte[] decodedData = Base64.getDecoder().decode(encodedData);

            // 将字节数组反序列化为对象
            try (ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(decodedData))) {
                return (ArrayList<Book>) objectInputStream.readObject();
            }
        }
    }
}