package com.example.assignment01.activity;

import static com.example.assignment01.settings.Constant.dbVersion;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.assignment01.db.UserInfoDBManager;

public abstract class ActivityWithUserInfo extends AppCompatActivity {
    protected UserInfoDBManager userInfoDBManager;
    protected SQLiteDatabase userDB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeDB();
    }

    protected void initializeDB() {
        userInfoDBManager = new UserInfoDBManager(this, "user_info", null, dbVersion);
        try {
            userDB = userInfoDBManager.getWritableDatabase();
        } catch (SQLiteException e) {
            e.printStackTrace();
            finish();
        }
    }

    protected abstract void initializeComponents();
    protected abstract void setOnClickListener();
}
