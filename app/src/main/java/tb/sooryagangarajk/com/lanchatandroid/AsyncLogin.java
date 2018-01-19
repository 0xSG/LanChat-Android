package tb.sooryagangarajk.com.lanchatandroid;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;



/**
 * Created by sooryagangarajk on 19/01/18.
 */

public class AsyncLogin extends AsyncTask<String, String, String> {


    @Override
    protected String doInBackground(String... strings) {

        try {
            String messageStr = MainActivity.msgGot.getText().toString();

            int server_port = 5545;
            DatagramSocket s = new DatagramSocket();
            InetAddress local = InetAddress.getByName(MainActivity.ipGot.getText().toString());
            int msg_length = messageStr.length();
            byte[] message = messageStr.getBytes();
            DatagramPacket p = new DatagramPacket(message, msg_length, local, server_port);
            s.send(p);

            s.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;


    }

    @Override
    protected void onPostExecute(String s) {
        MainActivity.msgGot.setText("");
        super.onPostExecute(s);
    }
}