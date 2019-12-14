package com.example.carparts.ui.my_announcement;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;

import com.example.carparts.DownLoadImageTask;
import com.example.carparts.MainActivity;
import com.example.carparts.R;
import com.example.carparts.RequestHandler;
import com.example.carparts.URLs;
import com.example.carparts.ui.register.RegisterFragment;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class MyAnnouncementCustomAdapter extends ArrayAdapter {

    private String ida[];


    private String[] title;
    private String[] path;
    private String[] price;
    private String[] endannouncement;
    private Button edit;
    private Button delete;
    private Activity context;

    public MyAnnouncementCustomAdapter(Activity context, String ida[], String[] title, String[] path, String[] price, String[] endannouncement) {
        super(context, R.layout.listview_my_announcement, title);
        this.ida = ida;
        this.context = context;
        this.title = title;
        this.path = path;
        this.price = price;
        this.endannouncement = endannouncement;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.listview_my_announcement, null, true);
        TextView nameTx = (TextView) listViewItem.findViewById(R.id.my_order_title);
        TextView priceTx = (TextView) listViewItem.findViewById(R.id.my_order_price);
        TextView endAnnouncementTx = (TextView) listViewItem.findViewById(R.id.my_order_end_announcement);
//        Button edit = (Button) listViewItem.findViewById(R.id.my_order_edit_btn);
//        Button delete = (Button) listViewItem.findViewById(R.id.my_order_delete_btn);
        nameTx.setText(title[position]);
        priceTx.setText("Cena: " + price[position] + "zł");
        System.out.println("End ma nie byc nullem: " + endAnnouncementTx);
        endAnnouncementTx.setText("The ends at: " + endannouncement[position]);

        Button edit = (Button) listViewItem.findViewById(R.id.my_order_edit_btn);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context.getApplicationContext(), EditMyAnnouncement.class);
                String id_my_announcement=ida[position];
                intent.putExtra("id_my_announcement", id_my_announcement);
                context.startActivity(intent);


            }
        });


        Button delete = (Button) listViewItem.findViewById(R.id.my_order_delete_btn);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder adbuilder = new AlertDialog.Builder(getContext());
                adbuilder.setMessage("Do you really want to delete announcement?")
                        .setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("ida ogloszenia do usuniecia?: " + ida[position]);
                        deleteAnnouncement(ida[position], position);
                    }
                })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setTitle("WAIT");
                adbuilder.show();
            }
        });

        ImageView image = (ImageView) listViewItem.findViewById(R.id.imimage);
        new DownLoadImageTask(image).execute(URLs.URL_PPHOTO + path[position]);
        return listViewItem;
    }

    private void deleteAnnouncement(final String ids, final int position) {
        class DeleteAnnouncement extends AsyncTask<Void, Void, String> {
            String idp = ids;
            ProgressBar progressBar;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                progressBar = (ProgressBar) context.findViewById(R.id.progressBar);
//                progressBar.setVisibility(View.VISIBLE);
            }

            private ArrayList<String> arrayList;

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
//                progressBar.setVisibility(View.GONE);
//                NavigationView navigationView = getContext().findViewById(R.id.nav_view);
                NavigationView navigationView = context.findViewById(R.id.nav_view);
                navigationView.getMenu().performIdentifierAction(R.id.nav_my_announcement, 0);

//                try {
////                    JSONObject obj = new JSONObject(s);
//                    if (!obj.getBoolean("error")) {
//                        Toast.makeText(getContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
//                        JSONArray jsonArray = obj.getJSONArray("deleteannouncement");
//
//                    } else {
//                        Toast.makeText(context.getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }

            @Override
            protected String doInBackground(Void... voids) {

                RequestHandler requestHandler = new RequestHandler();

                HashMap<String, String> params = new HashMap<>();
                params.put("id", idp);


                return requestHandler.sendPostRequest(URLs.URL_DELETEANNOUNCEMENT, params);
            }
        }

        DeleteAnnouncement da = new DeleteAnnouncement();
        da.execute();
    }

}
