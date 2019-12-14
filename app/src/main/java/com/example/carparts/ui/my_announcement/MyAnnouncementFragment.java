package com.example.carparts.ui.my_announcement;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.carparts.R;
import com.example.carparts.RequestHandler;
import com.example.carparts.SharedPrefManager;
import com.example.carparts.URLs;
import com.example.carparts.ui.AnnouncementDetail;
import com.example.carparts.ui.product.ProductListCustomAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class MyAnnouncementFragment extends Fragment {

    private MyAnnouncementViewModel myAnnouncementViewModel;
    View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myAnnouncementViewModel =
                ViewModelProviders.of(this).get(MyAnnouncementViewModel.class);
        root = inflater.inflate(R.layout.fragment_my_announcement, container, false);
        getMyAnnouncement();


        return root;
    }


    public void getMyAnnouncement() {
        class MyAnnouncement extends AsyncTask<Void, Void, String> {

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

                try {


                    System.out.println("response: " + s);
                    JSONObject obj = new JSONObject(s);
                    if (!obj.getBoolean("error")) {
                        Toast.makeText(getActivity().getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        JSONArray jsonArray = obj.getJSONArray("myannouncement");

                        ida = new String[jsonArray.length()];
                        String[] title = new String[jsonArray.length()];
                        String[] path = new String[jsonArray.length()];
                        String[] price = new String[jsonArray.length()];
                        String[] endannouncement = new String[jsonArray.length()];
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject producthome = jsonArray.getJSONObject(i);
                            ida[i] = producthome.getString("id");
                            title[i] = producthome.getString("title");
                            price[i] = producthome.getString("price");
                            path[i] = producthome.getString("path");
                            endannouncement[i] = producthome.getString("endannouncement");
                        }

                        MyAnnouncementCustomAdapter customadapter2;
                        final ListView listView = (ListView) getActivity().findViewById(R.id.lv_my_ann);
                        customadapter2 = new MyAnnouncementCustomAdapter(getActivity(), ida, title, path, price, endannouncement);
                        listView.setAdapter(customadapter2);

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(getActivity().getApplicationContext(), AnnouncementDetail.class);
                                String idP = ida[position];
                                intent.putExtra("id_announcement", idP);
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
                RequestHandler requestHandler = new RequestHandler();

                int id = SharedPrefManager.getInstance(getActivity().getApplicationContext()).getUser().getId();
                String ids = Integer.toString(id);

                HashMap<String, String> params = new HashMap<>();
                params.put("user_id", ids);

                return requestHandler.sendPostRequest(URLs.URL_MYANNOUNCEMENT, params);
            }
        }
        MyAnnouncement ma = new MyAnnouncement();
        ma.execute();
    }
}