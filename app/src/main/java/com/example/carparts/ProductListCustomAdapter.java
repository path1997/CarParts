package com.example.carparts;

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
        // TextView textView = (TextView) listViewItem.findViewById(R.id.tvurl);
        //  textView.setText(urls[position] );
        nameTx.setText(name[position] );
        priceTx.setText("Cena: "+price[position]+"zł" );
        ImageView image = (ImageView) listViewItem.findViewById(R.id.imimage);
        new DownLoadImageTask(image).execute(URLs.URL_PPHOTO+path[position]);
        return  listViewItem;
    }
}
class DownLoadImageTask extends AsyncTask<String,Void,Bitmap> {
    ImageView imageView;

    public DownLoadImageTask(ImageView imageView){
        this.imageView = imageView;
    }

    /*
        doInBackground(Params... params)
            Override this method to perform a computation on a background thread.
     */
    protected Bitmap doInBackground(String...urls){
        String urlOfImage = urls[0];
        Bitmap logo = null;
        try{
            InputStream is = new URL(urlOfImage).openStream();
                /*
                    decodeStream(InputStream is)
                        Decode an input stream into a bitmap.
                 */
            logo = BitmapFactory.decodeStream(is);
        }catch(Exception e){ // Catch the download exception
            e.printStackTrace();
        }
        return logo;
    }

    /*
        onPostExecute(Result result)
            Runs on the UI thread after doInBackground(Params...).
     */
    protected void onPostExecute(Bitmap result){
        imageView.setImageBitmap(result);
    }
}

