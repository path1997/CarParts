package com.example.carparts;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class ProductList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        TextView textView= findViewById(R.id.textView2);
        Bundle extras = getIntent().getExtras();
        textView.setText(extras.getString("name_cat"));
    }
}
