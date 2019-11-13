package com.example.carparts.ui.home;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.carparts.R;
import com.example.carparts.SharedPrefManager;
import com.example.carparts.ui.login.LoginFragment;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root= inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);
        if (SharedPrefManager.getInstance(getContext()).isLoggedIn()) {
            System.out.println("jest zalogowany");

        }
        return root;
    }
}