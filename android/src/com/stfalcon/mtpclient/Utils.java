package com.stfalcon.mtpclient;

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
}
