package com.example.prototype;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class HomeAdapter extends ArrayAdapter<HomeItem> {


    public HomeAdapter(@NonNull Context context, ArrayList<HomeItem> ItemList) {
        super(context, 0, ItemList);
    }

    @NonNull
    @Override

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            // Inflate the item layout if it's not already provided
            view = LayoutInflater.from(getContext()).inflate(R.layout.grid_item, parent, false);
        }

        HomeItem homeItem = getItem(position);

        TextView tv_name = view.findViewById(R.id.tv_item_grid);
        ImageView tv_image = view.findViewById(R.id.tv_item_image);

        tv_name.setText(homeItem.getItem_name());
        tv_image.setImageResource(homeItem.getID_image());

        return view;
    }
}
