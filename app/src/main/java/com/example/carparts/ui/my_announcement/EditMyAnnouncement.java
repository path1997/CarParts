package com.example.carparts.ui.my_announcement;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.carparts.MainActivity;
import com.example.carparts.R;
import com.example.carparts.RequestHandler;
import com.example.carparts.SharedPrefManager;
import com.example.carparts.URLs;
import com.example.carparts.User;
import com.example.carparts.ui.AnnouncementDetail;
import com.example.carparts.ui.all_announcement.AllAnnouncementFragment;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class EditMyAnnouncement extends AppCompatActivity {
    String id_my_announcement;
    private String ida[];
    View view;
    EditText editTitle, editPrice, editDescription;
    Button buttonChange;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_edit_my_announcement);

        editTitle= (EditText)findViewById(R.id.edit_my_order_title);
        editPrice = (EditText) findViewById(R.id.edit_my_order_price);
        editDescription = (EditText) findViewById(R.id.edit_my_order_description);
        buttonChange = (Button) findViewById(R.id.edit_my_order_btn_change);

        Bundle extras = getIntent().getExtras();
        id_my_announcement=extras.getString("id_my_announcement");

        loadDataAnnouncement();

        buttonChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editAnnouncement();
            }
        });

    }

    private void editAnnouncement(){

        final String idma = id_my_announcement;
        final String title = editTitle.getText().toString().trim();
        final String price = editPrice.getText().toString().trim();
        final String description = editDescription.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            editTitle.setError("Please enter new title");
            editTitle.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(price)) {
            editPrice.setError("Please enter new price");
            editPrice.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(description)) {
            editDescription.setError("Please enter new description");
            editDescription.requestFocus();
            return;
        }

        class EditAnnouncement extends AsyncTask<Void,Void,String>{

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//              progressBar = (ProgressBar) getActivity().findViewById(R.id.progressBar);
//               progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
//              progressBar.setVisibility(View.GONE);


                try {
                    JSONObject obj = new JSONObject(s);

                    if (!obj.getBoolean("error")) {

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);



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
                HashMap<String, String> params = new HashMap<>();
                params.put("myannouncementid", idma);
                params.put("title", title);
                params.put("price", price);
                params.put("description", description);
                return requestHandler.sendPostRequest(URLs.URL_EDITMYANNOUNCEMENT, params);
            }
        }
        EditAnnouncement ea = new EditAnnouncement();
        ea.execute();
    }

    private void loadDataAnnouncement() {
        final String idma = id_my_announcement;

        class LoadDataAnnouncement extends AsyncTask<Void,Void, String>{


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


                    System.out.println("response w editmyannouncement: " + s);
                    JSONObject obj = new JSONObject(s);
                    if (!obj.getBoolean("error")) {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        JSONArray jsonArray = obj.getJSONArray("getmyannouncement");

                        String[] title = new String[jsonArray.length()];
                        String[] price = new String[jsonArray.length()];
                        String[] description = new String[jsonArray.length()];
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject producthome = jsonArray.getJSONObject(i);
                            title[i] = producthome.getString("title");
                            price[i] = producthome.getString("price");
                            description[i] = producthome.getString("description");
                        }
                        System.out.println("Wypisuje title[0]: "+ title[0]);
                        System.out.println("Wypisuje price[0]: "+ price[0]);
                        System.out.println("Wypisuje description[0]: "+ description[0]);
                        editTitle.setText(title[0]);
                        editPrice.setText(price[0]);
                        editDescription.setText(description[0]);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler requestHandler = new RequestHandler();

                HashMap<String, String> params = new HashMap<>();
                params.put("myannouncementid", idma);


                return requestHandler.sendPostRequest(URLs.URL_GETMYANNOUNCEMENT, params);
            }
        }
        LoadDataAnnouncement lda = new LoadDataAnnouncement();
        lda.execute();


    }
}
