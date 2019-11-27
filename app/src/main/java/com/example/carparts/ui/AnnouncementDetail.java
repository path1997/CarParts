package com.example.carparts.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.carparts.DownLoadImageTask;
import com.example.carparts.R;
import com.example.carparts.RequestHandler;
import com.example.carparts.URLs;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class AnnouncementDetail extends AppCompatActivity {

    String id_announcement;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement_detail);
        Bundle extras = getIntent().getExtras();
        id_announcement=extras.getString("id_announcement");
        getAnnouncementDeatil();
    }


    private void getAnnouncementDeatil(){
        class Announcementdetail extends AsyncTask<Void, Void, String> {

            private ArrayList<String> arrayList;

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                try {
                    System.out.println("response in announcement detail: " + s);
                    JSONObject obj = new JSONObject(s);


                    if (!obj.getBoolean("error")) {

                        JSONArray jsonArray = obj.getJSONArray("announcementdetail");
                        String[] title = new String[jsonArray.length()];
                        String[] price = new String[jsonArray.length()];
                        String[] description = new String[jsonArray.length()];
                        String[] path = new String[jsonArray.length()];
                        String[] phone = new String[jsonArray.length()];
                        String[] fname = new String[jsonArray.length()];
                        String[] city = new String[jsonArray.length()];
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject productdetail = jsonArray.getJSONObject(i);
                            title[i] = productdetail.getString("title");
                            price[i] = productdetail.getString("price");
                            description[i] = productdetail.getString("description");
                            path[i] = productdetail.getString("path");
                            phone[i] = productdetail.getString("phone");
                            fname[i] = productdetail.getString("fname");
                            city[i] = productdetail.getString("city");
                        }


                        ImageView image = (ImageView) findViewById(R.id.im_ann_detail);
                        new DownLoadImageTask(image).execute(URLs.URL_PPHOTO+path[0]);
//
//                        ImageView image = (ImageView) listViewItem.findViewById(R.id.imimage);
//                        new DownLoadImageTask(image).execute(URLs.URL_PPHOTO+path[position]);

                        TextView tvTitle= (TextView) findViewById(R.id.tv_ann_title);
                        TextView tvPrice= (TextView) findViewById(R.id.tv_ann_price);
                        TextView tvDescription= (TextView) findViewById(R.id.tv_ann_description);
                        TextView tvphone= (TextView) findViewById(R.id.tv_ann_phone);
                        TextView tvfname_city= (TextView) findViewById(R.id.tv_ann_fname_number);
                        tvTitle.setText(title[0]);
                        tvPrice.setText("Cena: "+price[0]+"zł");
                        tvDescription.setText(description[0]);
                        tvphone.setText(phone[0]);
                        tvfname_city.setText(fname[0] + ", " + city[0]);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {

                RequestHandler requestHandler = new RequestHandler();

                HashMap<String, String> params = new HashMap<>();
                params.put("announcementid", id_announcement);


                return requestHandler.sendPostRequest(URLs.URL_ANNOUNCEMENTDETAIL, params);
            }
        }
Announcementdetail ad = new Announcementdetail();
        ad.execute();
    }
}
