package com.example.carparts.ui.my_orders;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.carparts.R;
import com.example.carparts.RequestHandler;
import com.example.carparts.SharedPrefManager;
import com.example.carparts.URLs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MyOrdersFragment extends Fragment {
    View root=null;
    String ida[];

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_my_orders, container, false);
        getmyorder();

        return root;
    }
    private void getmyorder() {
        System.out.println("jestem");

        class MyOrders extends AsyncTask<Void, Void, String> {

            private ProgressBar progressBar;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar = (ProgressBar) root.findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressBar.setVisibility(View.GONE);

                try {
                    JSONObject obj = new JSONObject(s);

                    if (!obj.getBoolean("error")) {
                        //Toast.makeText(getActivity().getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        JSONArray jsonArray = obj.getJSONArray("orders");

                        ida=new String[jsonArray.length()];;
                        String[] name = new String[jsonArray.length()];
                        String[] date = new String[jsonArray.length()];
                        String[] totalcost = new String[jsonArray.length()];
                        for(int i=0;i<jsonArray.length();i++) {
                            JSONObject myorders = jsonArray.getJSONObject(i);
                            ida[i]= myorders.getString("id");
                            name[i]= "Order "+ida;
                            totalcost[i]= myorders.getString("totalcost");
                            date[i]= myorders.getString("date_of_order");
                        }

                        OrderListProductListCustomAdapter customadapter1;
                        final ListView listView=(ListView) getActivity().findViewById(R.id.orderslistview);
                        customadapter1 = new OrderListProductListCustomAdapter(getActivity(),ida,name,totalcost,date);
                        listView.setAdapter(customadapter1);

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(getActivity().getApplicationContext(), MyOrderDetail.class);
                                String idP=ida[position];
                                intent.putExtra("idOrder", idP);
                                startActivity(intent);
                            }
                        });

                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler requestHandler = new RequestHandler();

                int id= SharedPrefManager.getInstance(getActivity().getApplicationContext()).getUser().getId();
                String ids=Integer.toString(id);

                HashMap<String, String> params = new HashMap<>();
                params.put("user_id", ids);

                return requestHandler.sendPostRequest(URLs.URL_GETMYORDER, params);
            }

        }
        MyOrders ca = new MyOrders();
        ca.execute();
    }
}