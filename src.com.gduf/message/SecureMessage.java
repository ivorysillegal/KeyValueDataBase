package message;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Base64;

public class SecureMessage {
    private static String encrypt(String message, String key) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] encryptedBytes = cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    private static String decrypt(String encryptedMessage, String key) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedMessage);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    private static String generateSignature(String message, String key) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hmacKeyBytes = secretKeySpec.getEncoded();
        byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
        byte[] signatureBytes = digest.digest(hmacKeyBytes);
        return Base64.getEncoder().encodeToString(signatureBytes);
    }

    private static String wrapMessage(String encryptedMessage, String signature) {
        return encryptedMessage + "|" + signature;
    }

    private static String[] unwrapAndVerify(String wrappedMessage, String key) throws Exception {
        String[] parts = wrappedMessage.split("\\|");
        String encryptedMessage = parts[0];
        String signature = parts[1];

        String calculatedSignature = generateSignature(encryptedMessage, key);
        if (!signature.equals(calculatedSignature)) {
            throw new Exception("Signature verification failed");
        }

        return new String[]{encryptedMessage, signature};
    }


    public static void main(String[] args) throws Exception {
        String encode = encode("我是你爹");
        String decode = decode(encode);
        System.out.println(decode);
    }

    public static String encode(String originalMessage) throws Exception {
        String key = "0123456789abcdef";
        // 加密和签名
        String encryptedMessage = encrypt(originalMessage, key);
        String signature = generateSignature(encryptedMessage, key);
        // 包装为标准报文格式
        String wrappedMessage = wrapMessage(encryptedMessage, signature);
        return wrappedMessage;
    }

    public static String decode(String wrappedMessage) throws Exception {
        // 解析报文并验证签名和解密
        String key = "0123456789abcdef";
        String[] unwrapped = unwrapAndVerify(wrappedMessage, key);
        String decryptedMessage = decrypt(unwrapped[0], key);
        return decryptedMessage;
    }

}
