package develop.toolkit.base.utils;

import develop.toolkit.base.struct.TwoValues;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * RSA加密
 *
 * @author qiushui on 2021-07-20.
 */
public abstract class RSAAdvice {

    private static final String KEY_ALGORITHM = "RSA";
    private static final int KEY_SIZE = 1024;

    /**
     * 生成公钥和私钥对
     */
    public static TwoValues<String, String> createRSAKeys() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
            keyPairGenerator.initialize(KEY_SIZE, new SecureRandom());
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();
            final Base64.Encoder encoder = Base64.getEncoder();
            String publicKeyValue = encoder.encodeToString(publicKey.getEncoded());
            String privateKeyValue = encoder.encodeToString(privateKey.getEncoded());
            return TwoValues.of(publicKeyValue, privateKeyValue);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * RSA公钥加密
     *
     * @param original  原文
     * @param publicKey 公钥
     * @return 密文
     */
    public static String encrypt(String original, String publicKey) {
        try {
            RSAPublicKey pubKey = (RSAPublicKey) KeyFactory
                    .getInstance(KEY_ALGORITHM)
                    .generatePublic(
                            new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey))
                    );
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(original.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * RSA私钥解密
     *
     * @param encryptString 加密字符串
     * @param privateKey    私钥
     * @return 铭文
     */
    public static String decrypt(String encryptString, String privateKey) throws Exception {
        final Base64.Decoder decoder = Base64.getDecoder();
        byte[] inputByte = decoder.decode(encryptString.getBytes(StandardCharsets.UTF_8));
        PrivateKey priKey = KeyFactory
                .getInstance(KEY_ALGORITHM)
                .generatePrivate(new PKCS8EncodedKeySpec(decoder.decode(privateKey)));
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        return new String(cipher.doFinal(inputByte));
    }
}
