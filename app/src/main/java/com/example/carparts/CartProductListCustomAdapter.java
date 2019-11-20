package com.example.carparts;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CartProductListCustomAdapter extends ArrayAdapter<String> {
    private Activity context;
    private String[] name;
    private String[] path;
    private int[] price;
    private int[] quantity;
    private int[] id;
    public CartProductListCustomAdapter(Activity context,int[] id, String[] name, String[] path, int[] price,int[] quantity) {
        super(context, R.layout.cart_listview_layout, name);
        this.context = context;
        // this.urls = urls;
        this.name = name;
        this.path = path;
        this.price=price;
        this.quantity=quantity;
        this.id=id;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.listview_layout, null, true);
        TextView nameTx = (TextView) listViewItem.findViewById(R.id.txname);
        TextView  priceTx = (TextView) listViewItem.findViewById(R.id.txprice);
        // TextView textView = (TextView) listViewItem.findViewById(R.id.tvurl);
        //  textView.setText(urls[position] );
        nameTx.setText(name[position] );
        final String ids=Integer.toString(id[position]);
        Button usun= (Button) listViewItem.findViewById(R.id.btDelete) ;
       /* listViewItem.findViewById(R.id.btDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductDetail.class);
                intent.putExtra("id_product", ids);
                context.startActivity(intent);
            }
        });*/
        priceTx.setText(quantity[position]+"x"+price[position]+"zł="+quantity[position]*price[position]+"zł");
        ImageView image = (ImageView) listViewItem.findViewById(R.id.imimage);
        new DownLoadImageTask(image).execute(URLs.URL_PPHOTO+path[position]);
        return  listViewItem;
    }
}
