package com.example.carparts;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MyOrderDetail extends AppCompatActivity {
    String id_order;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order_detail);
        Bundle extras = getIntent().getExtras();
        id_order=extras.getString("idOrder");
        getMyOrDerdetail();

    }
    void getMyOrDerdetail(){
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
                        JSONArray jsonArray = obj.getJSONArray("orderdetail");

                        int[] idz = new int[jsonArray.length()];
                        String[] cid = new String[jsonArray.length()];
                        String[] name = new String[jsonArray.length()];
                        String[] path = new String[jsonArray.length()];
                        int[] price = new int[jsonArray.length()];
                        int[] quantity = new int[jsonArray.length()];
                        //suma=new int[jsonArray.length()];
                        System.out.println("elo");
                        String totalcost="",date="",payment="";
                        for(int i=0;i<jsonArray.length();i++) {
                            JSONObject category = jsonArray.getJSONObject(i);
                            System.out.println(category.getString("price"));
                            idz[i]= category.getInt("id");
                            cid[i]= category.getString("cid");
                            name[i]= category.getString("name");
                            path[i]= category.getString("path");
                            price[i]= category.getInt("price");
                            quantity[i]= category.getInt("quantity");
                            totalcost=category.getString("totalcost");
                            date=category.getString("date_of_order");
                            payment=category.getString("payment");
                        }

                        CartProductListCustomAdapter customadapter;
                        final ListView listView=(ListView) findViewById(R.id.listvieworderdetail);
                        customadapter = new CartProductListCustomAdapter(MyOrderDetail.this,cid,idz,name,path,price,quantity,1 );
                        listView.setAdapter(customadapter);

                        TextView txdate=(TextView) findViewById(R.id.orderdate);
                        txdate.setText("Date of order: "+date.substring(0, 16));
                        TextView totalcosttx=(TextView) findViewById(R.id.ordercost);
                        totalcosttx.setText("Total cost: "+totalcost+"zł");
                        TextView paymentTx=(TextView) findViewById(R.id.orderpayment);
                        if (payment.equals("1")) {
                            paymentTx.setText("Method of Payment: Paid by Paypal");
                        } else {
                            paymentTx.setText("Method of Payment: Paid on delivery");
                        }

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
                params.put("order_id", id_order);

                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_GETMYORDERDETAIL, params);
            }
        }

        UserLogin ul = new UserLogin();
        ul.execute();
    }
}
