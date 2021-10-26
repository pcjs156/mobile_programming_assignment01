package com.example.assignment01;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class ProductDBManager extends SQLiteOpenHelper {
    public ProductDBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE IF NOT EXISTS product(id integer primary key autoincrement, name TEXT, filepath TEXT)";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql = "DROP TABLE product";
        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }

    public void create(SQLiteDatabase db, String name, String filepath) {
        String sql = String.format("INSERT INTO product (name, filepath) values (\"%s\", \"%s\");", name, filepath);
        db.execSQL(sql);
    }

    public ArrayList<Bundle> get(SQLiteDatabase db) {
        String sql = "SELECT id, name, filepath FROM product;";
        Cursor c = db.rawQuery(sql, null);

        ArrayList<Bundle> result = new ArrayList<>();
        while (c.moveToNext()) {
            Bundle product = new Bundle();
            product.putInt("id", c.getInt(0));
            product.putString("name", c.getString(1));
            product.putString("filepath", c.getString(2));
            result.add(product);
        }

        return result;
    }
}
