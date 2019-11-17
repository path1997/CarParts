package com.example.carparts.ui.contact;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.carparts.R;

public class ContactCustomListView extends ArrayAdapter {

    private String[] contacts;
    private Integer[] imgid;
    private Activity context;

    public ContactCustomListView(Activity context, String[] contacts, Integer[] imgid) {
        super(context, R.layout.contact_custom_layout,contacts);

        this.context=context;
        this.contacts=contacts;
        this.imgid=imgid;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View r=convertView;
            ViewHolder viewHolder = null;
            if(r==null){
                LayoutInflater layoutInflater = context.getLayoutInflater();
                r=layoutInflater.inflate(R.layout.contact_custom_layout,null,true);
                viewHolder=new ViewHolder(r);
                r.setTag(viewHolder);
            }
            else{
                viewHolder = (ViewHolder) r.getTag();
            }
            viewHolder.imageView.setImageResource(imgid[position]);

            viewHolder.textView.setText(contacts[position]);
        return r;
    }

    class ViewHolder{
        TextView textView;
        ImageView imageView;

        ViewHolder(View v){
            textView=(TextView) v.findViewById(R.id.tv_lv_contact);
            imageView=(ImageView) v.findViewById(R.id.im_lv_contact);
        }
    }
}
