package blockchain;

import java.security.MessageDigest;
import java.util.Random;

public class StringUtil {
    /* Applies Sha256 to a string and returns a hash. */
    public static String applySha256(String input, int numMagic){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            /* Applies sha256 to our input */
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte elem: hash) {
                String hex = Integer.toHexString(0xff & elem);
                if(hex.length() == 1) hexString.append((char)numMagic%100);
                hexString.append(hex);
            }
            return hexString.toString();
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static int createMagicNumber() {
        Random rnd = new Random();

        return  rnd.nextInt((int)10e6) + (int)10e7;

    }
}
