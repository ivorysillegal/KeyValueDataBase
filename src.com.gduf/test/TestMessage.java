package test;

import message.SecureMessage;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

public class TestMessage {


    public static void main(String[] args) throws Exception {
        //        初始化报文相关
        // 初始化密钥
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128);
        SecretKey secretKey = keyGen.generateKey();
        // 初始化数字签名密钥
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(2048);

        KeyPair keyPair = keyPairGen.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey aPublic = keyPair.getPublic();
        String msg = "我是你得";
        String encryptedMessage = SecureMessage.createEncryptedMessage(msg, secretKey, privateKey);
        String s = SecureMessage.decodeEncryptedMessage(encryptedMessage, secretKey, keyPair.getPublic());
        System.out.println(s);


    }
}
