package com.stfalcon.mtpclient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Random;
import java.util.zip.CRC32;


public class RequestBuilder {

    public static byte[] createReq_PqRequest() {
        try {
            //Req_Pq
            ByteBuffer bytes = ByteBuffer.allocate(4);
            byte[] req_pq = {0x78, (byte) 0x97, 0x46, 0x60};
            bytes.order(ByteOrder.LITTLE_ENDIAN);
            bytes.put(req_pq);
            byte[] arrayReqPq = bytes.array();
            bytes.clear();

            //Nonce
            bytes = ByteBuffer.allocate(16);
            byte[] b_nonce = new byte[16];
            new Random().nextBytes(b_nonce);
            bytes.order(ByteOrder.LITTLE_ENDIAN);
            byte[] arrayNonce = bytes.array();
            bytes.put(b_nonce);
            bytes.clear();

            //AUTH_KEY ()
            bytes = ByteBuffer.allocate(8);
            bytes.order(ByteOrder.LITTLE_ENDIAN);
            byte[] arrayAuth = bytes.array();
            bytes.clear();

            //MessageID
            bytes = ByteBuffer.allocate(8);
            bytes.putLong(System.currentTimeMillis() / 1000L);
            byte[] arrayMessageID = bytes.array();
            bytes.clear();

            //MessageLength
            bytes = ByteBuffer.allocate(4);
            bytes.order(ByteOrder.LITTLE_ENDIAN);
            bytes.putInt(arrayReqPq.length + arrayNonce.length);
            byte[] arrayMessageLength = bytes.array();
            bytes.clear();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(arrayAuth);
            outputStream.write(arrayMessageID);
            outputStream.write(arrayMessageLength);
            outputStream.write(arrayReqPq);
            outputStream.write(arrayNonce);
            byte[] arrayBodyMessage = outputStream.toByteArray();
            outputStream.flush();
            outputStream.close();
            ByteArrayOutputStream resultStreem = new ByteArrayOutputStream();
            resultStreem.write(createHeader(arrayBodyMessage.length));
            resultStreem.write(arrayBodyMessage);
            resultStreem.write(createCRC32(resultStreem.toByteArray()));
            return resultStreem.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] createHeader(int messageLength) {

        try {
            //длина сообщения
            ByteBuffer bytes = ByteBuffer.allocate(4);
            bytes.order(ByteOrder.LITTLE_ENDIAN);
            bytes.putInt(messageLength + 12);
            byte[] arrayHeader = bytes.array();
            bytes.clear();

            //порядковый номер пакета(пока ненужен)
            bytes = ByteBuffer.allocate(4);
            bytes.order(ByteOrder.LITTLE_ENDIAN);
            byte[] arrayPacketId = bytes.array();
            bytes.clear();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(arrayHeader);
            outputStream.write(arrayPacketId);
            return outputStream.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] createCRC32(byte[] message) {

        ByteBuffer bytes = ByteBuffer.allocate(4);
        CRC32 crc32 = new CRC32();
        crc32.update(message, 0, message.length);
        bytes.order(ByteOrder.LITTLE_ENDIAN);
        bytes.putInt((int) crc32.getValue());
        byte[] arrayCRC32 = bytes.array();
        bytes.clear();
        return arrayCRC32;
    }

    public static byte[] createP_Q_inner_data(HashMap<String, Object> hashMap) {
        try {
            //Req_Pq
            ByteBuffer bytes = ByteBuffer.allocate(4);
            byte[] p_q_inner_data = {(byte) 0x83, (byte) 0xc9, (byte) 0x5a, (byte) 0xec};
            Utils.reverseArray(p_q_inner_data);
            bytes.order(ByteOrder.LITTLE_ENDIAN);
            bytes.put(p_q_inner_data);
            byte[] arrayReqPq = bytes.array();
            bytes.clear();

            bytes = ByteBuffer.allocate(12);
            byte[] pq = (byte[]) hashMap.get("pq");
            //bytes.order(ByteOrder.LITTLE_ENDIAN);
            bytes.put(pq);
            byte[] arrayPQ = bytes.array();
            bytes.clear();

            bytes = ByteBuffer.allocate(8);
            long p = (Long) hashMap.get("p");
            //bytes.order(ByteOrder.LITTLE_ENDIAN);
            bytes.putLong(p);
            byte[] array_p = bytes.array();
            bytes.clear();

            bytes = ByteBuffer.allocate(8);
            long q = (Long) hashMap.get("q");
            //bytes.order(ByteOrder.LITTLE_ENDIAN);
            bytes.putLong(q);
            byte[] array_q = bytes.array();
            bytes.clear();

            bytes = ByteBuffer.allocate(16);
            byte[] nonce = (byte[]) hashMap.get("nonce");
            bytes.put(nonce);
            byte[] arrayNonce = bytes.array();
            bytes.clear();

            bytes = ByteBuffer.allocate(16);
            byte[] server_nonce = (byte[]) hashMap.get("server_nonce");
            bytes.put(server_nonce);
            byte[] arrayServerNonce = bytes.array();
            bytes.clear();

            //Nonce
            bytes = ByteBuffer.allocate(32);
            byte[] b_nonce = new byte[32];
            new Random().nextBytes(b_nonce);
            bytes.order(ByteOrder.LITTLE_ENDIAN);
            byte[] arrayNewNonce = bytes.array();
            bytes.put(b_nonce);
            bytes.clear();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(arrayReqPq);
            outputStream.write(arrayPQ);
            outputStream.write(array_p);
            outputStream.write(array_q);
            outputStream.write(arrayNonce);
            outputStream.write(arrayServerNonce);
            outputStream.write(arrayNewNonce);

            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
