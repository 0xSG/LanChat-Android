package tb.sooryagangarajk.com.lanchatandroid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sooryagangarajk on 22/01/18.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "LanChatStore";

    // SGList table name
    private static final String TABLE_LISTVIEW = "listView";

    // SG Table Columns names
    private static final String KEY_DATA = "data";
    private static final String KEY_ID = "id";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LIST_TABLE = "CREATE TABLE " + TABLE_LISTVIEW + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_DATA + " TEXT)";
        db.execSQL(CREATE_LIST_TABLE);
    }

    public void addData(String s) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DATA, s);
        db.insert(TABLE_LISTVIEW, null, values);
        db.close();

    }

    public boolean isDBHaveData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_LISTVIEW;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst())
            return true;
        else
            return false;

    }

    public ArrayList<String> getAllData() {
        ArrayList<String> SG_DATA_List = new ArrayList<String>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_LISTVIEW;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                String data = cursor.getString(1);

                // Adding contact to list
                SG_DATA_List.add(data);
            } while (cursor.moveToNext());
        }

        // return contact list
        return SG_DATA_List;
    }



    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LISTVIEW);

        // Create tables again
        onCreate(db);
    }
}