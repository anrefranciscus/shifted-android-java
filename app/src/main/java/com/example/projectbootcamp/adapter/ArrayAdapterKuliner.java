package com.example.projectbootcamp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.example.projectbootcamp.R;
import com.example.projectbootcamp.models.DataKuliner;

import java.util.ArrayList;

public class ArrayAdapterKuliner extends ArrayAdapter <DataKuliner> {

    public ArrayAdapterKuliner(@NonNull FragmentActivity context, @NonNull ArrayList<DataKuliner> kulinerList) {
        super(context, 0, kulinerList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View currentItemView = convertView;
        if (currentItemView == null){
            currentItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        }


        DataKuliner dataKulinerPosition = getItem(position);
        assert dataKulinerPosition != null;


        TextView textName = currentItemView.findViewById(R.id.label1);
        textName.setText(dataKulinerPosition.getNamaKuliner());
        TextView textKeterangan = currentItemView.findViewById(R.id.label2);
        textKeterangan.setText(dataKulinerPosition.getKeterangan());

        return currentItemView;
    }
}
