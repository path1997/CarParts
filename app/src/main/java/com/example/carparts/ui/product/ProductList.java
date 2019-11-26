package com.example.carparts.ui.product;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.carparts.R;
import com.example.carparts.RequestHandler;
import com.example.carparts.URLs;
import com.example.carparts.ui.cart.Cart;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ProductList extends AppCompatActivity {
    String category;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        Bundle extras = getIntent().getExtras();
        category=extras.getString("id");
        String category_name=extras.getString("name_cat");
        setTitle(category_name);
        getProducts();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.bt_cart) {
            Intent intent1 = new Intent(this, Cart.class);
            this.startActivity(intent1);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
    private void getProducts() {
        class Productlist extends AsyncTask<Void, Void, String> {

            ProgressBar progressBar;
            private String ida[];
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar = (ProgressBar) findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);
            }
            private ArrayList<String> arrayList;
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressBar.setVisibility(View.GONE);


                try {

                    JSONObject obj = new JSONObject(s);

                    if (!obj.getBoolean("error")) {

                        JSONArray jsonArray = obj.getJSONArray("products");

                        ida = new String[jsonArray.length()];
                        String[] name = new String[jsonArray.length()];
                        String[] path = new String[jsonArray.length()];
                        String[] price = new String[jsonArray.length()];
                        for(int i=0;i<jsonArray.length();i++) {
                            JSONObject category = jsonArray.getJSONObject(i);
                            ida[i]= category.getString("id");
                            name[i]= category.getString("name");
                            path[i]= category.getString("path");
                            price[i]= category.getString("price");
                        }

                        ProductListCustomAdapter customadapter;
                        final ListView listView=(ListView) findViewById(R.id.listview);
                        customadapter = new ProductListCustomAdapter(ProductList.this,name,path,price );
                        listView.setAdapter(customadapter);

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(getApplicationContext(), ProductDetail.class);
                                String idP=ida[position];
                                intent.putExtra("id_product", idP);
                                startActivity(intent);
                            }
                        });


                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {

                RequestHandler requestHandler = new RequestHandler();


                HashMap<String, String> params = new HashMap<>();
                params.put("id", category);


                return requestHandler.sendPostRequest(URLs.URL_PRODUCTLIST, params);
            }
        }

        Productlist ul = new Productlist();
        ul.execute();
    }
}
