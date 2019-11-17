package com.example.carparts.ui.contact;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.carparts.R;

public class ContactFragment extends Fragment {

    private ContactViewModel contactViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        contactViewModel = ViewModelProviders.of(this).get(ContactViewModel.class);
        View view = inflater.inflate(R.layout.fragment_contact, container, false);

        String [] contacts = {"Call to us", "Write to us", "Find us"};
        Integer[] imgid = {R.drawable.ic_contact_call,R.drawable.ic_contact_email,R.drawable.ic_contact_navigation};

        ListView listView = (ListView) view.findViewById(R.id.lv_contact);
        ContactCustomListView contactCustomListView = new ContactCustomListView(getActivity(),contacts,imgid);
        //ArrayAdapter<String> ListViewAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_expandable_list_item_1, contacts);

        listView.setAdapter(contactCustomListView);

        return view;
    }
}
