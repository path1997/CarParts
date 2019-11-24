package com.example.carparts.ui.cart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carparts.ui.delivery.Delivery;
import com.example.carparts.ui.product.ProductDetail;
import com.example.carparts.R;
import com.example.carparts.RequestHandler;
import com.example.carparts.SharedPrefManager;
import com.example.carparts.URLs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Cart extends AppCompatActivity {
    int[] idz;
    int suma=0;
    int pusty;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        pusty=1;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        getProducts();
        findViewById(R.id.btdelivery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn()){
                    Toast.makeText(getApplicationContext(), "You must be logged in", Toast.LENGTH_SHORT).show();
                } else if(pusty==1) {
                    Toast.makeText(getApplicationContext(), "Cart is empty", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), Delivery.class);
                    String suma1 = Integer.toString(suma);
                    intent.putExtra("suma", suma1);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
    @Override
    public void onRestart() {
        super.onRestart();
        this.recreate();
        suma=0;
        pusty=1;
    }
    public void getProducts() {
        pusty=1;
        class Cartc extends AsyncTask<Void, Void, String> {

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

                        JSONArray jsonArray = obj.getJSONArray("cart");

                        idz = new int[jsonArray.length()];
                        String[] cid = new String[jsonArray.length()];
                        String[] name = new String[jsonArray.length()];
                        String[] path = new String[jsonArray.length()];
                        int[] price = new int[jsonArray.length()];
                        int[] quantity = new int[jsonArray.length()];
                        System.out.println("elo");
                        for(int i=0;i<jsonArray.length();i++) {
                            JSONObject cart = jsonArray.getJSONObject(i);
                            pusty=0;
                            System.out.println(cart.getString("price"));
                            idz[i]= cart.getInt("id");
                            cid[i]= cart.getString("cid");
                            name[i]= cart.getString("name");
                            path[i]= cart.getString("path");
                            price[i]= cart.getInt("price");
                            quantity[i]= cart.getInt("quantity");
                            suma+=price[i]*quantity[i];
                        }


                        CartProductListCustomAdapter customadapter;
                        final ListView listView=(ListView) findViewById(R.id.listviewcart);
                        customadapter = new CartProductListCustomAdapter(Cart.this,cid,idz,name,path,price,quantity,0 );
                        listView.setAdapter(customadapter);
                        TextView wartosczamowienia=(TextView) findViewById(R.id.Wartosc);
                        wartosczamowienia.setText("Total order value : "+suma+"zł");
                        wartosczamowienia.setGravity(Gravity.CENTER);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(getApplicationContext(), ProductDetail.class);
                                String idP=Long.toString(idz[position]);
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
                int id= SharedPrefManager.getInstance(getApplicationContext()).getUser().getId();
                String ids=Integer.toString(id);

                HashMap<String, String> params = new HashMap<>();
                params.put("user_id", ids);

                return requestHandler.sendPostRequest(URLs.URL_GETCART, params);
            }
        }

        Cartc ul = new Cartc();
        ul.execute();
    }
}
