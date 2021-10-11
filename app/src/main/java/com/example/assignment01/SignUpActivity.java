package com.example.assignment01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.assignment01.parcelable.User;
import com.example.assignment01.util.MessageBox;

public class SignUpActivity extends ActivityWithUserInfoView {
    EditText idEdit;
    EditText passwordEdit;
    EditText passwordAgainEdit;
    EditText nameEdit;
    EditText telEdit;
    EditText addressEdit;

    Button idCheckBtn;
    Button passwordCheckBtn;
    Button signUpCompleteBtn;

    RadioGroup TCRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        linkComponents();
        setOnClickListener();
    }

    private void linkComponents() {
        TCRadioGroup = (RadioGroup) findViewById(R.id.TCRadioGroup);

        idEdit = (EditText) findViewById(R.id.idEdit);
        passwordEdit = (EditText) findViewById(R.id.passwordEdit);
        passwordAgainEdit = (EditText) findViewById(R.id.passwordAgainEdit);
        nameEdit = (EditText) findViewById(R.id.nameEdit);
        telEdit = (EditText) findViewById(R.id.telEdit);
        addressEdit = (EditText) findViewById(R.id.addressEdit);

        idCheckBtn = (Button) findViewById(R.id.idCheckBtn);
        passwordCheckBtn = (Button) findViewById(R.id.passwordCheckBtn);
        signUpCompleteBtn = (Button) findViewById(R.id.signUpCompleteBtn);
    }

    private void setOnClickListener() {
        signUpCompleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 이용 약관에 동의하지 않은 경우 회원가입을 수행하지 않음
                if (TCRadioGroup.getCheckedRadioButtonId() == R.id.TCDecline) {
                    Toast.makeText(getApplicationContext(), "이용 약관에 동의해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                String id = idEdit.getText().toString().trim();
                String password = passwordEdit.getText().toString().trim();
                String passwordAgain = passwordAgainEdit.getText().toString().trim();
                String name = nameEdit.getText().toString().trim();
                String tel = nameEdit.getText().toString().trim();
                String address = addressEdit.getText().toString().trim();

                MessageBox msg = new MessageBox("");
                boolean isCreated = userInfoDBManager.create(db, msg, id, password, passwordAgain,
                        name, tel, address);

                if (isCreated) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("user", new User(id, password, name, tel, address));
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), msg.getBody(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}