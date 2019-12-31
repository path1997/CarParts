package com.example.carparts.ui.delivery;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carparts.MainActivity;
import com.example.carparts.R;
import com.example.carparts.RequestHandler;
import com.example.carparts.SharedPrefManager;
import com.example.carparts.URLs;
import com.example.carparts.config.Config;
import com.google.android.material.navigation.NavigationView;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

public class Delivery extends AppCompatActivity {
    int suma;
    TextView totalcost;
    public static final int PAYPAL_REQUEST_CODE=7171;
    private static PayPalConfiguration config= new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX).clientId(Config.PAYPAL_CLIENT_ID);

    @Override
    protected void onDestroy(){
        stopService(new Intent(this,PayPalService.class));
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Method of delivery and pay");
        setContentView(R.layout.activity_delivery);
        Bundle extras = getIntent().getExtras();
        String suma1=extras.getString("suma");
        suma = Integer.parseInt(suma1);
        System.out.println(suma);
        suma+=9;
        totalcost=(TextView) findViewById(R.id.Totalcost);
        totalcost.setText("Total cost: "+suma+"zł");

        Intent intent= new Intent(this,PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        startService(intent);

        final RadioGroup radioGroup1 = (RadioGroup) findViewById(R.id.radio1);
        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                View radioButton = radioGroup1.findViewById(checkedId);
                int index = radioGroup1.indexOfChild(radioButton);


                switch (index) {
                    case 0:
                        suma-=6;
                        totalcost.setText("Total cost: "+suma+"zł");
                        break;
                    case 1:
                        suma+=6;
                        totalcost.setText("Total cost: "+suma+"zł");
                        break;
                }
            }
        });
        final RadioGroup radioGroup2 = (RadioGroup) findViewById(R.id.radio2);
        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                View radioButton = radioGroup2.findViewById(checkedId);
                int index = radioGroup2.indexOfChild(radioButton);


                switch (index) {
                    case 0:
                        suma-=10;
                        totalcost.setText("Total cost: "+suma+"zł");
                        break;
                    case 1:
                        suma+=10;
                        totalcost.setText("Total cost: "+suma+"zł");
                        break;
                }
            }
        });
        findViewById(R.id.btcheckout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final RadioButton payment = (RadioButton) findViewById(radioGroup2.getCheckedRadioButtonId());
                int position = radioGroup2.indexOfChild(payment);
                if(position==0){
                    addReservation(1);
                } else {
                    addReservation(0);
                }
            }
        });
    }
    void paypal(){
        PayPalPayment payPalPayment=new PayPalPayment(new BigDecimal(suma),"PLN","Zamowienie 1",PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent=new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
        startActivityForResult(intent,PAYPAL_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==PAYPAL_REQUEST_CODE){
            if(resultCode==RESULT_OK){
                PaymentConfirmation confirmation=data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if(confirmation!=null){
                    addOrder(1);
                }
            } else if(resultCode== Activity.RESULT_CANCELED) {
                Toast.makeText(this,"Cancel",Toast.LENGTH_SHORT).show();
                removeReservation();
            }
        } else if(resultCode== PaymentActivity.RESULT_EXTRAS_INVALID) {
            Toast.makeText(this, "Invalid", Toast.LENGTH_SHORT).show();

        }
    }
    void addOrder(final int payment){
        class Deliveryc extends AsyncTask<Void, Void, String> {
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
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();


                    } else {
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {

                RequestHandler requestHandler = new RequestHandler();
                int id = SharedPrefManager.getInstance(getApplicationContext()).getUser().getId();
                String ids = Integer.toString(id);
                String suma1 = Integer.toString(suma);
                String payment1 = Integer.toString(payment);

                HashMap<String, String> params = new HashMap<>();
                params.put("user_id", ids);
                params.put("payment",payment1);
                params.put("sum", suma1);

                return requestHandler.sendPostRequest(URLs.URL_CONFIRMORDER, params);
            }
        }
        Deliveryc ul = new Deliveryc();
        ul.execute();
    }
    void addReservation(final int payment){
        class Deliveryc extends AsyncTask<Void, Void, String> {
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
                        if(payment==0){
                            addOrder(payment);
                        } else {
                            paypal();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {

                RequestHandler requestHandler = new RequestHandler();
                int id = SharedPrefManager.getInstance(getApplicationContext()).getUser().getId();
                String ids = Integer.toString(id);

                HashMap<String, String> params = new HashMap<>();
                params.put("user_id", ids);

                return requestHandler.sendPostRequest(URLs.URL_ADDRESERVATION, params);
            }
        }
        Deliveryc ul = new Deliveryc();
        ul.execute();
    }
    void removeReservation(){
        class Deliveryc extends AsyncTask<Void, Void, String> {
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

                    } else {
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {

                RequestHandler requestHandler = new RequestHandler();
                int id = SharedPrefManager.getInstance(getApplicationContext()).getUser().getId();
                String ids = Integer.toString(id);

                HashMap<String, String> params = new HashMap<>();
                params.put("user_id", ids);

                return requestHandler.sendPostRequest(URLs.URL_REMOVERESERVATION, params);
            }
        }
        Deliveryc ul = new Deliveryc();
        ul.execute();
    }
}
