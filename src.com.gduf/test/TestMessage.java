package test;

import message.SecureMessage;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import static message.SecureMessage.decode;
import static message.SecureMessage.encode;

public class TestMessage {


    public static void main(String[] args) throws Exception {
//        String encode = encode("RIOT");
//        String decode = decode(encode);
//        System.out.println(decode);
        String a = "jdXbx5wHYN9dbd37ZFYiSA==|n59REfeyengfHx3d5evC3St5a/xzZcnCi1SOVkF2kp8=";
        ByteBuffer byteBuffer = Charset.forName("UTF-8").encode(a);
        String msg = "";
        msg += Charset.forName("UTF-8").decode(byteBuffer);
        System.out.println(msg);
        String decode = decode(msg);
        System.out.println(decode);
    }

}
