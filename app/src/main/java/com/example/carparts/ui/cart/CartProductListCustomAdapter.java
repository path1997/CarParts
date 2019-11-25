package com.example.carparts.ui.cart;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carparts.DownLoadImageTask;
import com.example.carparts.R;
import com.example.carparts.RequestHandler;
import com.example.carparts.URLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class CartProductListCustomAdapter extends ArrayAdapter<String> {
    private Activity context;
    private String[] name;
    private String[] path;
    private int[] price;
    private int[] quantity;
    private int[] id;
    private String[] cid;
    int type;
    public CartProductListCustomAdapter(Activity context,String[] cid, int[] id, String[] name, String[] path, int[] price,int[] quantity, int type) {
        super(context, R.layout.cart_listview_layout, name);
        this.context = context;
        this.name = name;
        this.path = path;
        this.price=price;
        this.quantity=quantity;
        this.id=id;
        this.cid=cid;
        this.type=type;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.cart_listview_layout, null, true);
        TextView nameTx = (TextView) listViewItem.findViewById(R.id.txname);
        TextView  priceTx = (TextView) listViewItem.findViewById(R.id.txprice);
        nameTx.setText(name[position] );
        final String ids=Integer.toString(id[position]);
        if(type==1){
            TextView plus=(TextView) listViewItem.findViewById(R.id.btPlus);
            TextView minus=(TextView) listViewItem.findViewById(R.id.btMinus);
            TextView remove=(TextView) listViewItem.findViewById(R.id.btRemove);
            plus.setVisibility(View.GONE);
            minus.setVisibility(View.GONE);
            remove.setVisibility(View.GONE);

        }


       listViewItem.findViewById(R.id.btMinus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(quantity[position]==1){
                    removeItem(cid[position]);
                } else {
                    minusItem(cid[position]);
                }
            }
        });
        listViewItem.findViewById(R.id.btPlus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                plusItem(cid[position]);
            }
        });
        listViewItem.findViewById(R.id.btRemove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                removeItem(cid[position]);

            }
        });
        priceTx.setText(quantity[position]+"x"+price[position]+"zł="+quantity[position]*price[position]+"zł");
        ImageView image = (ImageView) listViewItem.findViewById(R.id.imimage);
        new DownLoadImageTask(image).execute(URLs.URL_PPHOTO+path[position]);
        return  listViewItem;
    }
    private void minusItem(final String ids) {
        class MinusItem extends AsyncTask<Void, Void, String> {
            String idp=ids;
            ProgressBar progressBar;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar = (ProgressBar) context.findViewById(R.id.progressBar);
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
                        context.recreate();
                    } else {
                        Toast.makeText(context.getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {

                RequestHandler requestHandler = new RequestHandler();

                HashMap<String, String> params = new HashMap<>();
                params.put("id", idp);


                return requestHandler.sendPostRequest(URLs.URL_MINUSITEM, params);
            }
        }

        MinusItem ul = new MinusItem();
        ul.execute();
    }
    private void plusItem(final String ids) {
        class PlusItem extends AsyncTask<Void, Void, String> {
            String idp=ids;
            ProgressBar progressBar;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar = (ProgressBar) context.findViewById(R.id.progressBar);
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
                        context.recreate();
                    } else {
                        Toast.makeText(context.getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {

                RequestHandler requestHandler = new RequestHandler();

                HashMap<String, String> params = new HashMap<>();
                params.put("id", idp);


                return requestHandler.sendPostRequest(URLs.URL_PLUSITEM, params);
            }
        }

        PlusItem ul = new PlusItem();
        ul.execute();
    }
    private void removeItem(final String ids) {
        class RemoveItem extends AsyncTask<Void, Void, String> {
            String idp=ids;
            ProgressBar progressBar;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar = (ProgressBar) context.findViewById(R.id.progressBar);
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
                        context.recreate();
                    } else {
                        Toast.makeText(context.getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {

                RequestHandler requestHandler = new RequestHandler();

                HashMap<String, String> params = new HashMap<>();
                params.put("id", idp);


                return requestHandler.sendPostRequest(URLs.URL_REMOVEITEM, params);
            }
        }

        RemoveItem ul = new RemoveItem();
        ul.execute();
    }
}
