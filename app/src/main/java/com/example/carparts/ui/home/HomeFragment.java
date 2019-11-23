package com.example.carparts.ui.home;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.carparts.ui.product.ProductDetail;
import com.example.carparts.ui.product.ProductListCustomAdapter;
import com.example.carparts.R;
import com.example.carparts.RequestHandler;
import com.example.carparts.URLs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    View view=null;
    ActionBar actionBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.fragment_home, container, false);

        actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        getHome();
        return view;
    }

    public void getHome(){

        class HomeList extends AsyncTask<Void, Void, String> {
            private String ida[];
            private ImageView imageView;
            private ArrayList<String> arrayList;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                /*actionBar.hide();
                imageView=(ImageView) view.findViewById(R.id.imagelogo);
                imageView.setVisibility(View.VISIBLE);*/
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                /*SystemClock.sleep(600);
                actionBar.show();
                imageView=(ImageView) view.findViewById(R.id.imagelogo);
                imageView.setVisibility(View.GONE);*/

                try{


                    JSONObject obj = new JSONObject(s);
                    if(!obj.getBoolean("error")){
                        Toast.makeText(getActivity().getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        JSONArray jsonArray = obj.getJSONArray("productlistforhome");

                        ida=new String[jsonArray.length()];;
                        String[] name = new String[jsonArray.length()];
                        String[] path = new String[jsonArray.length()];
                        String[] price = new String[jsonArray.length()];
                        for(int i=0;i<jsonArray.length();i++) {
                            JSONObject producthome = jsonArray.getJSONObject(i);
                            ida[i]= producthome.getString("id");
                            name[i]= producthome.getString("name");
                            path[i]= producthome.getString("path");
                            price[i]= producthome.getString("price");
                        }

                        ProductListCustomAdapter customadapter1;
                        final ListView listView=(ListView) getActivity().findViewById(R.id.lv_home);
                        customadapter1 = new ProductListCustomAdapter(getActivity(),name,path,price );
                        listView.setAdapter(customadapter1);

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(getActivity().getApplicationContext(), ProductDetail.class);
                                String idP=ida[position];
                                intent.putExtra("id_product", idP);
                                startActivity(intent);
                            }
                        });

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();
                System.out.println("zapytalo");
                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_PRODUCTLISTFORHOME);
            }
        }
        HomeList hl = new HomeList();
        hl.execute();
        }

}