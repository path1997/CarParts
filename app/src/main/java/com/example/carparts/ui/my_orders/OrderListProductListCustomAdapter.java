package com.example.carparts.ui.my_orders;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carparts.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class OrderListProductListCustomAdapter extends ArrayAdapter<String> {
    private Activity context;
    private String[] name;
    private String[] totalcost;
    private String[] date;
    private String[] id;
    public OrderListProductListCustomAdapter(Activity context, String[] id, String[] name, String[] totalcost, String[] date) {
        super(context, R.layout.listview_myorder, name);
        this.context = context;
        this.name = name;
        this.id=id;
        this.totalcost=totalcost;
        this.date=date;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.listview_myorder, null, true);
        TextView nameTx = (TextView) listViewItem.findViewById(R.id.ordername);
        TextView  totalcostTx = (TextView) listViewItem.findViewById(R.id.ordercost);
         TextView dateTx = (TextView) listViewItem.findViewById(R.id.orderdate);

        nameTx.setText("Order "+id[position] );
        totalcostTx.setText("Total cost order: "+totalcost[position]+"zł");
        dateTx.setText("Date of order: "+date[position].substring(0, 16));
        return  listViewItem;
    }
}
