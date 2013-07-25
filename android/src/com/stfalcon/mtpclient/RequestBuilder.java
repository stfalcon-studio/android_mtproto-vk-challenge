package com.stfalcon.mtpclient;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.zip.CRC32;


public class RequestBuilder {

    public static byte[] NEW_NONCE;
    public static byte[] B;
    public static byte[] G_A;
    public static byte[] AUTH_KEY;
    public static byte[] DH_PRIME;
    public static byte[] SERVER_NONCE;
    public static byte[] NONCE;
    public static byte[] AUTH_HASH;
    public static byte[] MSG_KEY;

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
            byte[] pq = (byte[]) hashMap.get(Parser.PQ);
            //bytes.order(ByteOrder.LITTLE_ENDIAN);
            bytes.put(pq);
            byte[] arrayPQ = bytes.array();
            bytes.clear();

            bytes = ByteBuffer.allocate(8);
            bytes.put((byte[]) hashMap.get(Parser.P));
            byte[] array_p = bytes.array();
            bytes.clear();

            bytes = ByteBuffer.allocate(8);
            bytes.put((byte[]) hashMap.get(Parser.Q));
            byte[] array_q = bytes.array();
            bytes.clear();

            bytes = ByteBuffer.allocate(16);
            byte[] nonce = (byte[]) hashMap.get(Parser.NONCE);
            bytes.put(nonce);
            byte[] arrayNonce = bytes.array();
            bytes.clear();

            bytes = ByteBuffer.allocate(16);
            byte[] server_nonce = (byte[]) hashMap.get(Parser.SERVER_NONCE);
            bytes.put(server_nonce);
            byte[] arrayServerNonce = bytes.array();
            bytes.clear();

            //NewNonce
            bytes = ByteBuffer.allocate(32);
            byte[] b_nonce = new byte[32];
            new Random().nextBytes(b_nonce);
            //bytes.order(ByteOrder.LITTLE_ENDIAN);
            byte[] arrayNewNonce = bytes.array();
            NEW_NONCE = arrayNewNonce;

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

    public static byte[] create_client_DH_inner_data(HashMap<String, Object> hashMap) {
        try {
            //Client_DH_inner_data
            ByteBuffer bytes = ByteBuffer.allocate(4);
            byte[] DH_inner_data = {(byte) 0x66, (byte) 0x43, (byte) 0xb6, (byte) 0x54};
            Utils.reverseArray(DH_inner_data);
            bytes.put(DH_inner_data);
            byte[] arrayDH_inner_data = bytes.array();
            bytes.clear();

            //Nonce
            bytes = ByteBuffer.allocate(16);
            byte[] nonce = (byte[]) hashMap.get(Parser.NONCE);
            bytes.put(nonce);
            byte[] arrayNonce = bytes.array();
            bytes.clear();

            //Server_nonce
            bytes = ByteBuffer.allocate(16);
            byte[] server_nonce = (byte[]) hashMap.get(Parser.SERVER_NONCE);
            bytes.put(server_nonce);
            byte[] arrayServerNonce = bytes.array();
            bytes.clear();

            //Retry_id
            bytes = ByteBuffer.allocate(8);
            byte[] arrayRetry_id = bytes.array();
            bytes.clear();


            //Encrypted_data_bytes_header
            bytes = ByteBuffer.allocate(4);
            byte[] bytes_header = new byte[]{(byte) 0xFE, 0x00, 0x01, 0x00};
            bytes.put(bytes_header);
            byte[] gb_header = bytes.array();
            bytes.clear();

            //g_b
            bytes = ByteBuffer.allocate(256);
            byte[] g_b = generateGB(new BigInteger((byte[]) hashMap.get(Parser.G)), new BigInteger(1, (byte[]) hashMap.get(Parser.DH_PRIME)));//G_B
            bytes.put(g_b);
            Log.v("LOGER", "GB" + g_b.length + " " + Utils.byteArrayToHex(g_b));

            byte[] array_g_b = bytes.array();
            bytes.clear();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(arrayDH_inner_data);
            outputStream.write(arrayNonce);
            outputStream.write(arrayServerNonce);
            outputStream.write(arrayRetry_id);
            outputStream.write(gb_header);
            outputStream.write(array_g_b);

            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] createReq_DHRequest(HashMap<String, Object> hashMap) {
        try {

            //AUTH_KEY ()
            ByteBuffer bytes = ByteBuffer.allocate(8);
            bytes.order(ByteOrder.LITTLE_ENDIAN);
            byte[] arrayAuth = bytes.array();
            bytes.clear();

            //MessageID
            bytes = ByteBuffer.allocate(8);
            byte[] arrayMessageID = bytes.array();
            bytes.putLong(System.currentTimeMillis() / 1000L);
            bytes.clear();

            //MessageLength
            bytes = ByteBuffer.allocate(4);
            bytes.order(ByteOrder.LITTLE_ENDIAN);
            byte[] arrayMessageLength = bytes.array();
            bytes.putInt(320);
            bytes.clear();

            //Req_DH
            bytes = ByteBuffer.allocate(4);
            byte[] req_dh = {(byte) 0xbe, (byte) 0xe4, 0x12, (byte) 0xd7};
            bytes.order(ByteOrder.LITTLE_ENDIAN);
            bytes.put(req_dh);
            byte[] arrayReqDH = bytes.array();
            bytes.clear();

            //Nonce
            bytes = ByteBuffer.allocate(16);
            bytes.order(ByteOrder.LITTLE_ENDIAN);
            bytes.put((byte[]) hashMap.get(Parser.NONCE));
            byte[] arrayNonce = bytes.array();
            bytes.clear();

            //Server_nonce
            bytes = ByteBuffer.allocate(16);
            bytes.order(ByteOrder.LITTLE_ENDIAN);
            bytes.put((byte[]) hashMap.get(Parser.SERVER_NONCE));
            byte[] arrayServerNonce = bytes.array();
            bytes.clear();

            //P
            bytes = ByteBuffer.allocate(8);
            bytes.put((byte[]) hashMap.get(Parser.P));
            byte[] P = bytes.array();
            bytes.clear();

            //Q
            bytes = ByteBuffer.allocate(8);
            bytes.put((byte[]) hashMap.get(Parser.Q));
            byte[] Q = bytes.array();
            bytes.clear();

            //Public_key_fingerprint
            bytes = ByteBuffer.allocate(8);
            bytes.put((byte[]) hashMap.get(Parser.FINGER_PRINTS));
            byte[] Public_key_fingerprint = bytes.array();
            bytes.clear();

            //Encrypted_data
            bytes = ByteBuffer.allocate(4);
            byte[] bytes_header = new byte[]{(byte) 0xFE, 0x00, 0x01, 0x00};
            bytes.put(bytes_header);
            byte[] encrypted_data_header = bytes.array();
            bytes.clear();

            //Encrypted_data
            bytes = ByteBuffer.allocate(256);
            bytes.put(EncryptData.RSAEncrypt(EncryptData.getDataWithHash(RequestBuilder.createP_Q_inner_data(hashMap))));
            byte[] encrypted_data = bytes.array();
            bytes.clear();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(arrayAuth);
            outputStream.write(arrayMessageID);
            outputStream.write(arrayMessageLength);
            outputStream.write(arrayReqDH);
            outputStream.write(arrayNonce);
            outputStream.write(arrayServerNonce);
            outputStream.write(P);
            outputStream.write(Q);
            outputStream.write(Public_key_fingerprint);
            outputStream.write(encrypted_data_header);
            outputStream.write(encrypted_data);
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

    public static byte[] create_Set_client_DHRequest(HashMap<String, Object> hashMap) {
        try {

            //AUTH_KEY ()
            ByteBuffer bytes = ByteBuffer.allocate(8);
            bytes.order(ByteOrder.LITTLE_ENDIAN);
            byte[] arrayAuth = bytes.array();
            bytes.clear();

            //MessageID
            bytes = ByteBuffer.allocate(8);
            byte[] arrayMessageID = bytes.array();
            bytes.putLong(Long.reverseBytes((new Date().getTime() / 1000) << 32));
            bytes.clear();

            //MessageLength
            bytes = ByteBuffer.allocate(4);
            bytes.order(ByteOrder.LITTLE_ENDIAN);
            byte[] arrayMessageLength = bytes.array();
            bytes.putInt(376);
            bytes.clear();

            //client_DH
            bytes = ByteBuffer.allocate(4);
            byte[] req_dh = {(byte) 0xF5, (byte) 0x04, 0x5F, (byte) 0x1F};
            Utils.reverseArray(req_dh);
            bytes.order(ByteOrder.LITTLE_ENDIAN);
            bytes.put(req_dh);
            byte[] arrayReqDH = bytes.array();
            bytes.clear();

            //Nonce
            bytes = ByteBuffer.allocate(16);
            bytes.order(ByteOrder.LITTLE_ENDIAN);
            bytes.put((byte[]) hashMap.get(Parser.NONCE));
            byte[] arrayNonce = bytes.array();
            bytes.clear();

            //Server_nonce
            bytes = ByteBuffer.allocate(16);
            bytes.order(ByteOrder.LITTLE_ENDIAN);
            bytes.put((byte[]) hashMap.get(Parser.SERVER_NONCE));
            byte[] arrayServerNonce = bytes.array();
            bytes.clear();

            //Encrypted_data_bytes_header
            bytes = ByteBuffer.allocate(4);
            byte[] bytes_header = new byte[]{(byte) 0xFE, 0x50, 0x01, 0x00};
            bytes.put(bytes_header);
            byte[] encrypted_data_header = bytes.array();
            bytes.clear();

            //Encrypted_data
            bytes = ByteBuffer.allocate(336);
            byte[] decrypt_answer = EncryptData.decrypt_message((byte[]) hashMap.get(Parser.ENC_ANSWER), (byte[]) hashMap.get(Parser.SERVER_NONCE), NEW_NONCE);
            byte[] client_DH_inner_data = RequestBuilder.create_client_DH_inner_data(Parser.parse_server_DH_inner_data(decrypt_answer));
            Log.v("BUILDER", "" + EncryptData.getDataWithHash(client_DH_inner_data).length);
            bytes.put(EncryptData.igeEncrypt(EncryptData.IGE_KEY, EncryptData.IGE_IV, EncryptData.getDataWithHash1(client_DH_inner_data)));
            byte[] encrypted_data = bytes.array();
            bytes.clear();
            Log.v("STRING", Utils.byteArrayToHex(l_string(encrypted_data)));

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(arrayAuth);
            outputStream.write(arrayMessageID);
            outputStream.write(arrayMessageLength);
            outputStream.write(arrayReqDH);
            outputStream.write(arrayNonce);
            outputStream.write(arrayServerNonce);
            outputStream.write(encrypted_data_header);
            outputStream.write(encrypted_data);
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

    public static byte[] generateGB(BigInteger g, BigInteger dh_prime) {
        try {

            ByteBuffer bytes = ByteBuffer.allocate(256);
            byte[] b = new byte[256];
            new Random().nextBytes(b);
            bytes.put(b);
            byte[] arrayB = bytes.array();
            //Utils.reverseArray(arrayB);
            Log.v("LOGER", "arrayB: " + Utils.byteArrayToHex(arrayB));
            //byte[] bb = Utils.hexStringToByteArray("6F620AFA575C9233EB4C014110A7BCAF49464F798A18A0981FEA1E05E8DA67D9681E0FD6DF0EDF0272AE3492451A84502F2EFC0DA18741A5FB80BD82296919A70FAA6D07CBBBCA2037EA7D3E327B61D585ED3373EE0553A91CBD29B01FA9A89D479CA53D57BDE3A76FBD922A923A0A38B922C1D0701F53FF52D7EA9217080163A64901E766EB6A0F20BC391B64B9D1DD2CD13A7D0C946A3A7DF8CEC9E2236446F646C42CFE2B60A2A8D776E56C8D7519B08B88ED0970E10D12A8C9E355D765F2B7BBB7B4CA9360083435523CB0D57D2B106FD14F94B4EEE79D8AC131CA56AD389C84FE279716F8124A543337FB9EA3D988EC5FA63D90A4BA3970E7A39E5C0DE5");
            //Utils.reverseArray(bb);
            BigInteger bi = new BigInteger(arrayB);
            Log.v("LOGER", "g: " + Utils.byteArrayToHex(g.toByteArray()));
            Log.v("LOGER", "bi: " + Utils.byteArrayToHex(bi.toByteArray()));
            Log.v("LOGER", "dh_prime: " + Utils.byteArrayToHex(dh_prime.toByteArray()));
            BigInteger g_b = g.modPow(bi, dh_prime);
            byte[] gb_array = Utils.subByte(g_b.toByteArray(), 1, g_b.toByteArray().length - 1);
            Log.v("LOGER", "G_b: " + Utils.byteArrayToHex(gb_array));

            return gb_array;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] create_saveDeveloperInfo() {
        //SALT ()
        ByteBuffer bytes = ByteBuffer.allocate(8);
        bytes.put(SALT);
        byte[] salt = bytes.array();
        bytes.clear();

        //sessionID ()
        bytes = ByteBuffer.allocate(8);
        bytes.putInt(0);
        byte[] session_id = bytes.array();//0
        bytes.clear();

        //MessageID
        bytes = ByteBuffer.allocate(8);
        bytes.putLong(System.currentTimeMillis() / 1000L);//
        byte[] arrayMessageID = bytes.array();
        bytes.clear();

        //seq_no
        bytes = ByteBuffer.allocate(4);
        bytes.order(ByteOrder.LITTLE_ENDIAN);//1
        bytes.putInt(1);
        byte[] seq_no = bytes.array();
        bytes.clear();

        //VK ID
        bytes = ByteBuffer.allocate(4);
        //bytes.order(ByteOrder.LITTLE_ENDIAN);
        bytes.putInt(21423318);
        byte[] vk_id = bytes.array();
        bytes.clear();

        //name
        bytes = ByteBuffer.allocate(4);
        //bytes.order(ByteOrder.LITTLE_ENDIAN);
        String s_name = "Степан";
        bytes.put(l_string(s_name.getBytes()));
        byte[] name = bytes.array();
        bytes.clear();

        //phone
        bytes = ByteBuffer.allocate(4);
        //bytes.order(ByteOrder.LITTLE_ENDIAN);
        String s_phone = "+80978470342";
        bytes.put(l_string(s_phone.getBytes()));
        byte[] phone = bytes.array();
        bytes.clear();

        //age
        bytes = ByteBuffer.allocate(4);
        bytes.order(ByteOrder.LITTLE_ENDIAN);
        bytes.putInt(27);
        byte[] age = bytes.array();
        bytes.clear();

        //city
        bytes = ByteBuffer.allocate(4);
        String s_city = "Хмельницкий";
        bytes.put(l_string(s_city.getBytes()));
        byte[] city = bytes.array();
        bytes.clear();

        //MessageLength
        bytes = ByteBuffer.allocate(4);
        //bytes.order(ByteOrder.LITTLE_ENDIAN);
        bytes.putInt(vk_id.length + name.length + phone.length + age.length + city.length);
        byte[] msg_len = bytes.array();
        bytes.clear();

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(salt);
            outputStream.write(session_id);
            outputStream.write(arrayMessageID);
            outputStream.write(seq_no);
            outputStream.write(msg_len);
            outputStream.write(vk_id);
            outputStream.write(phone);
            outputStream.write(age);
            outputStream.write(city);
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }


    }

    public static byte[] l_string(byte[] data) {
        byte[] res;
        if (data.length > 254) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(4);
            byteBuffer.put((byte) 254);
            byteBuffer.put((byte) (data.length));
            byteBuffer.put((byte) (data.length >> 8));
            byteBuffer.put((byte) (data.length >> 16));
            int ran_len = 0;
            while (((data.length + 4 + ran_len) % 4) != 0) {
                ran_len++;
            }
            ByteBuffer resBuffer = ByteBuffer.allocate(data.length + 4 + ran_len);
            resBuffer.put(data);
            res = Utils.sumByte(byteBuffer.array(), resBuffer.array());
        } else {
            res = Utils.sumByte(new byte[]{(byte) data.length}, data);
            int ran_len = 0;
            while (((res.length + ran_len) % 4) != 0) {
                ran_len++;
            }
            ByteBuffer resBuffer = ByteBuffer.allocate(res.length + ran_len);
            resBuffer.put(data);
            res = resBuffer.array();
        }
        return res;
    }

    public static void saveAuthKey(HashMap<String, Object> hashMap) {
        AUTH_KEY = generateGB(new BigInteger(G_A), new BigInteger(1, DH_PRIME));
    }
}
