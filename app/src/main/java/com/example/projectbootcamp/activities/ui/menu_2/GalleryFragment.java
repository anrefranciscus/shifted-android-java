package com.example.projectbootcamp.activities.ui.menu_2;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.example.projectbootcamp.R;
import com.example.projectbootcamp.adapter.GridViewCustom;
import com.example.projectbootcamp.databinding.FragmentGalleryBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;
    public ProgressDialog progressDialog;

    GridView gridView;

    String iconList, title, price, rating;

    ArrayList<HashMap<String, String>> listProducts;
    GridViewCustom gridViewCustom;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Menu2ViewModel slideshowViewModel =
                new ViewModelProvider(this).get(Menu2ViewModel.class);

        View contentView = inflater.inflate(R.layout.fragment_gallery, container, false);
        gridView = contentView.findViewById(R.id.gridView);
        listProducts = new ArrayList<>();
        getDataGallery();
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

    public void getDataGallery() {
        setLoading();
        AndroidNetworking.initialize(getActivity());
        AndroidNetworking.get("https://fakestoreapi.com/products")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Cek response", "onResponse: " + response);
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject res = response.getJSONObject(i);
                                Log.d("responseku", "onResponse: " + res);
                                title = res.getString("title");
                                iconList = res.getString("image");
                                price = res.getString("price");

                                BigDecimal bigDecimal = new BigDecimal(price);
                                NumberFormat usFormat = NumberFormat.getCurrencyInstance(Locale.US);
                                String formatted = usFormat.format(bigDecimal);

                                // GET RATING
                                JSONObject getRating = res.getJSONObject("rating");
                                rating = getRating.getString("rate");
                                Log.d("CEK RATING", "onResponse: " + getRating);
                                HashMap<String, String> listProduct = new HashMap<>();


                                listProduct.put("title", title);
                                listProduct.put("image", iconList);
                                listProduct.put("price", formatted);
                                listProduct.put("rating", rating);

                                listProducts.add(listProduct);
                                Log.d("CEK HARGA", "onResponse: " + formatted);

                                Log.d("CEK PRODUCT HASMAP", "onResponse: " + listProduct);
                            }
                            gridViewCustom = new GridViewCustom(getActivity(), listProducts);
                            gridView.setAdapter(gridViewCustom);
                            progressDialog.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.d("Cek error", "onError: " + error);
                        progressDialog.dismiss();
                    }
                });
    }
}