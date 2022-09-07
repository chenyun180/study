package com.cloud.common.utils.decrypt;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.UUID;

/**
 *  @author: chenyun
 *  @Date: 2020/6/27 9:50
 *  @Description: AES算法：对称加密。密钥的长度有限制，工具类中生成的密钥为16位，位数不对会报错
 */
public class AesUtils {

    //决定了具体加解密的的实现。算法：AES；加密模式：ESB；填充方式：PKCS5Padding
    //常见的填充方式还有：PKCS7Padding、NoPadding。两端的填充方式也需要相同
    private static final String ALGORITHMSTR = "AES/ECB/PKCS5Padding";

    /**
     *  加密方法
     */
    public static String aesEncrypt(String content, String encryptKey) throws Exception {
        return base64Encode(aesEncryptToBytes(content, encryptKey));
    }

    /**
     * 解密方法
     */
    public static String aesDecrypt(String encryptStr, String decryptKey) throws Exception {
        return aesDecryptByBytes(base64Decode(encryptStr), decryptKey);
    }

    /**
     * 生成密钥
     */
    public static String getEncryptKey(){
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("DES");
            keyGenerator.init(56);
            // 生成一个Key
            SecretKey generateKey = keyGenerator.generateKey();
            // 转变为字节数组
            byte[] encoded = generateKey.getEncoded();
            // 生成密钥字符串
            String encodeHexString = Hex.encodeHexString(encoded);
            return encodeHexString;
        } catch (Exception e) {
            return UUID.randomUUID().toString().substring(0,16);
        }

    }


    public static String base64Encode(byte[] bytes) {
        return Base64.encodeBase64String(bytes);
    }

    public static byte[] base64Decode(String base64Code) throws Exception {
        return Base64.decodeBase64(base64Code);
    }

    public static byte[] aesEncryptToBytes(String content, String encryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(), "AES"));
        return cipher.doFinal(content.getBytes("utf-8"));
    }

    public static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(), "AES"));
        byte[] decryptBytes = cipher.doFinal(encryptBytes);
        return new String(decryptBytes);
    }

    public static void main(String[] args) throws Exception {

        String str = UUID.randomUUID().toString().substring(0,16);
        System.out.println(str);


        String content = "333";

        String encryptKey = getEncryptKey();
        System.out.println(encryptKey.length());

        String encrypt = aesEncrypt(content, encryptKey);
        System.out.println(encrypt.length() + ":加密后：" + encrypt);

        String decrypt = aesDecrypt("GT7p9uU/4O3byC0xXC3AtQ==", "c3ca815dc64c491c");
        System.out.println("解密后：" + decrypt);

    }

}
