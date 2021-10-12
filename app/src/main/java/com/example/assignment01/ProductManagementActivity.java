package com.example.assignment01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.assignment01.parcelable.User;

public class ProductManagementActivity extends ActivityWithUserInfoView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_management);

        Intent intentFromMainActivity = getIntent();
        boolean isGuest = intentFromMainActivity.getBooleanExtra("isGuest", true);

        if (isGuest) {
            Toast.makeText(getApplicationContext(), "게스트 로그인", Toast.LENGTH_SHORT).show();
        } else {
            User user = intentFromMainActivity.getParcelableExtra("user");
            Toast.makeText(getApplicationContext(), user.getId(), Toast.LENGTH_SHORT).show();
        }
    }
}