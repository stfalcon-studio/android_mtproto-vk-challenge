package com.stfalcon.mtpclient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import java.math.BigInteger;


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
                BigIntegerMath bigIntegerMath = new BigIntegerMath();
                bigIntegerMath.factor(new BigInteger("55465884552"));
                //bigIntegerMath.getfactors();
                Log.v("LOGER", bigIntegerMath.getfactors());
            }
        });

        return true;
    }

}
