package com.example.assignment01;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import static com.example.assignment01.settings.Constant.dbVersion;

import com.example.assignment01.parcelable.User;

public class MainActivity extends ActivityWithUserInfoView {
    ActivityResultLauncher<Intent> launcher;

    Button signUpBtn;
    Button loginBtn;

    EditText editId;
    EditText editPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeComponents();
        setOnClickListener();

        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            Toast.makeText(getApplicationContext(), "회원가입이 완료되었습니다.", Toast.LENGTH_LONG).show();

                            Intent intent = result.getData();
                            Bundle extras = intent.getExtras();
                            User user = intent.getParcelableExtra("user");

                            editId.setText(user.getId());
                            editPassword.setText(user.getPw());
                        }
                    }
                });
    }

    private void initializeComponents() {
        signUpBtn = (Button) findViewById(R.id.signUpBtn);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        editId = (EditText) findViewById(R.id.editId);
        editPassword = (EditText) findViewById(R.id.editPassword);
    }

    private void setOnClickListener() {
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                launcher.launch(intent);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = editId.getText().toString();
                String pw = editPassword.getText().toString();

                if (id.length() == 0 || pw.length() == 0) {
                    Toast.makeText(getApplicationContext(), "ID와 비밃번호를 모두 입력해 주세요.", Toast.LENGTH_LONG).show();
                } else {
                    User loggedUser = userInfoDBManager.login(db, id, pw);
                    if (loggedUser == null) {
                        Toast.makeText(getApplicationContext(), "ID와 비밀번호를 확인해 주세요.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), ProductManagementActivity.class);
                        intent.putExtra("user", loggedUser);
                        startActivity(intent);
                    }
                }
            }
        });
    }
}