package com.example.myapplication_database;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

class dbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "mycontacts.db";
    private static final int DATABASE_VERSION = 2;

    public dbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE contact ( _id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name TEXT, tel TEXT);");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS contact");
        onCreate(db);
    }
}

public class MainActivity extends AppCompatActivity {
    dbHelper helper;
    SQLiteDatabase db;
    EditText edit_name, edit_tel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setContentView(R.layout.main);
        helper = new dbHelper(this);
        try {
            db = helper.getWritableDatabase();
        } catch (SQLiteException ex) {
            db = helper.getReadableDatabase();
        }

        edit_name = (EditText) findViewById(R.id.name);
        edit_tel = (EditText) findViewById(R.id.tel);
        TextView list = (TextView) findViewById(R.id.list);

        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String name = edit_name.getText().toString();
                String tel = edit_tel.getText().toString();
                db.execSQL("INSERT INTO contact VALUES (null, '" + name
                        + "', '" + tel + "');");

                list.append("\n" + name + " " + tel);

            }
        });
        findViewById(R.id.query).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String name = edit_name.getText().toString();
                Cursor cursor;
                list.setText("");

                if(name.isEmpty()) {
                    cursor = db.rawQuery(
                            "SELECT name, tel FROM contact ;", null);

                } else {
                    cursor = db.rawQuery(
                            "SELECT name, tel FROM contact WHERE name='" + name
                                    + "';", null);
                }
                while (cursor.moveToNext()) {
                    String nam = cursor.getString(0);
                    String tel = cursor.getString(1);
                    //edit_tel.setText(tel);

                    list.append("\n" + nam + " " + tel + "\n");

                }
            }
        });
    }
}