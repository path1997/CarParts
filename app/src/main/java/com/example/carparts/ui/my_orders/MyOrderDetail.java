package com.example.carparts.ui.my_orders;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carparts.R;
import com.example.carparts.RequestHandler;
import com.example.carparts.SharedPrefManager;
import com.example.carparts.URLs;
import com.example.carparts.ui.cart.CartProductListCustomAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MyOrderDetail extends AppCompatActivity {
    String id_order;
    ArrayList<Integer> idz=new ArrayList<Integer>();
    ArrayList<String> cid=new ArrayList<String>();
    ArrayList<String> name=new ArrayList<String>();
    ArrayList<String> path=new ArrayList<String>();
    ArrayList<Integer> price=new ArrayList<Integer>();
    ArrayList<Integer> quantity=new ArrayList<Integer>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order_detail);
        Bundle extras = getIntent().getExtras();
        id_order=extras.getString("idOrder");
        setTitle("Order details");
        getMyOrDerdetail();

    }
    void getMyOrDerdetail(){
        class Myorder extends AsyncTask<Void, Void, String> {

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

                        JSONArray jsonArray = obj.getJSONArray("orderdetail");


                        System.out.println("elo");
                        String totalcost="",date="",payment="";
                        for(int i=0;i<jsonArray.length();i++) {
                            JSONObject myorder = jsonArray.getJSONObject(i);
                            System.out.println(myorder.getString("price"));
                            idz.add(myorder.getInt("id"));
                            cid.add(myorder.getString("cid"));
                            name.add(myorder.getString("name"));
                            path.add(myorder.getString("path"));
                            price.add(myorder.getInt("price"));
                            quantity.add(myorder.getInt("quantity"));
                            totalcost=myorder.getString("totalcost");
                            date=myorder.getString("date_of_order");
                            payment=myorder.getString("payment");
                        }

                        CartProductListCustomAdapter customadapter;
                        final ListView listView=(ListView) findViewById(R.id.listvieworderdetail);
                        customadapter = new CartProductListCustomAdapter(MyOrderDetail.this,cid,idz,name,path,price,quantity,1 );
                        listView.setAdapter(customadapter);

                        TextView txdate=(TextView) findViewById(R.id.orderdate);
                        txdate.setText("Date of order: "+date.substring(0, 16));
                        TextView txname=(TextView) findViewById(R.id.ordername);
                        txname.setText("Order "+id_order);
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
                RequestHandler requestHandler = new RequestHandler();
                int id= SharedPrefManager.getInstance(getApplicationContext()).getUser().getId();
                String ids=Integer.toString(id);

                HashMap<String, String> params = new HashMap<>();
                params.put("order_id", id_order);

                return requestHandler.sendPostRequest(URLs.URL_GETMYORDERDETAIL, params);
            }
        }

        Myorder ul = new Myorder();
        ul.execute();
    }
}
