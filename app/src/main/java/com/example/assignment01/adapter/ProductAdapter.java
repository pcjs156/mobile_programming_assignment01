package com.example.assignment01.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.assignment01.ProductDBManager;
import com.example.assignment01.ProductManagementActivity;
import com.example.assignment01.R;
import com.example.assignment01.util.ImageUtil;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.Array;
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

        TextView textView = itemView.findViewById(R.id.productName);
        textView.setText(((Bundle) getItem(i)).getString("name"));

        ImageView imageView = itemView.findViewById(R.id.productImage);
        File image = new File(((Bundle) getItem(i)).getString("filepath"));
        Bitmap imageBitmap = ImageUtil.compressBitmap(image);
        imageView.setImageBitmap(imageBitmap);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ProductManagementActivity.deleteMode) {
                    TextView nameView = (TextView) v.findViewById(R.id.productName);
                    String productName = nameView.getText().toString();

                    ArrayList<Bundle> newProducts = new ArrayList<>();
                    for (Bundle item : products) {
                        if (!item.getString("name").equals(productName))
                            newProducts.add(item);
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
