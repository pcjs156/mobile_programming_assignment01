package com.example.assignment01;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;

import com.example.assignment01.util.ImageUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;

public class NewProductActivity extends ActivityWithDB {
    EditText productNameEdit;

    Button prevProductImageBtn;
    Button nextProductImageBtn;

    Button productAddCancelBtn;
    Button productAddCommitBtn;
    ImageView newProductImageView;

    String filepath = "";
    String name = "";

    ArrayList<File> guitar_images;

    int imageIdx = 1;

    NewProductActivity self = this;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_new_product);

        initializeComponents();
        setOnClickListener();
        readyGuitarImageFiles();
        initProductImage();
    }

    @Override
    protected void initializeComponents() {
        productNameEdit = findViewById(R.id.productNameEdit);
        productAddCommitBtn = findViewById(R.id.productAddCommitBtn);
        productAddCancelBtn = findViewById(R.id.productAddCancelBtn);
        newProductImageView = findViewById(R.id.newProductImageView);
        nextProductImageBtn = findViewById(R.id.nextProductImageBtn);
        prevProductImageBtn = findViewById(R.id.prevProductImageBtn);
    }

    @Override
    protected void setOnClickListener() {
        productAddCommitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();

                String filepath = self.filepath;
                intent.putExtra("filepath", filepath);

                String name = productNameEdit.getText().toString();
                name = name.isEmpty() ? "이름 없음" : name;
                intent.putExtra("name", name);

                setResult(RESULT_OK, intent);
                finish();
            }
        });

        productAddCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProductManagementActivity.class);
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });

        prevProductImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageIdx--;
                if (imageIdx < 1) imageIdx = 10;
                updateImage();
            }
        });

        nextProductImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageIdx++;
                if (imageIdx > 10) imageIdx = 1;
                updateImage();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void readyGuitarImageFiles() {
        String extStorageDirectory = getExternalCacheDir().getAbsolutePath();
        File dir = new File(extStorageDirectory);
        File[] files = dir.listFiles();

        guitar_images = new ArrayList<>();
        for (File file : files) {
            String filename = file.getName();
            if (filename.startsWith("guitar") && filename.endsWith(".png")) {
                guitar_images.add(file);
            }
        }

        guitar_images.sort(new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                String name1 = o1.getName();
                String name2 = o2.getName();

                return name1.compareTo(name2);
            }
        });
    }

    private void initProductImage() {
        File firstImage = guitar_images.get(0);
        this.filepath = firstImage.getPath();
        newProductImageView.setImageBitmap(ImageUtil.compressBitmap(firstImage));
    }

    private void updateImage() {
        File image = guitar_images.get(imageIdx - 1);
        this.filepath = image.getPath();
        newProductImageView.setImageBitmap(ImageUtil.compressBitmap(image));
    }
}