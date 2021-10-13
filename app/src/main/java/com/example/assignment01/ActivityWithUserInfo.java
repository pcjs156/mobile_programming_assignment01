package com.example.assignment01;

import static com.example.assignment01.settings.Constant.dbVersion;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class ActivityWithUserInfo extends AppCompatActivity {
    protected UserInfoDBManager userInfoDBManager;
    protected SQLiteDatabase db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeDB();
    }

    private void initializeDB() {
        userInfoDBManager = new UserInfoDBManager(this, "user_info", null, dbVersion);
        try {
            db = userInfoDBManager.getWritableDatabase();
            Log.d("DB", db.toString());
        } catch (SQLiteException e) {
            e.printStackTrace();
            Log.e("DB", "데이터베이스를 얻어올 수 없음");
            finish();
        }
    }

    protected abstract void initializeComponents();
    protected abstract void setOnClickListener();
}
