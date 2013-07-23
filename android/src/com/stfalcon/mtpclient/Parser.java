package com.stfalcon.mtpclient;

import android.util.Log;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;

/**
 * Created by user on 7/22/13.
 */
public class Parser {

    public static final String AUTH = "auth_key";
    public static final String MESSAGE_ID = "message_id";
    public static final String MESSAGE_LENGTH = "message_length";
    public static final String RES_PQ = "res_pq";
    public static final String NONCE = "nonce";
    public static final String SERVER_NONCE = "server_nonce";
    public static final String PQ = "pq";
    public static final String P = "p";
    public static final String Q = "q";
    public static final String VECTOR_LONG = "vector_long";
    public static final String COUNT = "count";
    public static final String FINGER_PRINTS ="finger_prints";

    public static void reverse(byte[] array) {
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

    public static HashMap<String,Object> parseReqPqResponse(byte[] response) {

        try {
            HashMap<String,Object> result = new HashMap<String,Object>();
            ByteBuffer buffer = ByteBuffer.wrap(response, 0, 4);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            int header_message_length = buffer.getInt();
            byte[] message = new byte[header_message_length];
            ByteBuffer.wrap(response, 0, header_message_length).get(message);
            int header_pack_id = ByteBuffer.wrap(message, 4, 4).order(ByteOrder.LITTLE_ENDIAN).getInt();
            Log.v("PARSER", "HEADER: " + header_message_length + " " + header_pack_id);
            long auth_key = ByteBuffer.wrap(message, 8, 8).order(ByteOrder.LITTLE_ENDIAN).getLong();
            long message_id = ByteBuffer.wrap(message, 16, 8).order(ByteOrder.LITTLE_ENDIAN).getLong();
            int message_length = ByteBuffer.wrap(message, 24, 4).order(ByteOrder.LITTLE_ENDIAN).getInt();
            long res_pq = ByteBuffer.wrap(message, 28, 4).order(ByteOrder.BIG_ENDIAN).getInt();
            byte[] nonce = new byte[16];
            ByteBuffer.wrap(response, 32, 16).get(nonce);
            byte[] server_nonce = new byte[16];
            ByteBuffer.wrap(response, 48, server_nonce.length).get(server_nonce);
            byte[] pq = new byte[12];
            ByteBuffer.wrap(response, 64, pq.length).get(pq);
            result.put(Parser.AUTH, auth_key);
            result.put(Parser.MESSAGE_ID, message_id);
            result.put(Parser.MESSAGE_LENGTH, message_length);
            result.put(Parser.RES_PQ, res_pq);
            result.put(Parser.NONCE, nonce);
            result.put(Parser.SERVER_NONCE, server_nonce);
            result.put(Parser.PQ, pq);
            Log.v("PARSER", "AUTH: " + auth_key);
            Log.v("PARSER", "Message ID: " + message_id);
            Log.v("PARSER", "message_length: " + message_length);
            Log.v("PARSER", "RES_PQ: " + res_pq);
            Log.v("PARSER", "NONCE: " + Utils.byteArrayToHex(nonce));
            Log.v("PARSER", "Server_NONCE: " + Utils.byteArrayToHex(server_nonce));
            Log.v("PARSER", "PQ: " + Utils.byteArrayToHex(pq));
            long vector_long = ByteBuffer.wrap(message, 76, 4).order(ByteOrder.BIG_ENDIAN).getInt();
            long count = ByteBuffer.wrap(message, 80, 4).order(ByteOrder.LITTLE_ENDIAN).getInt();
            byte[] finger_prints = new byte[8];
            ByteBuffer.wrap(response, 84, finger_prints.length).get(finger_prints);
            byte[] PQ = new byte[8];
            ByteBuffer.wrap(response, 65, pq.length).get(pq);
            BigInteger bigInteger = new BigInteger(pq);
            BigIntegerMath bigIntegerMath = new BigIntegerMath();
            bigIntegerMath.factor(bigInteger);
            BigInteger[] pq_result = bigIntegerMath.getfactors();
            result.put(Parser.P, pq_result[0]);
            result.put(Parser.Q, pq_result[1]);
            result.put(Parser.VECTOR_LONG, vector_long);
            result.put(Parser.COUNT, count);
            result.put(Parser.FINGER_PRINTS, finger_prints);
            Log.v("PARSER", "P: " + pq_result[0]);
            Log.v("PARSER", "Q: " + pq_result[1]);
            Log.v("PARSER", "VECTOR_LONG: " + vector_long);
            Log.v("PARSER", "COUNT: " + count);
            Log.v("PARSER", "finger_prints: " + Utils.byteArrayToHex(finger_prints));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public void parseResponse() {

    }
}
