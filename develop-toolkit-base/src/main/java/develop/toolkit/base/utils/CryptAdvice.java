package develop.toolkit.base.utils;

import develop.toolkit.base.exception.CryptException;
import develop.toolkit.base.struct.TwoValues;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ArrayUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * 加密解密增强
 *
 * @author qiushui on 2021-04-23.
 */
public abstract class CryptAdvice {

    /**
     * DES算法
     */
    public static class DES {

        /**
         * 加密
         *
         * @param original  原文
         * @param secretKey 密钥
         * @return 密文
         */
        public static String encrypt(String original, String secretKey) {
            final byte[] data = initCipher(original.getBytes(StandardCharsets.UTF_8), secretKey, Cipher.ENCRYPT_MODE);
            return Base64.getEncoder().encodeToString(data);
        }

        /**
         * 解密
         *
         * @param ciphertext 密文
         * @param secretKey  密钥
         * @return 原文
         */
        public static String decrypt(String ciphertext, String secretKey) {
            final byte[] data = Base64.getDecoder().decode(ciphertext);
            return new String(initCipher(data, secretKey, Cipher.DECRYPT_MODE), StandardCharsets.UTF_8);
        }

        @SneakyThrows(Exception.class)
        private static byte[] initCipher(byte[] data, String secretKey, int mode) {
            final DESKeySpec dks = new DESKeySpec(secretKey.getBytes(StandardCharsets.UTF_8));
            final SecretKey secureKey = SecretKeyFactory.getInstance("DES").generateSecret(dks);
            final Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(mode, secureKey, new SecureRandom());
            return cipher.doFinal(data);
        }
    }

    /**
     * RSA算法
     */
    public static class RSA {

        private static final String KEY_ALGORITHM = "RSA";
        private static final int KEY_SIZE = 1024;
        private static final String SIGNATURE_ALGORITHM = "Sha1WithRSA";

        /**
         * 生成公钥和私钥对
         *
         * @return 公钥 私钥
         */
        @SneakyThrows(NoSuchAlgorithmException.class)
        public static TwoValues<String, String> createRSAKeys() {
            final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
            keyPairGenerator.initialize(KEY_SIZE, new SecureRandom());
            final KeyPair keyPair = keyPairGenerator.generateKeyPair();
            final PublicKey publicKey = keyPair.getPublic();
            final PrivateKey privateKey = keyPair.getPrivate();
            final Base64.Encoder encoder = Base64.getEncoder();
            return TwoValues.of(
                    encoder.encodeToString(publicKey.getEncoded()),
                    encoder.encodeToString(privateKey.getEncoded())
            );
        }

        /**
         * RSA公钥加密
         *
         * @param original        原文
         * @param publicKeyBase64 base64公钥
         * @return 密文
         */
        public static String encrypt(String original, String publicKeyBase64) throws CryptException {
            try {
                final byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyBase64);
                RSAPublicKey pubKey = (RSAPublicKey) KeyFactory
                        .getInstance(KEY_ALGORITHM)
                        .generatePublic(new X509EncodedKeySpec(publicKeyBytes));
                Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
                cipher.init(Cipher.ENCRYPT_MODE, pubKey);
                final byte[] bytes = original.getBytes(StandardCharsets.UTF_8);
                final int offset = 64;
                byte[] enBytes = null;
                for (int i = 0; i < bytes.length; i += offset) {
                    byte[] doFinal = cipher.doFinal(ArrayUtils.subarray(bytes, i, i + 64));
                    enBytes = ArrayUtils.addAll(enBytes, doFinal);
                }
                return Base64.getEncoder().encodeToString(enBytes);
            } catch (Exception e) {
                throw new CryptException(e);
            }
        }

        /**
         * RSA私钥解密
         *
         * @param ciphertext       密文
         * @param privateKeyBase64 base64私钥
         * @return 明文
         */
        public static String decrypt(String ciphertext, String privateKeyBase64) throws CryptException {
            try {
                final Base64.Decoder decoder = Base64.getDecoder();
                final byte[] privateKeyBytes = decoder.decode(privateKeyBase64);
                PrivateKey privateKey = KeyFactory
                        .getInstance(KEY_ALGORITHM)
                        .generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));
                Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
                cipher.init(Cipher.DECRYPT_MODE, privateKey);

                byte[] inputBytes = decoder.decode(ciphertext.getBytes(StandardCharsets.UTF_8));
                StringBuilder sb = new StringBuilder();
                final int offset = 128;
                for (int i = 0; i < inputBytes.length; i += offset) {
                    byte[] doFinal = cipher.doFinal(ArrayUtils.subarray(inputBytes, i, i + offset));
                    sb.append(new String(doFinal));
                }
                return sb.toString();
            } catch (Exception e) {
                throw new CryptException(e);
            }
        }

        /**
         * 生成base64签名结果
         *
         * @param data             数据
         * @param privateKeyBase64 base64私钥
         * @return base64签名
         */
        public static String signature(byte[] data, String privateKeyBase64) {
            try {
                final byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyBase64);
                Signature sign = Signature.getInstance(SIGNATURE_ALGORITHM);
                PrivateKey privateKey = KeyFactory
                        .getInstance(KEY_ALGORITHM)
                        .generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));
                sign.initSign(privateKey);
                sign.update(data);
                return Base64.getEncoder().encodeToString(sign.sign());
            } catch (Exception e) {
                throw new CryptException(e);
            }
        }

        /**
         * 用公钥验证签名
         *
         * @param data            数据
         * @param signatureBase64 base64签名字符串
         * @param publicKeyBase64 base64公钥
         */
        public static boolean verifySignature(byte[] data, String signatureBase64, String publicKeyBase64) {
            try {
                final Base64.Decoder decoder = Base64.getDecoder();
                final byte[] publicKeyBytes = decoder.decode(publicKeyBase64);
                final byte[] signatureBytes = decoder.decode(signatureBase64);
                RSAPublicKey publicKey = (RSAPublicKey) KeyFactory
                        .getInstance(KEY_ALGORITHM)
                        .generatePublic(new X509EncodedKeySpec(publicKeyBytes));
                Signature sign = Signature.getInstance(SIGNATURE_ALGORITHM);
                sign.initVerify(publicKey);
                sign.update(data);
                return sign.verify(signatureBytes);
            } catch (Exception e) {
                return false;
            }
        }
    }
}
