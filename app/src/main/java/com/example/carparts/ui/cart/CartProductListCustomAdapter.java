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
        // this.urls = urls;
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
        // TextView textView = (TextView) listViewItem.findViewById(R.id.tvurl);
        //  textView.setText(urls[position] );
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

       /* listViewItem.findViewById(R.id.btDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductDetail.class);
                intent.putExtra("id_product", ids);
                context.startActivity(intent);
            }
        });*/
       listViewItem.findViewById(R.id.btMinus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open register screen
                minusItem(cid[position]);
            }
        });
        listViewItem.findViewById(R.id.btPlus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open register screen
                plusItem(cid[position]);
            }
        });
        listViewItem.findViewById(R.id.btRemove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open register screen
                removeItem(cid[position]);

            }
        });
        priceTx.setText(quantity[position]+"x"+price[position]+"zł="+quantity[position]*price[position]+"zł");
        ImageView image = (ImageView) listViewItem.findViewById(R.id.imimage);
        new DownLoadImageTask(image).execute(URLs.URL_PPHOTO+path[position]);
        return  listViewItem;
    }
    private void minusItem(final String ids) {
        class UserLogin extends AsyncTask<Void, Void, String> {
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
                    //converting response to json object
                    //System.out.println("przed");
                    JSONObject obj = new JSONObject(s);
                    //System.out.println("za");
                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        Toast.makeText(context.getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        context.recreate();
                    } else {
                        Toast.makeText(context.getApplicationContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
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
                params.put("id", idp);

                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_MINUSITEM, params);
            }
        }

        UserLogin ul = new UserLogin();
        ul.execute();
    }
    private void plusItem(final String ids) {
        class UserLogin extends AsyncTask<Void, Void, String> {
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
                    //converting response to json object
                    //System.out.println("przed");
                    JSONObject obj = new JSONObject(s);
                    //System.out.println("za");
                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        Toast.makeText(context.getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        context.recreate();
                    } else {
                        Toast.makeText(context.getApplicationContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
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
                params.put("id", idp);

                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_PLUSITEM, params);
            }
        }

        UserLogin ul = new UserLogin();
        ul.execute();
    }
    private void removeItem(final String ids) {
        class UserLogin extends AsyncTask<Void, Void, String> {
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
                    //converting response to json object
                    //System.out.println("przed");
                    JSONObject obj = new JSONObject(s);
                    //System.out.println("za");
                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        Toast.makeText(context.getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        context.recreate();
                    } else {
                        Toast.makeText(context.getApplicationContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
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
                params.put("id", idp);

                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_REMOVEITEM, params);
            }
        }

        UserLogin ul = new UserLogin();
        ul.execute();
    }
}
