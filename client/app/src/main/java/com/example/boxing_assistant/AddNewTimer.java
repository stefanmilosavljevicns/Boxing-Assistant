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

import com.example.boxing_assistant.DataBaseTimer.Time;
import com.example.boxing_assistant.DataBaseTimer.TimeDatabase;
import com.example.boxing_assistant.Models.ModelTimer;
import com.example.boxing_assistant.ViewModel.BasicTimerRepository;
import com.example.boxing_assistant.ViewModel.TimerViewModel;
import com.example.boxing_assistant.databinding.FragmentAddNewTimerBinding;


public class AddNewTimer extends DialogFragment {
    FragmentAddNewTimerBinding binding;
    View view;
    Button cancel, add;
    ModelTimer modelTimer;
    EditText edit;
    TimeDatabase data;
    Time time;
    private TimerViewModel sharedViewModelTimer;
    private BasicTimerRepository bt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddNewTimerBinding.inflate(inflater, container, false);

        view = binding.getRoot();
        edit = binding.editTimer;
        cancel = binding.cancelTimer;
        add = binding.addTimer;
        data = TimeDatabase.getInstance(getContext());
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

        sharedViewModelTimer = ViewModelProviders.of(requireActivity()).get(TimerViewModel.class);
        sharedViewModelTimer.getTimerData();


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit.getText().length() > 0 && !edit.getText().equals("Custom Plan")) {
                    add();
                    time = new Time();
                    time.setName(edit.getText().toString());
                    time.setRoundNumber(modelTimer.getNumberOfRounds());
                    time.setRoundMin(modelTimer.getRoundMin());
                    time.setRoundSec(modelTimer.getRoundSec());
                    time.setRestMin(modelTimer.getRestMin());
                    time.setRestSec(modelTimer.getRestSec());
                    time.setPreparetMin(modelTimer.getPrepareMin());
                    time.setPrepareSec(modelTimer.getPrepareSec());
                    data.mainDao().insert(time);
                    bt.timeSet = time;
                    bt.timeSet.setId(time.getId());
                    bt.titleSet = time.getName();
                    cancel();
                } else {
                    Toast.makeText(getContext(), "Please enter valid title", Toast.LENGTH_LONG).show();
                }
            }
        });
        return view;
    }

    void add() {

        modelTimer = sharedViewModelTimer.getTimerData().getValue();
        bt = BasicTimerRepository.getInstance();


    }

    void cancel() {

        NavHostFragment.findNavController(this).navigate(R.id.action_addNewTimer_to_basic_Timer);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}