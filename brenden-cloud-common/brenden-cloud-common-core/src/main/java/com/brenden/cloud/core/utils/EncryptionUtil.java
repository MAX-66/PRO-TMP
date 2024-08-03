package com.brenden.cloud.core.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * <p>
 *
 * </p>
 *
 * @author lxq
 * @since 2024/8/3
 */
public class EncryptionUtil {
    public static String encryptMD5(String input) {
        return encrypt(input, "MD5");
    }

    public static String encryptSHA1(String input) {
        return encrypt(input, "SHA-1");
    }

    public static String encryptSHA256(String input) {
        return encrypt(input, "SHA-256");
    }

    public static String encryptSHA512(String input) {
        return encrypt(input, "SHA-512");
    }

    public static String encryptSHA3_256(String input) {
        return encrypt(input, "SHA3-256");
    }

    private static String encrypt(String input, String algorithm) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            byte[] hashBytes = messageDigest.digest(input.getBytes());
            StringBuilder stringBuilder = new StringBuilder();
            for (byte b : hashBytes) {
                stringBuilder.append(String.format("%02x", b));
            }
            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Algorithm not found: " + algorithm, e);
        }
    }

    public static void main(String[] args) {
        String originalString = "Hello, World!";
        System.out.println("MD5: " + encryptMD5(originalString));
        System.out.println("SHA-1: " + encryptSHA1(originalString));
        System.out.println("SHA-256: " + encryptSHA256(originalString));
        System.out.println("SHA-512: " + encryptSHA512(originalString));
        System.out.println("SHA3-256: " + encryptSHA3_256(originalString));
    }
}
