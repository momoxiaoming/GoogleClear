package com.dn.baselib.util;

import android.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESUtil
{

    public static String KEY = "dwnxthekey902902";

    public static String mode="AES/CBC/PKCS7Padding";

    public static String spec="AES";

//    static {
//        try {
//            Security.addProvider(new BouncyCastleProvider());
//        } catch (Throwable t) {
//            t.printStackTrace();
//        }
//    }

    public static byte[] decrypt(byte[] content, byte[] keyByte, byte[] ivByte) throws Exception {
        Cipher cipher = Cipher.getInstance(mode);
        Key sKeySpec = new SecretKeySpec(keyByte,spec);
        //生成iv
        AlgorithmParameters params = AlgorithmParameters.getInstance(spec);
        params.init(new IvParameterSpec(ivByte));
        cipher.init(Cipher.DECRYPT_MODE, sKeySpec, params);
        // 初始化
        return cipher.doFinal(content);
    }

    public static String d(String input) throws Exception {
        byte[] b64 = Base64.decode(input, Base64.DEFAULT);
        byte[] k = KEY.getBytes(StandardCharsets.UTF_8);
        byte[] dBytes = decrypt(b64, k, k);
        return new String(dBytes, StandardCharsets.UTF_8);
    }

    public static byte[] encrypt(byte[] content, byte[] keyByte, byte[] ivByte) throws Exception {
        Cipher cipher = Cipher.getInstance(mode);
        Key sKeySpec = new SecretKeySpec(keyByte, spec);
        //生成iv
        AlgorithmParameters params = AlgorithmParameters.getInstance(spec);
        params.init(new IvParameterSpec(ivByte));
        cipher.init(Cipher.ENCRYPT_MODE, sKeySpec, params);
        // 初始化
        return cipher.doFinal(content);
    }

    public static String e(String input) throws Exception {
        byte[] k = KEY.getBytes(StandardCharsets.UTF_8);
        byte[] b64 = Base64.encode(input.getBytes(), Base64.DEFAULT);
        byte[] dBytes = encrypt(b64, k, k);
        return new String(dBytes, StandardCharsets.UTF_8);
    }

}