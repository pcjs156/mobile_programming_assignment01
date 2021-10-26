package com.example.assignment01;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.assignment01.adapter.ProductAdapter;
import com.example.assignment01.parcelable.User;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class ProductManagementActivity extends ActivityWithDB {
    private static final int ADD_PRODUCT = 1;

    ListView productContainer;
    ProductAdapter productAdapter;

    FrameLayout bottomBarContainer;

    LinearLayout normalModeBottomBtnContainer;
    Button deleteProductBtn;
    Button addProductBtn;
    Button userInfoBtn;

    LinearLayout deleteModeBottomBtnContainer;
    Button deleteCancelBtn;

    User loggedUser = null;
    boolean isGuest = false;

    ProductManagementActivity self = this;

    public static boolean deleteMode = false;

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
        productContainer = (ListView) findViewById(R.id.productContainer);
        bottomBarContainer = (FrameLayout) findViewById(R.id.bottomBarContainer);

        normalModeBottomBtnContainer = (LinearLayout) findViewById(R.id.normalModeBottomBtnContainer);
        deleteProductBtn = (Button) findViewById(R.id.deleteProductBtn);
        addProductBtn = (Button) findViewById(R.id.addProductBtn);
        userInfoBtn = (Button) findViewById(R.id.userInfoBtn);

        deleteModeBottomBtnContainer = (LinearLayout) findViewById(R.id.deleteModeBottomBtnContainer);
        deleteCancelBtn = (Button) findViewById(R.id.deleteCancelBtn);

        initializeProductContainer();
    }

    @Override
    protected void setOnClickListener() {
        deleteProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isGuest) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(self);
                    dialog.setTitle("회원만 사용할 수 있는 기능입니다.");
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
                    dialog.show();
                } else {
                    deleteMode = true;
                    normalModeBottomBtnContainer.setVisibility(View.INVISIBLE);
                    deleteModeBottomBtnContainer.setVisibility(View.VISIBLE);
                }
            }
        });

        deleteCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteMode = false;

                normalModeBottomBtnContainer.setVisibility(View.VISIBLE);
                deleteModeBottomBtnContainer.setVisibility(View.INVISIBLE);
            }
        });

        userInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(self);

                if (isGuest) {
                    dialog.setTitle("회원만 사용할 수 있는 기능입니다.");
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

        addProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isGuest) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(self);
                    dialog.setTitle("회원만 사용할 수 있는 기능입니다.");
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
                    dialog.show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), NewProductActivity.class);
                    startActivityForResult(intent, ADD_PRODUCT);
                }
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

    private void initializeProductContainer() {
        createInitialProducts();

        ArrayList<Bundle> products = productDBManager.get(productDB);
        productAdapter = new ProductAdapter(products);
        productContainer.setAdapter(productAdapter);
    }

    private void createInitialProducts() {
        SharedPreferences prefs = getSharedPreferences("product", MODE_PRIVATE);
        boolean isInitialProductCreated = prefs.getBoolean("isInitialProductCreated", false);

        if (!isInitialProductCreated) {
            // 처음에 몇 개의 제품을 준비해놓을 것인지 결정
            final int INITIAL_PRODUCT_CNT = 7;

            final String BASE_IMG_NAME = "guitar";
            final String PACKAGE_NAME = getPackageName();
            String extStorageDirectory = getExternalCacheDir().getAbsolutePath();

            File directory = new File(extStorageDirectory);
            File[] files = directory.listFiles();
            // 미리 준비된 기타 이미지만 남김
            ArrayList<File> guitar_images = new ArrayList<>();
            for (File file : files) {
                if (file.getName().endsWith(".png") && file.getName().startsWith("guitar"))
                    guitar_images.add(file);
            }

            // 이미지를 섞어줌
            Random r = new Random();
            final int IMG_CNT = guitar_images.size();
            for (int i = 0; i < IMG_CNT * 2; i++) {
                int idx1 = r.nextInt(IMG_CNT);
                int idx2 = r.nextInt(IMG_CNT);

                File f1 = guitar_images.get(idx1);
                File f2 = guitar_images.get(idx2);

                guitar_images.set(idx1, f2);
                guitar_images.set(idx2, f1);
            }

            for (int i = 0; i < INITIAL_PRODUCT_CNT; i++) {
                File image = guitar_images.get(i);
                productDBManager.create(productDB, "기타 " + (i + 1), image.getPath());
            }
        }

        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("isInitialProductCreated", true);
        editor.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_PRODUCT) {
            if (resultCode == RESULT_OK) {
                String filepath = data.getStringExtra("filepath");
                String name = data.getStringExtra("name");

                productDBManager.create(productDB, name, filepath);
                Bundle newInfo = new Bundle();
                newInfo.putString("name", name);
                newInfo.putString("filepath", filepath);

                productAdapter.products.add(0, newInfo);
                productAdapter.notifyDataSetChanged();

                Toast.makeText(getApplicationContext(), "제품 추가가 완료되었습니다.", Toast.LENGTH_SHORT).show();
            } else if (requestCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "제품 추가를 취소하셨습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}