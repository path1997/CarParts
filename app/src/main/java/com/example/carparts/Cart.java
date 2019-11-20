package com.example.carparts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Cart extends AppCompatActivity {
    int[] idz;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        getProducts();
    }
    @Override
    public void onRestart() {
        super.onRestart();
        this.recreate();
    }
    private void getProducts() {
        class UserLogin extends AsyncTask<Void, Void, String> {

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
                    //converting response to json object
                    //System.out.println("przed");
                    JSONObject obj = new JSONObject(s);
                    //System.out.println("za");
                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        //Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                        //getting the user from the response
                        JSONArray jsonArray = obj.getJSONArray("cart");

                        idz = new int[jsonArray.length()];
                        String[] name = new String[jsonArray.length()];
                        String[] path = new String[jsonArray.length()];
                        int[] price = new int[jsonArray.length()];
                        int[] quantity = new int[jsonArray.length()];
                        System.out.println("elo");
                        for(int i=0;i<jsonArray.length();i++) {
                            JSONObject category = jsonArray.getJSONObject(i);
                            System.out.println(category.getString("price"));
                            idz[i]= category.getInt("id");
                            name[i]= category.getString("name");
                            path[i]= category.getString("path");
                            price[i]= category.getInt("price");
                            quantity[i]= category.getInt("quantity");
                        }



                        CartProductListCustomAdapter customadapter;
                        final ListView listView=(ListView) findViewById(R.id.listviewcart);
                        customadapter = new CartProductListCustomAdapter(Cart.this,idz,name,path,price,quantity );
                        listView.setAdapter(customadapter);

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
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();
                int id=SharedPrefManager.getInstance(getApplicationContext()).getUser().getId();
                String ids=Integer.toString(id);
                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("user_id", ids);

                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_GETCART, params);
            }
        }

        UserLogin ul = new UserLogin();
        ul.execute();
    }
}
