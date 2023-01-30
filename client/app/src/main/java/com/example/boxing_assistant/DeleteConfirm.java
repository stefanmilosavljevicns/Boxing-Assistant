package com.example.boxing_assistant;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.navigation.fragment.NavHostFragment;


import com.example.boxing_assistant.DatabaseCombo.Combo;
import com.example.boxing_assistant.DatabaseCombo.ComboDatabase;
import com.example.boxing_assistant.ViewModel.DialogComboRepository;
import com.example.boxing_assistant.databinding.FragmentDeleteconfirmBinding;


public class DeleteConfirm extends DialogFragment {
    FragmentDeleteconfirmBinding binding;

    int id;
    ComboDatabase main;
    DialogComboRepository dg;
    Combo combo;
    TextView title;
    Button yes, no;
    String text;

    @SuppressLint("StringFormatInvalid")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDeleteconfirmBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        title = binding.confirmButton;
        yes = binding.yes;
        no = binding.no;
        main = ComboDatabase.getInstance(getContext());
        id = DeleteConfirmArgs.fromBundle(getArguments()).getID();
        combo = main.mainDao().findByUserId(id);
        text = "Are you sure you want to delete " + combo.getName() + " ?";
        title.setText(text);
        yes.setOnClickListener(this::setYes);
        no.setOnClickListener(this::setNo);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    void setYes(View view) {
        dg = DialogComboRepository.getInstance();
        if (dg.getDataB().getValue() != null) {
            if (dg.getDataB().getValue().getID() == combo.getID()) {
                clear();
            } else if (dg.getDataB().getValue().getID() == 0 && dg.getDataB().getValue().getName().equals(combo.getName())) {
                clear();
            }
        }
        main.mainDao().delete(combo);
        NavHostFragment.findNavController(this).navigate(R.id.action_deleteconfirm_to_workoutChooser);
    }

    void setNo(View view) {
        NavHostFragment.findNavController(this).navigate(R.id.action_deleteconfirm_to_workoutChooser);
    }

    void clear() {
        dg.dataSet.clear();
        dg.titleSet = null;
        dg.databSet = null;
        dg.modTime.setPrepareSec(0);
        dg.modTime.setPrepareMin(0);
        dg.modTime.setNumberOfRounds(1);
        dg.modTime.setRestSec(0);
        dg.modTime.setRestMin(0);
        dg.modTime.setRoundSec(0);
        dg.modTime.setRoundMin(0);
    }
}