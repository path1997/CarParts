package com.example.carparts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ProductList extends AppCompatActivity {
    String category;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        //textView= findViewById(R.id.textView2);
        Bundle extras = getIntent().getExtras();
        category=extras.getString("name_cat");
        //textView.setText(category);
        getProducts();
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
                    JSONObject obj = new JSONObject(s);

                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        //Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                        //getting the user from the response
                        JSONArray jsonArray = obj.getJSONArray("products");

                        String[] name = new String[jsonArray.length()];
                        String[] path = new String[jsonArray.length()];
                        String[] price = new String[jsonArray.length()];
                        for(int i=0;i<jsonArray.length();i++) {
                            JSONObject category = jsonArray.getJSONObject(i);
                            name[i]= category.getString("name");
                            path[i]= category.getString("path");
                            price[i]= category.getString("price");
                        }

                        /*for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject category = jsonArray.getJSONObject(i);

                            String name = category.getString("name");
                            int price = category.getInt("price");
                            String description = category.getString("description");
                            String photo = category.getString("path");
                            //textView.setText("name" + name);
                            System.out.println("name" + name);
                            System.out.println("price" + price);
                            System.out.println("description" + description);
                            System.out.println("photo" + photo);
                            System.out.println();

                        }*/
                        ProductListCustomAdapter customadapter;
                        final ListView listView=(ListView) findViewById(R.id.listview);
                        customadapter = new ProductListCustomAdapter(ProductList.this,name,path,price );
                        listView.setAdapter(customadapter);

                        //arrayList = new ArrayList<String>();
                        /*for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject category = jsonArray.getJSONObject(i);

                            String name = category.getString("name");
                            arrayList.add(name);
                        }
                        ArrayAdapter<String> aAdaptor = new ArrayAdapter<String>(getApplicationContext(), R.layout.custom_layout, arrayList ) {

                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {

                                TextView textView = (TextView) super.getView(position, convertView, parent);

                                return textView;
                            }
                        };
                        listView.setAdapter( aAdaptor );
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(getApplicationContext(), ProductList.class);
                                String item = arrayList.get(position);
                                intent.putExtra("name_cat", item);
                                startActivity(intent);
                            }
                        });
*/

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

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("name_category", category);

                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_PRODUCTLIST, params);
            }
        }

        UserLogin ul = new UserLogin();
        ul.execute();
    }
}
