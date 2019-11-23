package com.example.carparts.ui.all_announcement;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.carparts.ui.product.ProductListCustomAdapter;
import com.example.carparts.R;
import com.example.carparts.RequestHandler;
import com.example.carparts.URLs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AllAnnouncementFragment extends Fragment {

    private AllAnnouncementViewModel allAnnouncementViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        allAnnouncementViewModel = ViewModelProviders.of(this).get(AllAnnouncementViewModel.class);
        View root = inflater.inflate(R.layout.fragment_all_announcement, container, false);
//        final TextView textView = root.findViewById(R.id.text_all_announcement);
//        allAnnouncementViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });


        getAllAnnouncement();
        return root;
    }

    public void getAllAnnouncement(){

        class AllAnnouncement extends AsyncTask<Void, Void, String> {
            //private String ida[];
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


                    System.out.println("response: " + s);
                    JSONObject obj = new JSONObject(s);
                    if(!obj.getBoolean("error")){
                        Toast.makeText(getActivity().getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        JSONArray jsonArray = obj.getJSONArray("allannouncement");

                        //ida=new String[jsonArray.length()];
                        String[] title = new String[jsonArray.length()];
                        String[] path = new String[jsonArray.length()];
                        String[] price = new String[jsonArray.length()];
                        for(int i=0;i<jsonArray.length();i++) {
                            JSONObject producthome = jsonArray.getJSONObject(i);
                            //ida[i]= producthome.getString("id");
                            title[i]= producthome.getString("title");
                            price[i]= producthome.getString("price");
                            path[i]= producthome.getString("path");
                        }

                        ProductListCustomAdapter customadapter1;
                        final ListView listView=(ListView) getActivity().findViewById(R.id.lv_all_ann);
                        customadapter1 = new ProductListCustomAdapter(getActivity(),title,path,price);
                        listView.setAdapter(customadapter1);

//                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                Intent intent = new Intent(getActivity().getApplicationContext(), ProductDetail.class);
//                                String idP=ida[position];
//                                intent.putExtra("id_product", idP);
//                                startActivity(intent);
//                            }
//                        });

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
                return requestHandler.sendPostRequest(URLs.URL_ALLANNOUNCEMENT);
            }
        }
        AllAnnouncement aa = new AllAnnouncement();
        aa.execute();
    }

}