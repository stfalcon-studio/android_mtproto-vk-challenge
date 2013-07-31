package com.stfalcon.mtpclient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import java.math.BigInteger;
import java.util.Random;


public class MainActivity extends Activity {

    TCPLink tcpLink = new TCPLink();
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        startService(new Intent(MainActivity.this, TCPLink.class));
        super.onCreate(savedInstanceState);
        RequestBuilder.SESSION_ID = new byte[8];
        new Random().nextBytes(RequestBuilder.SESSION_ID);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        context = this;
        Button startService = (Button) findViewById(R.id.start);
        Button stopService = (Button) findViewById(R.id.stop);

        startService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tcpLink.sendReqPqRequest();
            }
        });

        stopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* try {
                    //byte[] key = EncryptData.getTmp_aes_key(Utils.hexStringToByteArray("A5CF4D33F4A11EA877BA4AA573907330"), Utils.hexStringToByteArray("311C85DB234AA2640AFC4A76A735CF5B1F0FD68BD17FA181E1229AD867CC024D"));
                    //byte[] iv = EncryptData.getTmp_aes_iv(Utils.hexStringToByteArray("A5CF4D33F4A11EA877BA4AA573907330"), Utils.hexStringToByteArray("311C85DB234AA2640AFC4A76A735CF5B1F0FD68BD17FA181E1229AD867CC024D"));
                    byte[] key = Utils.hexStringToByteArray("5468697320697320616E20696D706C65");
                    byte[] iv = Utils.hexStringToByteArray("6D656E746174696F6E206F6620494745206D6F646520666F72204F70656E5353");
                    Log.v("LOGER", Utils.byteArrayToHex(key));
                    Log.v("LOGER", Utils.byteArrayToHex(iv));
                    //byte[] mes = Utils.hexStringToByteArray("");
                    byte[] mes = Utils.hexStringToByteArray("99706487A1CDE613BC6DE0B6F24B1C7AA448C8B9C3403E3467A8CAD89340F53B");
                    byte[] res = EncryptData.igeEncrypt(key, iv, mes);
                    Log.v("LOGER", "(" + mes.length + ")" + " " + Utils.byteArrayToHex(res) + "  (" + res.length + ")");
                    //Log.v("LOGER", Utils.byteArrayToHex(EncryptData.igeDecrypt(key, iv, Utils.hexStringToByteArray("4C2E204C6574277320686F70652042656E20676F74206974207269676874210A"))));
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
                /*String s = ""
                RequestBuilder.l_string()*/
                byte[] g = Utils.hexStringToByteArray("0002");
                byte[] dh_pr = Utils.hexStringToByteArray("C71CAEB9C6B1C9048E6C522F70F13F73980D40238E3E21C14934D037563D930F48198A0AA7C14058229493D22530F4DBFA336F6E0AC925139543AED44CCE7C3720FD51F69458705AC68CD4FE6B6B13ABDC9746512969328454F18FAF8C595F642477FE96BB2A941D5BCD1D4AC8CC49880708FA9B378E3C4F3A9060BEE67CF9A4A4A695811051907E162753B56B0F6B410DBA74D8A84B2A14B3144E0EF1284754FD17ED950D5965B4B9DD46582DB1178D169C6BC465B0D6FF9CA3928FEF5B9AE4E418FC15E83EBEA0F87FA9FF5EED70050DED2849F47BF959D956850CE929851F0D8115F635B105EE2E4E15D04B2454BF6F4FADF034B10403119CD8E3B92FCC5B");
                byte[] g_B = RequestBuilder.generateGB(new BigInteger(g), new BigInteger(1, dh_pr));

            }
        });

        stopService.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                byte[] dh = Utils.hexStringToByteArray("C71CAEB9C6B1C9048E6C522F70F13F73980D40238E3E21C14934D037563D930F48198A0AA7C14058229493D22530F4DBFA336F6E0AC925139543AED44CCE7C3720FD51F69458705AC68CD4FE6B6B13ABDC9746512969328454F18FAF8C595F642477FE96BB2A941D5BCD1D4AC8CC49880708FA9B378E3C4F3A9060BEE67CF9A4A4A695811051907E162753B56B0F6B410DBA74D8A84B2A14B3144E0EF1284754FD17ED950D5965B4B9DD46582DB1178D169C6BC465B0D6FF9CA3928FEF5B9AE4E418FC15E83EBEA0F87FA9FF5EED70050DED2849F47BF959D956850CE929851F0D8115F635B105EE2E4E15D04B2454BF6F4FADF034B10403119CD8E3B92FCC5B");
                //Utils.reverseArray(dh);
                RequestBuilder.generateGB(new BigInteger(Utils.hexStringToByteArray("02")), new BigInteger(1, dh));
                return false;
            }
        });
        return true;
    }

}
