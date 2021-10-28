package com.example.assignment01.adapter;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.assignment01.ProductManagementActivity;
import com.example.assignment01.R;
import com.example.assignment01.util.ImageUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends BaseAdapter {
    public List<Bundle> products = new ArrayList<>();

    public ProductAdapter self = this;

    public ProductAdapter(ArrayList<Bundle> products) {
        for (Bundle product : products) {
            this.products.add(product);
        }
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int i) {
        return products.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.product_item, viewGroup, false);

        TextView productIdView = itemView.findViewById(R.id.productId);
        productIdView.setText(Integer.toString(((Bundle) getItem(i)).getInt("id")));

        TextView textView = itemView.findViewById(R.id.productName);
        textView.setText(((Bundle) getItem(i)).getString("name"));

        ImageView imageView = itemView.findViewById(R.id.productImage);
        File image = new File(((Bundle) getItem(i)).getString("filepath"));
        Bitmap imageBitmap = ImageUtil.compressBitmap(image);
        imageView.setImageBitmap(imageBitmap);

        itemView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O_MR1)
            @Override
            public void onClick(View v) {
                if (ProductManagementActivity.deleteMode) {
                    TextView productIdView = (TextView) v.findViewById(R.id.productId);
                    int id = Integer.parseInt(productIdView.getText().toString());

                    ArrayList<Bundle> newProducts = new ArrayList<>();
                    for (Bundle item : products) {
                        if (item.getInt("id") == id) {
                            String DB_PATH = "/data/data/com.example.assignment01/databases/product";
                            File DB = new File(DB_PATH);

                            SQLiteDatabase.OpenParams params = new SQLiteDatabase.OpenParams.Builder().build();
                            SQLiteDatabase db = SQLiteDatabase.openDatabase(DB, params);
                            db.execSQL("DELETE FROM product WHERE id=" + id);
                            db.close();
                        } else {
                            newProducts.add(item);
                        }
                    }
                    self.products = newProducts;
                    self.notifyDataSetChanged();
                }
            }
        });

        return itemView;
    }

    public View getView(int i, View view, ViewGroup viewGroup, ArrayList<Bundle> newProducts) {
        View itemView = this.getView(i, view, viewGroup);

        return itemView;
    }
}
