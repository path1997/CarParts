package com.example.carparts.ui.category;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.carparts.R;
import com.example.carparts.RequestHandler;
import com.example.carparts.SharedPrefManager;
import com.example.carparts.URLs;
import com.example.carparts.User;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;

public class CategoryFragment extends Fragment {
    View root=null;
    private CategoryViewModel categoryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
         root = inflater.inflate(R.layout.fragment_category, container, false);

       /* super.onCreate(savedInstanceState);*/
        categoryList();
        return root;
    }
    private void categoryList() {
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

                        //getting the user from the response
                        JSONArray jsonArray = obj.getJSONArray("category");
                        final TextView[] tv = new TextView[jsonArray.length()];
                        final RelativeLayout rl = (RelativeLayout) getActivity().findViewById(R.id.rl);
                        final TextView podpis= new TextView(getActivity().getApplicationContext());
/*                        podpis.setPadding(20, 15, 10, 15);
                        podpis.setTextSize((float) 20);
                        podpis.setText("Kategorie");
                        rl.addView(podpis);*/
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject category = jsonArray.getJSONObject(i);

                            String name = category.getString("name");
                            /*int age = employee.getInt("age");
                            String mail = employee.getString("mail");

                            mTextViewResult.append(firstName + ", " + String.valueOf(age) + ", " + mail + "\n\n");*/
                            tv[i] = new TextView(getActivity().getApplicationContext());
                            RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams
                                    ((int) RelativeLayout.LayoutParams.MATCH_PARENT,(int) RelativeLayout.LayoutParams.WRAP_CONTENT);
                            params.topMargin  = (i)*100;
                            params.bottomMargin  = (i+1)*100;
                            tv[i].setBackgroundResource(R.drawable.border);
                            tv[i].setText(name);
                            tv[i].setTextSize((float) 20);
                            tv[i].setPadding(20, 15, 10, 15);
                            tv[i].setLayoutParams(params);

                            rl.addView(tv[i]);
                        }

                        //creating a new user object
                        /*User user = new User(
                                userJson.getInt("id"),
                                userJson.getString("username"),
                                userJson.getString("email")
                        );

                        //storing the user in shared preferences
                        SharedPrefManager.getInstance(getActivity().getApplicationContext()).userLogin(user);*/
                        System.out.println("udalo sie");
                        //starting the profile activity
                        /*NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
                        navigationView.getMenu().performIdentifierAction(R.id.nav_home, 0);*/

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
                System.out.println("zapytalo");
                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_CATEGORY);
            }

        }
        CategoryList ca = new CategoryList();
        ca.execute();
    }
}