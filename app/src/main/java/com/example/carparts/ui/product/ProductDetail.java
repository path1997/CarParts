package com.example.carparts.ui.product;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.carparts.R;
import com.example.carparts.RequestHandler;
import com.example.carparts.SharedPrefManager;
import com.example.carparts.SliderAdapter;
import com.example.carparts.URLs;
import com.example.carparts.ui.cart.Cart;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ProductDetail extends AppCompatActivity {
    String id_product;
    ViewFlipper v_flipper;
    EditText ilosc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        Bundle extras = getIntent().getExtras();
        id_product=extras.getString("id_product");
        ilosc=(EditText) findViewById(R.id.Ilosc);
        getDetails();
        findViewById(R.id.btBuy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToCart();
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.cart_menu, menu);
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
    private void getDetails() {
        class Productdetail extends AsyncTask<Void, Void, String> {
            SliderView sliderView;
            ProgressBar progressBar;

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

                        JSONArray jsonArray = obj.getJSONArray("productdetail");

                        String[] name = new String[jsonArray.length()];
                        String[] description = new String[jsonArray.length()];
                        String[] price = new String[jsonArray.length()];
                        for(int i=0;i<jsonArray.length();i++) {
                            JSONObject productdetail = jsonArray.getJSONObject(i);
                            name[i]= productdetail.getString("name");
                            description[i]= productdetail.getString("description");
                            price[i]= productdetail.getString("price");
                        }
                        setTitle(name[0]);
                        jsonArray = obj.getJSONArray("productphotos");

                        String[] path = new String[jsonArray.length()];
                        for(int i=0;i<jsonArray.length();i++) {
                            JSONObject category = jsonArray.getJSONObject(i);
                            path[i]= category.getString("path");

                        }
                        System.out.println("WIELKOSC"+ jsonArray.length());
                        SliderView sliderView = findViewById(R.id.imageSlider);

                        final SliderAdapter adapter = new SliderAdapter(getApplicationContext());
                        adapter.setCount(jsonArray.length());
                        adapter.setPath(path);

                        sliderView.setSliderAdapter(adapter);
                        sliderView.setAutoCycle(false);

                        TextView tvTitle= (TextView) findViewById(R.id.tvTitle);
                        TextView tvDescription= (TextView) findViewById(R.id.tvDesc);
                        TextView tvPrice= (TextView) findViewById(R.id.tvPrice);
                        tvTitle.setText(name[0]);
                        tvDescription.setText(description[0]);
                        tvPrice.setText("Cena: "+price[0]+"zł");


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
                params.put("productid", id_product);


                return requestHandler.sendPostRequest(URLs.URL_PRODUCTDETAIL, params);
            }
        }

        Productdetail ul = new Productdetail();
        ul.execute();
    }
    private void addToCart() {


        class Productdetail extends AsyncTask<Void, Void, String> {
            ProgressBar progressBar;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar = (ProgressBar) findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                try {
                    JSONObject obj = new JSONObject(s);

                    if (!obj.getBoolean("error")) {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        finish();


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
               String ilosc1=ilosc.getText().toString();
                int id= SharedPrefManager.getInstance(getApplicationContext()).getUser().getId();
                String ids=Integer.toString(id);
                System.out.println(ids);
                System.out.println(ilosc1);
                System.out.println(id_product);
                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("quantity",ilosc1);
                params.put("id_product", id_product);
                params.put("id_user",ids);

                return requestHandler.sendPostRequest(URLs.URL_ADDPRODUCT, params);
            }
        }

        Productdetail ul = new Productdetail();
        ul.execute();
    }
}
