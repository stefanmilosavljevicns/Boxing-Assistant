package com.example.boxing_assistant;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.boxing_assistant.DataBaseTimer.Time;
import com.example.boxing_assistant.DataBaseTimer.TimeDatabase;
import com.example.boxing_assistant.DatabaseCombo.Combo;
import com.example.boxing_assistant.DatabaseCombo.ComboDatabase;
import com.example.boxing_assistant.ViewModel.BasicTimerRepository;
import com.example.boxing_assistant.ViewModel.DialogComboRepository;
import com.example.boxing_assistant.databinding.FragmentRenameWorkoutBinding;

public class RenameWorkout extends DialogFragment {
    int id;
    ComboDatabase mdb;
    TimeDatabase tdb;
    private FragmentRenameWorkoutBinding binding;
    private View view;
    private Button rename, cancel;
    private EditText renamtxt;
    private Boolean isTimer, isWorkout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRenameWorkoutBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        id = RenameWorkoutArgs.fromBundle(getArguments()).getId();
        isWorkout = RenameWorkoutArgs.fromBundle(getArguments()).getIsWorkout();
        isTimer = RenameWorkoutArgs.fromBundle(getArguments()).getIsTimer();
        rename = binding.addTimerRename;
        cancel = binding.cancelTimerRename;
        renamtxt = binding.editTimerRename;
        if (isTimer) {
            tdb = TimeDatabase.getInstance(getContext());
        } else if (isWorkout) {
            mdb = ComboDatabase.getInstance(getContext());
        }
        rename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTimer) {
                    if (!renamtxt.getText().toString().equals("Custom Plan")) {
                        renameTimer();
                    } else {
                        Toast.makeText(getContext(), "Invalid title name", Toast.LENGTH_SHORT).show();
                    }
                } else if (isWorkout) {
                    if (!renamtxt.getText().toString().equals("Custom Plan")) {
                        rename();
                    } else {
                        Toast.makeText(getContext(), "Invalid title name", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
        return view;
    }

    private void rename() {
        Combo updateCombo = mdb.mainDao().findByUserId(id);
        DialogComboRepository bc = DialogComboRepository.getInstance();
        if (bc.titleSet != null) {
            if (bc.titleSet.equals(updateCombo.getName())) {
                bc.titleSet = renamtxt.getText().toString();
            }
        }
        if (renamtxt.getText().length() > 0) {
            updateCombo.setName(renamtxt.getText().toString());
            mdb.mainDao().update(updateCombo);
            NavHostFragment.findNavController(this).navigate(R.id.action_renameWorkout_to_workoutChooser);
        } else {
            Toast.makeText(getContext(), "INVALID NAME", Toast.LENGTH_SHORT).show();
        }

    }

    private void renameTimer() {
        Time updateTime = tdb.mainDao().findByUserId(id);
        BasicTimerRepository bc = BasicTimerRepository.getInstance();
        if (bc.titleSet != null) {
            if (updateTime.getName().equals(bc.titleSet)) {
                bc.titleSet = renamtxt.getText().toString();
                Toast.makeText(getContext(), "DONE", Toast.LENGTH_SHORT).show();
            }
        }


        if (renamtxt.getText().length() > 0) {
            updateTime.setName(renamtxt.getText().toString());
            tdb.mainDao().update(updateTime);
            NavHostFragment.findNavController(this).navigate(R.id.action_renameWorkout_to_timerChooser);
        } else {
            Toast.makeText(getContext(), "INVALID NAME", Toast.LENGTH_SHORT).show();
        }
    }

    private void cancel() {
        if (isTimer) {
            NavHostFragment.findNavController(this).navigate(R.id.action_renameWorkout_to_timerChooser);
        } else if (isWorkout) {
            NavHostFragment.findNavController(this).navigate(R.id.action_renameWorkout_to_workoutChooser);
        }
    }
}