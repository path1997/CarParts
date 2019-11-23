package com.example.carparts.ui.register;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class RegisterFragment extends Fragment {

    EditText Fname,SName,PhoneNumber,Email,PostCode,City,Address,Password;
    View root;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_register, container, false);
        super.onCreate(savedInstanceState);
        Fname = (EditText) root.findViewById(R.id.FName);
        SName = (EditText) root.findViewById(R.id.SName);
        PhoneNumber = (EditText) root.findViewById(R.id.PhoneNumber);
        Email = (EditText) root.findViewById(R.id.Email);
        PostCode = (EditText) root.findViewById(R.id.PostCode);
        City = (EditText) root.findViewById(R.id.City);
        Address = (EditText) root.findViewById(R.id.Address);
        Password = (EditText) root.findViewById(R.id.Password);


        root.findViewById(R.id.buttonRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        return root;

    }

    private void registerUser() {
        final String fname = Fname.getText().toString().trim();
        final String sname = SName.getText().toString().trim();
        final String password = Password.getText().toString().trim();
        final String phone = PhoneNumber.getText().toString().trim();
        final String email = Email.getText().toString().trim();
        final String postcode = PostCode.getText().toString().trim();
        final String city = City.getText().toString().trim();
        final String address = Address.getText().toString().trim();


        if (TextUtils.isEmpty(fname)) {
            Fname.setError("Please enter first name");
            Fname.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(sname)) {
            SName.setError("Please enter your second name");
            SName.requestFocus();
            return;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Email.setError("Enter a valid email");
            Email.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            PhoneNumber.setError("Please enter your phone");
            PhoneNumber.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            Email.setError("Please enter your email");
            Email.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(postcode)) {
            PostCode.setError("Please enter your postcode");
            PostCode.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(city)) {
            City.setError("Please enter your city");
            City.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(address)) {
            Address.setError("Please enter your address");
            Address.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Password.setError("Enter a password");
            Password.requestFocus();
            return;
        }


        class RegisterUser extends AsyncTask<Void, Void, String> {

            private ProgressBar progressBar;

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler requestHandler = new RequestHandler();

                HashMap<String, String> params = new HashMap<>();
                params.put("fname", fname);
                params.put("sname", sname);
                params.put("phone", phone);
                params.put("email", email);
                params.put("postcode", postcode);
                params.put("city", city);
                params.put("address", address);
                params.put("password", password);

                return requestHandler.sendPostRequest(URLs.URL_REGISTER, params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar = (ProgressBar) getActivity().findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(root.getWindowToken(), 0);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressBar.setVisibility(View.GONE);

                try {
                    JSONObject obj = new JSONObject(s);

                    if (!obj.getBoolean("error")) {
                        Toast.makeText(getActivity().getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                        JSONObject userJson = obj.getJSONObject("user");

                        User user = new User(
                                userJson.getInt("id"),
                                userJson.getString("fname"),
                                userJson.getString("sname"),
                                userJson.getString("email"),
                                userJson.getString("phone"),
                                userJson.getString("postcode"),
                                userJson.getString("city"),
                                userJson.getString("address")
                        );

                        SharedPrefManager.getInstance(getActivity().getApplicationContext()).userLogin(user);
                        System.out.println(SharedPrefManager.getInstance(getActivity().getApplicationContext()).isLoggedIn());
                        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
                        navigationView.getMenu().performIdentifierAction(R.id.nav_home, 0);
                        Menu menu = navigationView.getMenu();
                        MenuItem nav_login = menu.findItem(R.id.nav_login);
                        nav_login.setTitle("My account");
                        View header = navigationView.getHeaderView(0);
                        TextView textView= (TextView) header.findViewById(R.id.textView);
                        textView.setText("Hi "+user.getFname()+" "+user.getSname());

                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute();
    }

}
