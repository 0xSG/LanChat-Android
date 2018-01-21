package tb.sooryagangarajk.com.lanchatandroid;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import static tb.sooryagangarajk.com.lanchatandroid.MainActivity.ip;
import static tb.sooryagangarajk.com.lanchatandroid.MainActivity.mFirebaseAnalytics;
import static tb.sooryagangarajk.com.lanchatandroid.MainActivity.pushDataToList;

/**
 * Created by sooryagangarajk on 19/01/18.
 */

public class MyService extends Service {
    static String senderIp;

    @Override
    public void onCreate() {

        super.onCreate();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    final String text;
                    try {

                        int server_port = 5545;
                        byte[] message = new byte[1500];
                        DatagramPacket p = new DatagramPacket(message, message.length);
                        DatagramSocket s = new DatagramSocket(server_port);

                        s.receive(p);
                        text = new String(message, 0, p.getLength());

                        senderIp = p.getAddress().toString();

                        Bundle bundle = new Bundle();
                        bundle.putString("Device", ip);
                        bundle.putString("msg", text);
                        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {

                            @Override
                            public void run() {

                                if (!MainActivity.isMinimized)
                                    pushDataToList(senderIp.substring(1) + ":" + text);//MainActivity.msgView.setText(text);
                                else
                                    MainActivity.notify(senderIp.substring(1), text, getApplicationContext());
                            }
                        });
                        s.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        //TODO for communication return IBinder implementation
        return null;
    }
}