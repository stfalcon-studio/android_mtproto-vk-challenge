package com.stfalcon.mtpclient;

import android.util.Base64;
import android.util.Log;

import org.spongycastle.asn1.ASN1InputStream;
import org.spongycastle.asn1.x509.RSAPublicKeyStructure;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Arrays;
import java.util.Formatter;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by anton on 7/23/13.
 */
public class EncryptData {

    public static byte[] IGE_KEY;
    public static byte[] IGE_IV;
    private static String publicKeyString = "MIIBCgKCAQEAwVACPi9w23mF3tBkdZz+zwrzKOaaQdr01vAbU4E1pvkfj4sqDsm6" +
            "lyDONS789sVoD/xCS9Y0hkkC3gtL1tSfTlgCMOOul9lcixlEKzwKENj1Yz/s7daS" +
            "an9tqw3bfUV/nqgbhGX81v/+7RFAEd+RwFnK7a+XYl9sluzHRyVVaTTveB2GazTw" +
            "Efzk2DWgkBluml8OREmvfraX3bkHZJTKX4EQSjBbbdJ2ZXIsRrYOXfaA+xayEGB+" +
            "8hdlLmAjbCVfaigxX0CDqWeR1yFL9kwd9P0NsZRPsmoqVwMbMu7mStFai6aIhc3n" +
            "Slv8kg9qv1m6XHVQY3PnEw+QQtqSIXklHwIDAQAB";

    public static byte[] getDataWithHash(byte[] data) {
        try {
            byte[] data_with_hash;// = new byte[255];
            byte[] data_SHA1 = SHAsum(data);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            int len = data.length + data_SHA1.length;
            int ran_len = 0;
            while (((len + ran_len) % 16) != 0) {
                ran_len++;
            }
            if ((ran_len + data.length + data_SHA1.length) < 255) {
                ran_len += 128;
            }
            byte[] random_bytes = new byte[ran_len-1];
            new Random().nextBytes(random_bytes);
            Log.v("LOGER", "" + data.length);
            Log.v("LOGER", "" + data_SHA1.length);
            Log.v("LOGER", "" + random_bytes.length);
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

    public static byte[] getDataWithHash1(byte[] data) {
        try {
            byte[] data_with_hash;// = new byte[255];
            byte[] data_SHA1 = SHAsum(data);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            int len = data.length + data_SHA1.length;
            int ran_len = 0;
            while (((len + ran_len) % 16) != 0) {
                ran_len++;
            }
            if ((ran_len + data.length + data_SHA1.length) < 255) {
                ran_len += 128;
            }
            byte[] random_bytes = new byte[ran_len-1];
            new Random().nextBytes(random_bytes);
            Log.v("LOGER", "" + data.length);
            Log.v("LOGER", "" + data_SHA1.length);
            Log.v("LOGER", "" + random_bytes.length);
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

    public static byte[] getDataWithHash2(byte[] data) {
        try {
            byte[] data_with_hash;// = new byte[255];
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            int len = data.length;
            int ran_len = 0;
            while (((len + ran_len) % 16) != 0) {
                ran_len++;
            }
            /*if ((ran_len + data.length) < 255) {
                ran_len += 128;
            }*/
            byte[] random_bytes = new byte[ran_len-1];
            new Random().nextBytes(random_bytes);
            Log.v("LOGER", "" + data.length);
            Log.v("LOGER", "" + random_bytes.length);
            //outputStream.write(data_SHA1);
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

    public static byte[] getTmp_aes_iv(byte[] server_nonce, byte[] new_nonce) {
        try {
            byte[] tmp_aes_iv = Utils.sumByte(Utils.subByte(SHAsum(Utils.sumByte(server_nonce, new_nonce)), 12, 8),
                    Utils.sumByte(SHAsum(Utils.sumByte(new_nonce, new_nonce)), Utils.subByte(new_nonce, 0, 4)));
            IGE_IV = tmp_aes_iv;
            return tmp_aes_iv;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] getTmp_aes_key(byte[] server_nonce, byte[] new_nonce) {
        try {
            byte[] tmp_aes_key = Utils.sumByte(SHAsum(Utils.sumByte(new_nonce, server_nonce)),
                    Utils.subByte(SHAsum(Utils.sumByte(server_nonce, new_nonce)), 0, 12));
            IGE_KEY = tmp_aes_key;
            return tmp_aes_key;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] decrypt_message(final byte[] message, byte[] server_nonce, byte[] new_nonce) {
        try {
            byte[] key = EncryptData.getTmp_aes_key(server_nonce, new_nonce);
            byte[] iv = EncryptData.getTmp_aes_iv(server_nonce, new_nonce);
            Log.v("DECRYPT", "getTmp_aes_key: " + Utils.byteArrayToHex(key));
            Log.v("DECRYPT", "getTmp_aes_iv: " + Utils.byteArrayToHex(iv));
            byte[] mes = message;
            byte[] res = EncryptData.igeDecrypt(key, iv, mes);
            ByteBuffer byteBuffer = ByteBuffer.wrap(res);
            res = Utils.subByte(res, 20, res.length - 20);
            res = Utils.subByte(res, 0, res.length - 8);
            Log.v("DECRYPT", "(" + mes.length + ")" + " " + Utils.byteArrayToHex(res) + "  (" + res.length + ")");
            //res = Utils.subByte(res,0,res.length - )
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] igeDecrypt(final byte[] key, final byte[] IV,
                                    final byte[] Message) throws Exception {

        final Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"));

        final int blocksize = cipher.getBlockSize();

        byte[] iv1 = Arrays.copyOfRange(IV, 0, blocksize);
        byte[] iv2 = Arrays.copyOfRange(IV, blocksize, IV.length);
        //Log.v("LOGER","key"+Utils.byteArrayToHex(key));
        //Log.v("LOGER","iv1"+Utils.byteArrayToHex(iv1));
        //Log.v("LOGER","iv2"+Utils.byteArrayToHex(iv2));

        byte[] decrypted = new byte[0];

        byte[] block, tmp;
        for (int i = 0; i < Message.length; i += blocksize) {
            block = java.util.Arrays.copyOfRange(Message, i, i + blocksize);
            //Log.v("LOGER","block "+Utils.byteArrayToHex(block));
            tmp = Utils.xor(cipher.doFinal(Utils.xor(block, iv2)), iv1);
            iv1 = block;
            iv2 = tmp;

            decrypted = Utils.sumByte(decrypted, iv2);
        }
        return decrypted;
    }

    public static byte[] igeEncrypt(final byte[] key, final byte[] IV,
                                    final byte[] Message) throws Exception {

        final Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"));

        final int blocksize = cipher.getBlockSize();

        byte[] iv2 = Arrays.copyOfRange(IV, 0, blocksize);
        byte[] iv1 = Arrays.copyOfRange(IV, blocksize, IV.length);

        byte[] decrypted = new byte[0];

        byte[] block, tmp;
        for (int i = 0; i < Message.length; i += blocksize) {
            block = java.util.Arrays.copyOfRange(Message, i, i + blocksize);
            //Log.v("LOGER","block "+Utils.byteArrayToHex(block));
            tmp = Utils.xor(cipher.doFinal(Utils.xor(block, iv2)), iv1);
            iv1 = block;
            iv2 = tmp;

            decrypted = Utils.sumByte(decrypted, iv2);
        }

        return decrypted;
    }


}
