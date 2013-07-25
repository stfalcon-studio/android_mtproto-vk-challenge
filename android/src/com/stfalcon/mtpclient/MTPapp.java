package com.stfalcon.mtpclient;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.Gravity;
import android.widget.Toast;

import java.io.File;
import java.security.Security;

/**
 * Created by user on 7/19/13.
 */
public class MTPapp extends Application {
    public static Context context;


    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        //Security.insertProviderAt(new org.spongycastle.asn1.provider.BouncyCastleProvider(), 1);
    }


    public static void showToastMessage(String message) {
        new ToastMessageTask().execute(message);
    }



    public static class ToastMessageTask extends AsyncTask<Object, String, String> {
        String toastMessage;

        @Override
        protected String doInBackground(Object... params) {
            toastMessage = (String) params[0];
            return toastMessage;
        }

        protected void OnProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        // This is executed in the context of the main GUI thread
        protected void onPostExecute(String result) {

            /*final Toast toast = Toast.makeText(MTPapp.context, result, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();*/

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //toast.cancel();
                }
            }, 1500);
        }
    }

}
