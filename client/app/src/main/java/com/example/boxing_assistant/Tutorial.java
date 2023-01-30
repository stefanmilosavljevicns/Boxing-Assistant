package com.example.boxing_assistant;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.boxing_assistant.databinding.FragmentTutorial2Binding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class Tutorial extends Fragment {
    FragmentTutorial2Binding binding;
    View view;
    FloatingActionButton back, left, right;
    ScrollView main;
    TextView tut1, tut2, tut3, tut4;
    int tutPage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        tutPage = 1;

        binding = FragmentTutorial2Binding.inflate(inflater, container, false);

        view = binding.getRoot();
        back = binding.tutBack;
        left = binding.goleftTut;
        right = binding.gorightTut;
        tut1 = binding.tutorialTimer;
        tut2 = binding.tutorialCombo;
        tut3 = binding.tutorialMusic;
        tut4 = binding.tutorialCamera;
        main = binding.mainScroll;
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tutPage > 1) {
                    tutPage--;
                    load_tut();

                }
            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tutPage < 4) {
                    tutPage++;
                    load_tut();

                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_tutorial_to_basicPanel);
            }
        });

        return view;
    }

    private void load_tut() {
        switch (tutPage) {
            case 1:
                main.smoothScrollTo(0, tut1.getTop());
                break;
            case 2:
                main.smoothScrollTo(0, tut2.getTop());
                break;

            case 3:
                main.smoothScrollTo(0, tut3.getTop());
                break;

            case 4:
                main.smoothScrollTo(0, tut4.getTop());
                break;

        }
    }
}