package develop.toolkit.base.utils;

import lombok.SneakyThrows;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * @author qiushui on 2021-04-23.
 */
public abstract class CryptAdvice {

    public static String encryptDES(String original, String secretKey) {
        final byte[] data = initCipher(original.getBytes(StandardCharsets.UTF_8), secretKey, Cipher.ENCRYPT_MODE);
        return Base64.getEncoder().encodeToString(data);
    }

    public static String decryptDES(String cryptograph, String secretKey) {
        final byte[] data = Base64.getDecoder().decode(cryptograph);
        return new String(initCipher(data, secretKey, Cipher.DECRYPT_MODE), StandardCharsets.UTF_8);
    }

    @SneakyThrows(Exception.class)
    private static byte[] initCipher(byte[] data, String secretKey, int mode) {
        final String algorithm = "DES";
        SecureRandom random = new SecureRandom();
        DESKeySpec dks = new DESKeySpec(secretKey.getBytes(StandardCharsets.UTF_8));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm);
        SecretKey secureKey = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(mode, secureKey, random);
        return cipher.doFinal(data);
    }
}
