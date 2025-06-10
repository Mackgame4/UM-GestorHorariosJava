package Utils;

import java.util.Base64;

public class DataEncoder {
    public static String encode(String data) {
        return Base64.getEncoder().encodeToString(data.getBytes());
    }

    public static String decode(String data) {
        byte[] decodedBytes = Base64.getDecoder().decode(data);
        return new String(decodedBytes);
    }
}
