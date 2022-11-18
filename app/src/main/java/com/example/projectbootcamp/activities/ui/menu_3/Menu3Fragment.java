package com.example.projectbootcamp.activities.ui.menu_3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.projectbootcamp.databinding.FragmentMenu3Binding;

public class Menu3Fragment extends Fragment {

    private FragmentMenu3Binding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Menu3ViewModel slideshowViewModel =
                new ViewModelProvider(this).get(Menu3ViewModel.class);


        binding = FragmentMenu3Binding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textReview;
        slideshowViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}