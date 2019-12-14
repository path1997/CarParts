package com.example.carparts.ui.product;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.carparts.DownLoadImageTask;
import com.example.carparts.R;
import com.example.carparts.URLs;

import java.io.InputStream;
import java.net.URL;

public class ProductListCustomAdapter extends ArrayAdapter<String> {
    private Activity context;
    private String[] name;
    private String[] path;
    private String[] price;
    public ProductListCustomAdapter(Activity context, String[] name, String[] path, String[] price) {
        super(context, R.layout.listview_layout, name);
        this.context = context;
        // this.urls = urls;
        this.name = name;
        this.path = path;
        this.price=price;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.listview_layout, null, true);
        TextView  nameTx = (TextView) listViewItem.findViewById(R.id.txname);
        TextView  priceTx = (TextView) listViewItem.findViewById(R.id.txprice);
        nameTx.setText(name[position] );
        priceTx.setText("Cena: "+price[position]+"zł" );
        ImageView image = (ImageView) listViewItem.findViewById(R.id.imimage);
        new DownLoadImageTask(image).execute(URLs.URL_PPHOTO+path[position]);
        return  listViewItem;
    }
}


