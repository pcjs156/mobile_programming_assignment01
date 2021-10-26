package com.example.assignment01;

import static com.example.assignment01.settings.Constant.dbVersion;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

public abstract class ActivityWithDB extends ActivityWithUserInfo {
    protected ProductDBManager productDBManager;
    protected SQLiteDatabase productDB;

    @Override
    protected void initializeDB() {
        super.initializeDB();

        productDBManager = new ProductDBManager(this, "product", null, dbVersion);
        try {
            productDB = productDBManager.getWritableDatabase();
        } catch (SQLiteException e) {
            e.printStackTrace();
            finish();
        }
    }
}
