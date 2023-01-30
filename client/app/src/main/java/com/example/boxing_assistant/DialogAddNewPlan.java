package com.example.boxing_assistant;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;

import com.example.boxing_assistant.DatabaseCombo.Combo;
import com.example.boxing_assistant.DatabaseCombo.ComboDatabase;
import com.example.boxing_assistant.Models.ModelTimer;
import com.example.boxing_assistant.ViewModel.DialogComboRepository;
import com.example.boxing_assistant.ViewModel.DialogComboViewModel;
import com.example.boxing_assistant.databinding.FragmentDialogAddNewPlanBinding;


public class DialogAddNewPlan extends DialogFragment {
    FragmentDialogAddNewPlanBinding binding;
    Button cancel, add;
    private DialogComboViewModel sharedViewModel;


    ModelTimer modelTimer;
    EditText edit;
    View view;
    Combo combo;
    ComboDatabase main;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDialogAddNewPlanBinding.inflate(inflater, container, false);
        sharedViewModel = ViewModelProviders.of(requireActivity()).get(DialogComboViewModel.class);
        sharedViewModel.init();
        DialogComboRepository dg;
        dg = DialogComboRepository.getInstance();
        main = ComboDatabase.getInstance(getContext());
        modelTimer = sharedViewModel.getTimerData().getValue();
        view = binding.getRoot();
        edit = binding.edit;
        add = binding.ADD;
        cancel = binding.CANCEL;
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit.getText().length() > 0 && !edit.getText().equals("Custom Plan")) {
                    combo = new Combo();
                    combo.setMoves(sharedViewModel.getNameData().getValue());
                    combo.setName(edit.getText().toString());
                    combo.setRoundNumber(modelTimer.getNumberOfRounds());
                    combo.setRoundMin(modelTimer.getRoundMin());
                    combo.setRoundSec(modelTimer.getRoundSec());
                    combo.setRestMin(modelTimer.getRestMin());
                    combo.setRestSec(modelTimer.getRestSec());
                    combo.setPreparetMin(modelTimer.getPrepareMin());
                    combo.setPrepareSec(modelTimer.getPrepareSec());
                    main.mainDao().insert(combo);
                    dg.titleSet = combo.getName();
                    dg.databSet = combo;
                    cancel();
                } else {
                    Toast.makeText(getContext(), "Please enter valid title", Toast.LENGTH_LONG).show();
                }
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    void cancel() {
        NavHostFragment.findNavController(this).navigate(R.id.action_dialogAddNewPlan_to_timer_Sa_Kombinacijama);
    }

}