package com.sohel.drivermanagement.Admin.Adapters;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sohel.drivermanagement.R;

public class GridAdapter extends BaseAdapter {
    private Context context;
    private String[] values;
    private  int[] images;

    public GridAdapter(Context context, String[] values, int[] images) {
        this.context = context;
        this.values = values;
        this.images = images;
    }

    @Override
    public int getCount() {
        return values.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null){

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.grid_item_layout, parent, false);

            ImageView imageView=convertView.findViewById(R.id.admin_main_categoryImageView);
            TextView textView=convertView.findViewById(R.id.admin_main_TextViewid);

            imageView.setImageResource(images[position]);
            textView.setText(values[position]);




        }


        return convertView;
    }
}
