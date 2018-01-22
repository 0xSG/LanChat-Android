package tb.sooryagangarajk.com.lanchatandroid;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static boolean isMinimized = false;
    public static EditText msgGot;
    public static EditText ipGot;
    public static Button sendButton;
    public static Intent notiInt = null;
    public static ListView listView;
    public static TextView myIp;
    public static Context myContext;
    public static ArrayAdapter<String> stringArrayAdapter;
    public static ArrayList<String> arrayList;
    public static FirebaseAnalytics mFirebaseAnalytics;
    public static String ip = null;
    public static DatabaseHandler db;



    @Override
    protected void onResume() {
        isMinimized = false;
        Log.d("sgk","onResume called");
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


         db = new DatabaseHandler(this);
        Log.d("sgk","onCreate called");

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        ipGot = findViewById(R.id.ipId);
        msgGot = findViewById(R.id.msgId);
        sendButton = findViewById(R.id.sbtnid);
        listView = findViewById(R.id.mylist);
        myIp = (TextView) findViewById(R.id.myipid);
        myContext = getApplicationContext();



        new Thread(new Runnable() {
            @Override
            public void run() {
                WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
                ip = null;
                if (wm != null) {
                    ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
                }
                myIp.setText(ip);
            }
        }).start();

        notiInt = new Intent(getApplicationContext(), MyService.class);
        getApplicationContext().startService(notiInt);

        arrayList = new ArrayList<String>();

        if(db.isDBHaveData()){
            arrayList=db.getAllData();
            Log.d("sgk","isDBHaveData called restored data"+arrayList);

        }
        stringArrayAdapter = new ArrayAdapter<String>(myContext, R.layout.custom_row, R.id.textView, arrayList);
        listView.setAdapter(stringArrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String string = arrayList.get(position).split(":")[0];
                ipGot.setText((string.equals("You") ? "127.0.0.1" : string));
            }
        });
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncLogin obj = new AsyncLogin();
                obj.execute();

                Bundle bundle = new Bundle();
                bundle.putString("Device", ip);
                bundle.putString("msg", msgGot.getText().toString());
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                pushDataToList("You:" + msgGot.getText().toString());
            }
        });
    }

    public static void pushDataToList(String s) {
        db.addData(s);
        arrayList.add(s);
        listView.setAdapter(stringArrayAdapter);
    }

    @Override
    protected void onPause() {
        Log.d("sgk","onPause called");
        isMinimized = true;
        super.onPause();
    }

    public static void notify(String nIp, String nMsg, Context context) {

        pushDataToList(nIp + ":" + nMsg);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(nIp)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentText(nMsg);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, mBuilder.build());
    }

}
