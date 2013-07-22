package com.stfalcon.sendfoto;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by user on 7/19/13.
 */
public class TCPLink extends Service {
    private static final String HOST = "95.142.192.65";
    String binaryString;
    public static TCPLink self;


    public static TCPLink getInstance() {
        if (self == null) {
            self = new TCPLink();
        }
        return self;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        startService();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() { }

    class PutData
    {
        String dataFromServer;
        Context context;
    }

    // Здесь выполняем инициализацию нужных нам значений
// и открываем наше сокет-соединение
    private void startService() {

        binaryString = "s544efs875";

        try {
            openConnection();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // данный метод открыает соединение
    public void openConnection() throws InterruptedException
    {
        try {

// PutData - это класс, с помощью которого мы передадим параметры в
// создаваемый поток
            PutData data = new PutData();
            data.dataFromServer = binaryString;
            data.context = this;

// создаем новый поток для сокет-соединения
            new ToSocket().execute(data);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    class ToSocket extends AsyncTask<PutData, Integer, Integer>{
        Context mCtx;
        Socket mySock;

        protected void onProgressUpdate(Integer... progress){
        }

        protected void onPostExecute(Integer result){
// Это выполнится после завершения работы потока
        }

        protected void onCancelled(Integer result){
// Это выполнится после завершения работы потока
        }


        protected Integer doInBackground(PutData... param)
        {
            InetAddress serverAddr;

            mCtx = param[0].context;
            String toServer = param[0].dataFromServer;

            try {
                while(true)
                {
                    serverAddr = InetAddress.getByName(HOST);
                    mySock = new Socket(serverAddr, 80);

// открываем сокет-соединение
                    SocketData data = new SocketData();
                    data.context = mCtx;
                    data.socket = mySock;

// Еще один поток, именно он будет принимать входящие сообщения
                    GetPacket pack = new GetPacket();
                    AsyncTask<SocketData, Integer, Integer> running = pack.execute(data);

                    String message = toServer;
// Посылаем dataFromServer на сервер
                    try {
                        PrintWriter out = new PrintWriter( new BufferedWriter( new OutputStreamWriter(mySock.getOutputStream())),true);

                        out.println(message);

                    } catch(Exception e){while(running.getStatus().equals(AsyncTask.Status.RUNNING));}  // Следим за потоком, принимающим сообщения
// Если поток закончил принимать сообщения - это означает,
// что соединение разорвано (других причин нет).
// Это означает, что нужно закрыть сокет
// и открыть его опять в бесконечном цикле (см. while(true) выше)
                    try
                    {
                        mySock.close();
                    }
                    catch(Exception e)
                    {}
                }
            } catch (Exception e) {
                return -1;
            }
        }
    }



    class SocketData
    {
        Socket socket;
        Context context;
    }

    class GetPacket extends AsyncTask<SocketData, Integer, Integer>
    {
        Context mCtx;
        char[] mData;
        Socket mySock;

        protected void onProgressUpdate(Integer... progress)
        {
            try
            {
// Получаем принятое от сервера сообщение
                String prop = String.valueOf(mData);

            }
            catch(Exception e)
            {
                MTPapp.showToastMessage("Socket error: " + e.getMessage());
            }
        }

        protected void onPostExecute(Integer result){
// Это выполнится после завершения работы потока
        }

        protected void onCancelled(Integer result){
// Это выполнится после завершения работы потока
        }

        protected Integer doInBackground(SocketData... param)
        {
            mySock = param[0].socket;
            mCtx = param[0].context;
            mData = new char[4096];

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(mySock.getInputStream()));
                int read = 0;

// Принимаем сообщение от сервера
// Данный цикл будет работать, пока соединение не оборвется
// или внешний поток не скажет данному cancel()
                while ((read = reader.read(mData)) >= 0 && !isCancelled())
                {
// "Вызываем" onProgressUpdate каждый раз, когда принято сообщение
                    if(read > 0) publishProgress(read);
                }
                reader.close();
            } catch (IOException e) {
                return -1;
            }
            catch (Exception e) {
                return -1;
            }
            return 0;
        }
    }



}	 
	 
	 

	
