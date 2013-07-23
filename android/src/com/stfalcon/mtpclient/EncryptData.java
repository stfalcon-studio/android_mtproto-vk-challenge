package com.stfalcon.mtpclient;

import android.util.Base64;
import android.util.Log;

import org.spongycastle.asn1.ASN1InputStream;
import org.spongycastle.asn1.x509.RSAPublicKeyStructure;

import java.io.ByteArrayOutputStream;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Formatter;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by anton on 7/23/13.
 */
public class EncryptData {

    private static String publicKeyString = //"-----BEGIN RSA PUBLIC KEY-----\n" +
            "MIIBCgKCAQEAwVACPi9w23mF3tBkdZz+zwrzKOaaQdr01vAbU4E1pvkfj4sqDsm6" +
                    "lyDONS789sVoD/xCS9Y0hkkC3gtL1tSfTlgCMOOul9lcixlEKzwKENj1Yz/s7daS" +
                    "an9tqw3bfUV/nqgbhGX81v/+7RFAEd+RwFnK7a+XYl9sluzHRyVVaTTveB2GazTw" +
                    "Efzk2DWgkBluml8OREmvfraX3bkHZJTKX4EQSjBbbdJ2ZXIsRrYOXfaA+xayEGB+" +
                    "8hdlLmAjbCVfaigxX0CDqWeR1yFL9kwd9P0NsZRPsmoqVwMbMu7mStFai6aIhc3n" +
                    "Slv8kg9qv1m6XHVQY3PnEw+QQtqSIXklHwIDAQAB";
    //"-----END RSA PUBLIC KEY-----";

    public static byte[] getDataWithHash(byte[] data) {
        try {
            byte[] data_with_hash = new byte[255];
            byte[] data_SHA1 = SHAsum(data);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] random_bytes = new byte[data_with_hash.length - (data.length + data_SHA1.length)];
            new Random().nextBytes(random_bytes);
            outputStream.write(data_SHA1);
            outputStream.write(data);
            outputStream.write(random_bytes);
            data_with_hash = outputStream.toByteArray();
            return data_with_hash;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] SHAsum(byte[] convertme) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        Log.v("ENCRYPT", "SHA_1: " + byteArray2Hex(md.digest(convertme)));
        return md.digest(convertme);
    }

    private static String byteArray2Hex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }

    public static byte[] RSAEncrypt(final byte[] plain) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        try {
            Cipher cipher = Cipher.getInstance("RSA/None/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, getFromString(publicKeyString));
            byte[] encryptedBytes = cipher.doFinal(plain);
            Log.v("ENCRYPT", "RSA:" + Utils.byteArrayToHex(encryptedBytes));
            Log.v("ENCRYPT", "RSA l:" + encryptedBytes.length);
            return encryptedBytes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static PublicKey getFromString(String keystr) throws Exception {
        byte[] keyBytes = Base64.decode(keystr, Base64.DEFAULT);
        ASN1InputStream in = new ASN1InputStream(keyBytes);
        RSAPublicKeyStructure pStruct = RSAPublicKeyStructure.getInstance(in.readObject());
        RSAPublicKeySpec spec = new RSAPublicKeySpec(pStruct.getModulus(), pStruct.getPublicExponent());
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

}
