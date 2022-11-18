package com.example.projectbootcamp.activities.ui.menu_1;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.projectbootcamp.R;
import com.example.projectbootcamp.enums.Apis;
import com.example.projectbootcamp.enums.Params;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeFragment extends Fragment {
    private HomeFragment binding;
    private static final String TAG = "ReadKuliner";

    ImageView splashScreen;
    private ArrayList<HashMap<String, String>> listKuliner;
    private ListView listView;

    public ProgressDialog progressDialog;

    String namaKuliner, keterangan;
    SwipeRefreshLayout swipeRefreshLayout;

    SimpleAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        listKuliner = new ArrayList<>();
        View contentView = inflater.inflate(R.layout.fragment_home, container, false);

        listView = contentView.findViewById(R.id.listViewKuliner);

        swipeRefreshLayout = contentView.findViewById(R.id.fragmentHome);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listKuliner.clear();
                getData();
                swipeRefreshLayout.setRefreshing(false);
                swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.YELLOW, Color.BLUE);
            }
        });


        // Searcb Text
        EditText searchText = contentView.findViewById(R.id.searchText);
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
//                filterListKuliener(editable.toString());
            }
        });

        getData();
        return contentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void setLoading() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Mohon tunggu...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void getData() {
        setLoading();
        AndroidNetworking.initialize(getActivity());
        AndroidNetworking.get(Apis.APIS_REQUEST_DATA)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "response" + response);
                        try {
                            JSONArray data = response.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject res = data.getJSONObject(i);
                                namaKuliner = res.getString(Params.JSON_NAME);
                                keterangan = res.getString(Params.JSON_KETERANGAN);
                                HashMap<String, String> listFood = new HashMap<>();
                                listFood.put("nama", namaKuliner);
                                listFood.put("keterangan", keterangan);
                                listKuliner.add(listFood);
                            }
                            Log.d(TAG, "onResponse: " + listKuliner);
                            adapter = new SimpleAdapter(getActivity(), listKuliner, R.layout.list_item,
                                    new String[]{"nama", "keterangan"}, new int[]{R.id.label1, R.id.label2}
                            );
                            listView.setAdapter(adapter);
                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.d(TAG, "onError: " + error);
                    }
                });
    }
}