package com.example.carparts.ui.my_orders;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.carparts.MyOrderDetail;
import com.example.carparts.OrderListProductListCustomAdapter;
import com.example.carparts.ProductDetail;
import com.example.carparts.ProductList;
import com.example.carparts.ProductListCustomAdapter;
import com.example.carparts.R;
import com.example.carparts.RequestHandler;
import com.example.carparts.SharedPrefManager;
import com.example.carparts.URLs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MyOrdersFragment extends Fragment {
    View root=null;
    String ida[];
    private MyOrdersViewModel myOrdersViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_my_orders, container, false);
        getmyorder();

        return root;
    }
    private void getmyorder() {
        System.out.println("jestem");

        class CategoryList extends AsyncTask<Void, Void, String> {

            private ProgressBar progressBar;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //displaying the progress bar while user registers on the server
                progressBar = (ProgressBar) root.findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //hiding the progressbar after completion
                progressBar.setVisibility(View.GONE);

                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);

                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        Toast.makeText(getActivity().getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        JSONArray jsonArray = obj.getJSONArray("orders");

                        ida=new String[jsonArray.length()];;
                        String[] name = new String[jsonArray.length()];
                        String[] date = new String[jsonArray.length()];
                        String[] totalcost = new String[jsonArray.length()];
                        for(int i=0;i<jsonArray.length();i++) {
                            JSONObject producthome = jsonArray.getJSONObject(i);
                            ida[i]= producthome.getString("id");
                            name[i]= "Order "+ida;
                            totalcost[i]= producthome.getString("totalcost");
                            date[i]= producthome.getString("date_of_order");
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
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();
                int id= SharedPrefManager.getInstance(getActivity().getApplicationContext()).getUser().getId();
                String ids=Integer.toString(id);
                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("user_id", ids);

                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_GETMYORDER, params);
            }

        }
        CategoryList ca = new CategoryList();
        ca.execute();
    }
}