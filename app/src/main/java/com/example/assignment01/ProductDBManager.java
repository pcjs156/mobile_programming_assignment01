package com.example.assignment01;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ProductDBManager extends SQLiteOpenHelper {
    public ProductDBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE IF NOT EXISTS product(name TEXT, filepath TEXT)";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql = "DROP TABLE product";
        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }

    public void create(SQLiteDatabase db, String name, String filepath) {
        Log.d("PYTHON", db.toString());
        String sql = String.format("INSERT INTO product values (\"%s\", \"%s\");", name, filepath);
        db.execSQL(sql);
    }
}
