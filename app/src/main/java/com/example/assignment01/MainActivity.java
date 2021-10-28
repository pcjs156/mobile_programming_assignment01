package com.example.assignment01;

import static com.example.assignment01.settings.Constant.IS_FIRST_EXEC_REFERENCE;
import static com.example.assignment01.settings.Constant.USER_INFO_PREFERENCE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.example.assignment01.parcelable.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends ActivityWithDB {

    ActivityResultLauncher<Intent> launcher;

    Button signUpBtn;
    Button loginBtn;
    Button guestLoginBtn;

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
                            Intent intent = result.getData();
                            Bundle extras = intent.getExtras();
                            User user = intent.getParcelableExtra("user");

                            updateLoginInfo(user.getId(), user.getPw());
                            fillLoginInfo();

                            Toast.makeText(getApplicationContext(), "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        fillLoginInfo();
        initializeDB();
        if (initializeProductData()) {
            Toast.makeText(getApplicationContext(), "앱이 최초로 실행되어 더미 데이터를 추가했습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    protected void initializeComponents() {
        signUpBtn = (Button) findViewById(R.id.signUpBtn);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        guestLoginBtn = (Button) findViewById(R.id.guestLoginBtn);
        editId = (EditText) findViewById(R.id.editId);
        editPassword = (EditText) findViewById(R.id.editPassword);
    }

    protected void setOnClickListener() {
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
                    Toast.makeText(getApplicationContext(), "ID와 비밀번호를 모두 입력해 주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    User loggedUser = userInfoDBManager.login(userDB, id, pw);
                    if (loggedUser == null) {
                        Toast.makeText(getApplicationContext(), "ID와 비밀번호를 확인해 주세요.", Toast.LENGTH_SHORT).show();
                    } else {
                        updateLoginInfo(loggedUser.getId(), loggedUser.getPw());

                        Intent intent = new Intent(getApplicationContext(), ProductManagementActivity.class);
                        intent.putExtra("user", loggedUser);
                        intent.putExtra("isGuest", false);
                        startActivity(intent);
                    }
                }
            }
        });

        guestLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateLoginInfo("", "");

                Intent intent = new Intent(getApplicationContext(), ProductManagementActivity.class);
                intent.putExtra("isGuest", true);
                startActivity(intent);
            }
        });
    }

    private void fillLoginInfo() {
        SharedPreferences loginInfoPref = getSharedPreferences(USER_INFO_PREFERENCE, MODE_PRIVATE);

        String id = loginInfoPref.getString("id", "");
        String pw = loginInfoPref.getString("pw", "");

        editId.setText(id);
        editPassword.setText(pw);
    }

    private void updateLoginInfo(String id, String pw) {
        SharedPreferences loginInfoPref = getSharedPreferences(USER_INFO_PREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor editor = loginInfoPref.edit();
        editor.putString("id", id);
        editor.putString("pw", pw);
        editor.commit();
    }


    private boolean initializeProductData() {
        SharedPreferences pref = getSharedPreferences(IS_FIRST_EXEC_REFERENCE, MODE_PRIVATE);
        boolean isFirstExec = pref.getBoolean("isFirstExec", true);

        if (isFirstExec) {
            final String BASE_IMG_NAME = "guitar";
            final String PACKAGE_NAME = getPackageName();
            String extStorageDirectory = getExternalCacheDir().getAbsolutePath();

            for (int i = 1; i <= 10; i++) {
                String resName = BASE_IMG_NAME + i;
                int resId;

                try {
                    resId = getResources().getIdentifier(resName, "drawable", PACKAGE_NAME);

                    Bitmap bm = BitmapFactory.decodeResource(getResources(), resId);

                    File storage = getCacheDir();
                    String filename = resName + ".png";
                    File tempFile = new File(extStorageDirectory + "/" + filename);
                    tempFile.createNewFile();

                    FileOutputStream out = new FileOutputStream(tempFile);
                    bm.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.close();
                } catch (Resources.NotFoundException ex) {
                    finish();
                    break;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("isFirstExec", false);
            editor.commit();
        }

        return isFirstExec;
    }
}