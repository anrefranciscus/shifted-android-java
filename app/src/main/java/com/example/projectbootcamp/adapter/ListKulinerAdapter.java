package com.example.projectbootcamp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectbootcamp.R;
import com.example.projectbootcamp.models.DataKuliner;

import java.util.ArrayList;
import java.util.List;

public class ListKulinerAdapter extends RecyclerView.Adapter<ListKulinerAdapter.ViewHolder> {
    private List<DataKuliner> dataKuliner;
    private Context mContext;

    public ListKulinerAdapter(FragmentActivity activity, List<DataKuliner> dataKuliner) {
        this.mContext = activity;
        this.dataKuliner = dataKuliner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutInflater = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.list_item, parent, false);
        return new ViewHolder(layoutInflater);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nama.setText(dataKuliner.get(position).getNamaKuliner());
        holder.keterangan.setText(dataKuliner.get(position).getKeterangan());
    }

    @Override
    public int getItemCount() {
        return (dataKuliner != null) ? dataKuliner.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nama, keterangan, kontributor, lat, lon, status;

        public ViewHolder(View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.label1);
            keterangan = itemView.findViewById(R.id.label2);
        }
    }

    public void setFilterdList(ArrayList<DataKuliner> filterdList) {
        this.dataKuliner = filterdList;
        notifyDataSetChanged();
    }

}
