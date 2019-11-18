package com.example.carparts.ui.contact;


import android.content.Intent;
import android.net.Uri;
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

import com.example.carparts.Geolocation;
import com.example.carparts.ProductList;
import com.example.carparts.R;

public class ContactFragment extends Fragment {
    ListView listView;
    private ContactViewModel contactViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        contactViewModel = ViewModelProviders.of(this).get(ContactViewModel.class);
        View view = inflater.inflate(R.layout.fragment_contact, container, false);

        final String[] contacts = {"Call to us", "Write to us", "Find us"};
        Integer[] imgid = {R.drawable.ic_contact_call, R.drawable.ic_contact_email, R.drawable.ic_contact_navigation};
        listView = (ListView) view.findViewById(R.id.lv_contact);

        ContactCustomListView contactCustomListView = new ContactCustomListView(getActivity(), contacts, imgid);
        //ArrayAdapter<String> ListViewAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_expandable_list_item_1, contacts);

        listView.setAdapter(contactCustomListView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:0123456789"));
                    startActivity(intent);
                }
                if (position == 1) {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto", "car@parts.cp", null));
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Mail from app!");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "I wnt buy... ");
                    startActivity(Intent.createChooser(emailIntent, "Send email..."));

                    try {
                        startActivity(Intent.createChooser(emailIntent, "How to send mail?"));
                    } catch (android.content.ActivityNotFoundException ex) {
                    }
                }
                if (position == 2) {
                    Intent intent = new Intent(getActivity().getApplicationContext(), Geolocation.class);
                    startActivity(intent);
                }
            }
        });

        return view;
    }


}
