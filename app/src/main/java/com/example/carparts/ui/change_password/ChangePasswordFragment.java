package com.example.carparts.ui.change_password;

import android.app.Activity;
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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.carparts.R;
import com.example.carparts.RequestHandler;
import com.example.carparts.SharedPrefManager;
import com.example.carparts.URLs;
import com.example.carparts.User;
import com.example.carparts.ui.login.LoginViewModel;
import com.example.carparts.ui.register.RegisterFragment;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ChangePasswordFragment extends Fragment {
    View root;
    private LoginViewModel loginViewModel;
    EditText Password1,Password2,Password3;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

            root = inflater.inflate(R.layout.fragment_change_password, container, false);
            /*super.onCreate(savedInstanceState);*/

            Password1 = (EditText) root.findViewById(R.id.password1);
            Password2 = (EditText) root.findViewById(R.id.password2);
            Password3 = (EditText) root.findViewById(R.id.password3);


            //if user presses on login
            //calling the method login
            root.findViewById(R.id.buttonChange).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changePassword();
                }
            });

        return root;

    }
    private void changePassword() {
        //first getting the values
        final String password1 = Password1.getText().toString();
        final String password2 = Password2.getText().toString();
        final String password3 = Password3.getText().toString();

        //validating inputs
        if (TextUtils.isEmpty(password1)) {
            Password1.setError("Please enter your old password");
            Password1.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password2)) {
            Password2.setError("Please enter your new password");
            Password2.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password3)) {
            Password3.setError("Please repeat your password");
            Password3.requestFocus();
            return;
        }
        if(!password2.equals(password3)){
            Password3.setError("The password is not the same");
            Password3.requestFocus();
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

                        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(root.getWindowToken(), 0);
                        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
                        navigationView.getMenu().performIdentifierAction(R.id.nav_login, 0);

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

                int id=SharedPrefManager.getInstance(getActivity().getApplicationContext()).getUser().getId();
                String ids=Integer.toString(id);

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("id",ids);
                params.put("oldpassword", password1);
                params.put("newpassword", password2);

                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_CHANGEPASSWORD, params);
            }
        }

        UserLogin ul = new UserLogin();
        ul.execute();
    }

}
