import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Cryptographer {


    public static String getMD5String(String password) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("MD5");
            digest.reset();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        digest.update(password.getBytes());
        byte[] code = digest.digest();
        BigInteger bigInt = new BigInteger(1, code);
        String hashtext = bigInt.toString(16);
        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }
        return hashtext;
    }
    public static String getSalt(){
        SecureRandom random = new SecureRandom();
        byte[] code = random.generateSeed(16);
        BigInteger bigInt = new BigInteger(1, code);
        String salt = bigInt.toString(16);
        return salt;
    }

}
