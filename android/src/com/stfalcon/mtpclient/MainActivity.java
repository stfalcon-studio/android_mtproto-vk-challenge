package com.stfalcon.mtpclient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity {

    TCPLink tcpLink = new TCPLink();
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        startService(new Intent(MainActivity.this, TCPLink.class));
        super.onCreate(savedInstanceState);
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
                /*try {
                    byte[] key = EncryptData.getTmp_aes_key(Utils.hexStringToByteArray("A5CF4D33F4A11EA877BA4AA573907330"), Utils.hexStringToByteArray("311C85DB234AA2640AFC4A76A735CF5B1F0FD68BD17FA181E1229AD867CC024D"));
                    byte[] iv = EncryptData.getTmp_aes_iv(Utils.hexStringToByteArray("A5CF4D33F4A11EA877BA4AA573907330"), Utils.hexStringToByteArray("311C85DB234AA2640AFC4A76A735CF5B1F0FD68BD17FA181E1229AD867CC024D"));
                    // byte[] key = Utils.hexStringToByteArray("5468697320697320616E20696D706C65");
                    //byte[] iv = Utils.hexStringToByteArray("6D656E746174696F6E206F6620494745206D6F646520666F72204F70656E5353");
                    Log.v("LOGER", Utils.byteArrayToHex(key));
                    Log.v("LOGER", Utils.byteArrayToHex(iv));
                    byte[] mes = Utils.hexStringToByteArray("28A92FE20173B347A8BB324B5FAB2667C9A8BBCE6468D5B509A4CBDDC186240AC912CF7006AF8926DE606A2E74C0493CAA57741E6C82451F54D3E068F5CCC49B4444124B9666FFB405AAB564A3D01E67F6E912867C8D20D9882707DC330B17B4E0DD57CB53BFAAFA9EF5BE76AE6C1B9B6C51E2D6502A47C883095C46C81E3BE25F62427B585488BB3BF239213BF48EB8FE34C9A026CC8413934043974DB03556633038392CECB51F94824E140B98637730A4BE79A8F9DAFA39BAE81E1095849EA4C83467C92A3A17D997817C8A7AC61C3FF414DA37B7D66E949C0AEC858F048224210FCC61F11C3A910B431CCBD104CCCC8DC6D29D4A5D133BE639A4C32BBFF153E63ACA3AC52F2E4709B8AE01844B142C1EE89D075D64F69A399FEB04E656FE3675A6F8F412078F3D0B58DA15311C1A9F8E53B3CD6BB5572C294904B726D0BE337E2E21977DA26DD6E33270251C2CA29DFCC70227F0755F84CFDA9AC4B8DD5F84F1D1EB36BA45CDDC70444D8C213E4BD8F63B8AB95A2D0B4180DC91283DC063ACFB92D6A4E407CDE7C8C69689F77A007441D4A6A8384B666502D9B77FC68B5B43CC607E60A146223E110FCB43BC3C942EF981930CDC4A1D310C0B64D5E55D308D863251AB90502C3E46CC599E886A927CDA963B9EB16CE62603B68529EE98F9F5206419E03FB458EC4BD9454AA8F6BA777573CC54B328895B1DF25EAD9FB4CD5198EE022B2B81F388D281D5E5BC580107CA01A50665C32B552715F335FD76264FAD00DDD5AE45B94832AC79CE7C511D194BC42B70EFA850BB15C2012C5215CABFE97CE66B8D8734D0EE759A638AF013");
                    //byte[] mes = Utils.hexStringToByteArray("4C2E204C6574277320686F70652042656E20676F74206974207269676874210A");
                    byte[] res = EncryptData.ige(key, iv, mes);
                    Log.v("LOGER", "(" + mes.length + ")" + " " + Utils.byteArrayToHex(res) + "  (" + res.length + ")");
                    //Log.v("LOGER", Utils.byteArrayToHex(EncryptData.ige(key, iv, Utils.hexStringToByteArray("4C2E204C6574277320686F70652042656E20676F74206974207269676874210A"))));
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
            }
        });
        return true;
    }

}
