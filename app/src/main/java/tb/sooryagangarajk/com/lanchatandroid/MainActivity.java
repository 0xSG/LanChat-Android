package tb.sooryagangarajk.com.lanchatandroid;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static boolean isMinimized = false;
    public static TextView msgView;
    public static EditText msgGot;
    public static EditText ipGot;
    public static Button sendButton;
    public static Intent notiInt = null;
    public static ListView listView;
    public static Context myContext;
    public static ArrayAdapter<String> stringArrayAdapter;
    public static ArrayList<String> arrayList;


    @Override
    protected void onResume() {
        isMinimized = false;


        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ipGot = findViewById(R.id.ipId);
        msgGot = findViewById(R.id.msgId);
        sendButton = findViewById(R.id.sbtnid);
        listView = findViewById(R.id.mylist);
        myContext=getApplicationContext();

        notiInt = new Intent(getApplicationContext(), MyService.class);
        getApplicationContext().startService(notiInt);

        arrayList = new ArrayList<String>();

        stringArrayAdapter = new ArrayAdapter<String>(myContext, R.layout.custom_row, R.id.textView, arrayList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String string =arrayList.get(position).split(":")[0];
                ipGot.setText((string.equals("You")?"127.0.0.1":string));
            }
        });


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncLogin obj=new AsyncLogin();
                obj.execute();

                pushDataToList("You:"+msgGot.getText().toString());



            }
        });


    }

    public static void pushDataToList(String s) {
        arrayList.add(s);
        listView.setAdapter(stringArrayAdapter);


    }

    @Override
    protected void onPause() {
        isMinimized = true;

        super.onPause();
    }


    public static void notify(String nIp, String nMsg, Context context) {

        pushDataToList(nIp+":"+nMsg);
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
