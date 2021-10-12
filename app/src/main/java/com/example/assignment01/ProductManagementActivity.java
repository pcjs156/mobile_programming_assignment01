package com.example.assignment01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.assignment01.parcelable.User;

public class ProductManagementActivity extends ActivityWithUserInfoView {
    FrameLayout bottomBarContainer;

    LinearLayout normalModeBottomBtnContainer;
    Button deleteProductBtn;
    Button addProductBtn;
    Button userInfoBtn;

    LinearLayout deleteModeBottomBtnContainer;
    Button deleteCancelBtn;
    Button deleteCommitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_management);
        initializeComponents();
        setOnClickListener();

        Intent intentFromMainActivity = getIntent();
        initializeUserInfo(intentFromMainActivity);
    }

    @Override
    protected void initializeComponents() {
        bottomBarContainer = (FrameLayout) findViewById(R.id.bottomBarContainer);

        normalModeBottomBtnContainer = (LinearLayout) findViewById(R.id.normalModeBottomBtnContainer);
        deleteProductBtn = (Button) findViewById(R.id.deleteProductBtn);
        addProductBtn = (Button) findViewById(R.id.addProductBtn);
        userInfoBtn = (Button) findViewById(R.id.userInfoBtn);

        deleteModeBottomBtnContainer = (LinearLayout) findViewById(R.id.deleteModeBottomBtnContainer);
        deleteCancelBtn = (Button) findViewById(R.id.deleteCancelBtn);
        deleteCommitBtn = (Button) findViewById(R.id.deleteCommitBtn);
    }

    @Override
    protected void setOnClickListener() {
        deleteProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                normalModeBottomBtnContainer.setVisibility(View.INVISIBLE);
                deleteModeBottomBtnContainer.setVisibility(View.VISIBLE);
            }
        });

        deleteCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                normalModeBottomBtnContainer.setVisibility(View.VISIBLE);
                deleteModeBottomBtnContainer.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void initializeUserInfo(Intent intentFromMainActivity) {
        boolean isGuest = intentFromMainActivity.getBooleanExtra("isGuest", true);

        if (isGuest) {
            Toast.makeText(getApplicationContext(), "게스트 로그인", Toast.LENGTH_SHORT).show();
        } else {
            User user = intentFromMainActivity.getParcelableExtra("user");
            Toast.makeText(getApplicationContext(), user.getId(), Toast.LENGTH_SHORT).show();
        }
    }
}