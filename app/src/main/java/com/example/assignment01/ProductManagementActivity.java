package com.example.assignment01;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

    User loggedUser = null;
    boolean isGuest = false;

    ProductManagementActivity productManagementActivity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_management);
        initializeComponents();
        setOnClickListener();

        Intent intentFromMainActivity = getIntent();
        initializeUserInfo(intentFromMainActivity);
        isGuest = intentFromMainActivity.getBooleanExtra("isGuest", false);
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

        userInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(productManagementActivity);

                if (isGuest) {
                    dialog.setTitle("게스트 사용자");
                    dialog.setPositiveButton("회원가입", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                    dialog.setNegativeButton("돌아가기", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // DO NOTHING
                        }
                    });
                } else {
                    dialog.setTitle("사용자 정보");

                    StringBuilder builder = new StringBuilder();
                    builder.append("ID: " + loggedUser.getId() + "\n");

                    String name = loggedUser.getName().isEmpty() ? "UNKNOWN" : loggedUser.getName();
                    builder.append("NAME: " + name + "\n");

                    String tel = loggedUser.getTel().isEmpty() ? "UNKNOWN" : loggedUser.getTel();
                    builder.append("TEL: " + tel + "\n");

                    String addr = loggedUser.getAddress().isEmpty() ? "UNKNOWN" : loggedUser.getAddress();
                    builder.append("ADDR: " + addr);
                    dialog.setMessage(builder.toString());

                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // DO NOTHING
                        }
                    });
                }

                dialog.show();
            }
        });
    }

    private void initializeUserInfo(Intent intentFromMainActivity) {
        boolean isGuest = intentFromMainActivity.getBooleanExtra("isGuest", true);

        if (isGuest) {
            loggedUser = null;
            Toast.makeText(getApplicationContext(), "게스트 로그인", Toast.LENGTH_SHORT).show();
        } else {
            loggedUser = intentFromMainActivity.getParcelableExtra("user");
            Toast.makeText(getApplicationContext(), loggedUser.getId(), Toast.LENGTH_SHORT).show();
        }
    }
}