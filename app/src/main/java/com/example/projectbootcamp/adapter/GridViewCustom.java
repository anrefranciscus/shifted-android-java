package com.example.projectbootcamp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.example.projectbootcamp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class GridViewCustom extends BaseAdapter {
    //Initialize Context
    private Context mcontext;
    //Initialize HasMap
    private ArrayList<HashMap<String, String>> listProducts;

    public GridViewCustom(FragmentActivity activity, ArrayList<HashMap<String, String>> listProducts) {
        this.listProducts = listProducts;
        this.mcontext = activity;

    }

    @Override
    public int getCount() {
        return (listProducts != null) ? listProducts.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return listProducts.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View gridView;
        LayoutInflater inflater = (LayoutInflater) mcontext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            gridView = inflater.inflate(R.layout.list_item_products, null);
            ImageView imageView = gridView.findViewById(R.id.gridImage);
            TextView textTitle = gridView.findViewById(R.id.gridTitle);
            TextView textPrice = gridView.findViewById(R.id.gridPrice);

            RatingBar ratingBar = gridView.findViewById(R.id.ratingProduct);

            HashMap<String, String> map = new HashMap<String, String>();


            map = listProducts.get(i);
            Log.d("CEK MAP ADAPTER", "getView: " + map);

            textTitle.setText(map.get("title"));
            textPrice.setText(map.get("price"));

            // Convert Rating
            Float convertRating = new Float(map.get("rating"));
            ratingBar.setRating(convertRating);

            //Picasso Load Image By Url
            Picasso.get().load(map.get("image")).into(imageView);

        } else {
            gridView = convertView;
        }
        return gridView;
    }
}
