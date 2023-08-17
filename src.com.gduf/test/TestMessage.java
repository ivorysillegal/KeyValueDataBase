package test;

import message.SecureMessage;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import static message.SecureMessage.decode;
import static message.SecureMessage.encode;

public class TestMessage {


    public static void main(String[] args) throws Exception {
        String encode = encode("RIOT");
        String decode = decode(encode);
        System.out.println(decode);
    }
}
