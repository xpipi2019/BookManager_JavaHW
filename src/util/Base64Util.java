package util;

import java.util.Base64;

/**
 * @author XPIPI
 */
public class Base64Util {
    public static String encodeBase64(String text) {
        return Base64.getEncoder().encodeToString(text.getBytes());
    }

    public static String decodeBase64(String encodedText) {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedText);
        return new String(decodedBytes);
    }
}
