package com.example.boxing_assistant;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.boxing_assistant.databinding.FragmentMainLoadingBinding;


public class MainLoading extends Fragment {
    FragmentMainLoadingBinding binding;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentMainLoadingBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        return view;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}
