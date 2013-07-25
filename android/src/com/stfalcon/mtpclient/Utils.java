package com.stfalcon.mtpclient;

import java.nio.ByteBuffer;

/**
 * Created by anton on 7/22/13.
 */
public class Utils {

    public static String byteArrayToHex(byte[] array) {
        return byteArrayToHex(array, 0, array.length);
    }

    public static String byteArrayToHex(byte[] array, int offset, int length) {
        StringBuilder sb = new StringBuilder();

        sb.append("---\n");

        boolean isFirst = true;
        int count = 0;
        for (int i = offset; i < offset + length; i++) {
            byte b = array[i];

            if (!isFirst) {
                if (count == 16) {
                    count = 0;
                    sb.append('\n');
                } else {
                    sb.append(' ');
                }
            } else {
                isFirst = false;
            }

            String a = Integer.toHexString(((int) b) & 0xff);
            if (a.length() == 1) a = '0' + a;
            sb.append(a);

            count++;
        }

        sb.append("\n---\n");

        return sb.toString();
    }

    public static void reverseArray(byte[] array) {
        if (array == null) {
            return;
        }
        int i = 0;
        int j = array.length - 1;
        byte tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }

    public static byte[] subByte(byte[] bytes, int index, int count) {
        byte[] substr = new byte[count];
        ByteBuffer.wrap(bytes, index, count).get(substr);
        return substr;
    }

    public static byte[] sumByte(byte[] bytes1, byte[] bytes2) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(bytes1.length + bytes2.length);
        byteBuffer.put(bytes1);
        byteBuffer.put(bytes2);
        return byteBuffer.array();
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public static byte[] xor(byte[] A, byte[] B) {
        byte[] C = new byte[A.length];
        for (int i = 0; i < A.length; i++)
            C[i] = (byte) (A[i] ^ B[i]);
        return C;
    }
}
