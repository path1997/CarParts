package com.example.carparts.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.carparts.MainActivity;
import com.example.carparts.R;
import com.example.carparts.RequestHandler;
import com.example.carparts.SharedPrefManager;
import com.example.carparts.URLs;
import com.example.carparts.User;
import com.example.carparts.ui.home.HomeFragment;
import com.example.carparts.ui.register.RegisterFragment;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginFragment extends Fragment {
    View root;
    private LoginViewModel loginViewModel;
    EditText editTextUsername, editTextPassword;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        if(SharedPrefManager.isLoggedIn()){
            root = inflater.inflate(R.layout.fragment_login, container, false);
            SharedPrefManager.getInstance(getActivity().getApplicationContext()).logout();
            NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
            navigationView.getMenu().performIdentifierAction(R.id.nav_home, 0);
            Menu menu = navigationView.getMenu();
            MenuItem nav_login = menu.findItem(R.id.nav_login);
            nav_login.setTitle("Login");
            View header = navigationView.getHeaderView(0);
            TextView textView= (TextView) header.findViewById(R.id.textView);
            textView.setText("You are not logged in");
            /*root.findViewById(R.id.buttonLogin).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    userLogout();
                }
            });*/
        } else {

            root = inflater.inflate(R.layout.fragment_login, container, false);
            /*super.onCreate(savedInstanceState);*/

            editTextUsername = (EditText) root.findViewById(R.id.editTextPassword);
            editTextPassword = (EditText) root.findViewById(R.id.editTextPassword);


            //if user presses on login
            //calling the method login
            root.findViewById(R.id.buttonLogin).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    userLogin();
                }
            });

            //if user presses on not registered
            root.findViewById(R.id.textViewRegister).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //open register screen
                    FragmentTransaction fr = getFragmentManager().beginTransaction();
                    fr.replace(R.id.nav_host_fragment, new RegisterFragment());
                    fr.commit();
                }
            });
        }
        return root;

    }
    private void userLogin() {
        //first getting the values
        final String username = editTextUsername.getText().toString();
        final String password = editTextPassword.getText().toString();

        //validating inputs
        if (TextUtils.isEmpty(username)) {
            editTextUsername.setError("Please enter your username");
            editTextUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Please enter your password");
            editTextPassword.requestFocus();
            return;
        }

        //if everything is fine

        class UserLogin extends AsyncTask<Void, Void, String> {
            ProgressBar progressBar;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar = (ProgressBar) getActivity().findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //progressBar.setVisibility(View.GONE);


                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);

                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        Toast.makeText(getActivity().getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                        //getting the user from the response
                        JSONObject userJson = obj.getJSONObject("user");

                        //creating a new user object
                        User user = new User(
                                userJson.getInt("id"),
                                userJson.getString("username"),
                                userJson.getString("email")
                        );

                        //storing the user in shared preferences
                        SharedPrefManager.getInstance(getActivity().getApplicationContext()).userLogin(user);

                        //starting the profile activity
                        //getActivity().finish();
                        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
                        navigationView.getMenu().performIdentifierAction(R.id.nav_home, 0);
                        Menu menu = navigationView.getMenu();
                        MenuItem nav_login = menu.findItem(R.id.nav_login);
                        nav_login.setTitle("Logout");
                        View header = navigationView.getHeaderView(0);
                        TextView textView= (TextView) header.findViewById(R.id.textView);
                        textView.setText("Hi "+username);
                        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(root.getWindowToken(), 0);

                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
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
                params.put("username", username);
                params.put("password", password);

                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_LOGIN, params);
            }
        }

        UserLogin ul = new UserLogin();
        ul.execute();
    }
}