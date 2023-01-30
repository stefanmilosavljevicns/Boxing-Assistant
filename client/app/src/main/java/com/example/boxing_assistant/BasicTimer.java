package com.example.boxing_assistant;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.example.boxing_assistant.DataBaseTimer.TimeDatabase;
import com.example.boxing_assistant.Models.ModelTimer;
import com.example.boxing_assistant.MusicDataBase.MusicDataBase;
import com.example.boxing_assistant.ViewModel.BasicTimerRepository;
import com.example.boxing_assistant.ViewModel.TimerViewModel;
import com.example.boxing_assistant.databinding.FragmentBasicTimerBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class BasicTimer extends Fragment {
    private TimerViewModel sharedViewModel;
    NumberPicker round_picker;
    NumberPicker round_min_picker;
    NumberPicker round_sec_picker;
    NumberPicker rest_min_picker;
    NumberPicker rest_sec_picker;
    NumberPicker prepare_min_picker;
    NumberPicker prepare_sec_picker;
    FloatingActionButton back;
    FragmentBasicTimerBinding binding;
    TextView titletext;
    SwitchCompat switchCompat;
    View view;
    Button fight;
    ImageButton preset, save;
    BasicTimerRepository dig;
    TimeDatabase data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        data = TimeDatabase.getInstance(getContext());
        dig = BasicTimerRepository.getInstance();
        binding = FragmentBasicTimerBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        back = binding.timerBack;
        switchCompat = binding.musicSwitch;
        round_picker = binding.roundCounter;
        titletext = binding.titletext;
        round_min_picker = binding.minRoundCounter;
        round_sec_picker = binding.secondRoundCounter;
        rest_min_picker = binding.restCounterMinutes;
        rest_sec_picker = binding.restCounterSeconds;
        prepare_min_picker = binding.prepareCounterMinutes;
        prepare_sec_picker = binding.prepareCounterSeconds;
        preset = binding.timerPreset;
        save = binding.saveTimer;
        String title;

        Set_Clock_values(round_min_picker);
        Set_Clock_values(round_sec_picker);
        Set_Clock_values(rest_min_picker);
        Set_Clock_values(rest_sec_picker);
        prepare_min_picker.setMinValue(0);
        prepare_min_picker.setMaxValue(59);
        prepare_min_picker.setValue(0);
        prepare_sec_picker.setMinValue(0);
        prepare_sec_picker.setMaxValue(59);
        prepare_sec_picker.setValue(15);
        round_picker.setMaxValue(100);
        round_picker.setMinValue(1);
        round_picker.setValue(1);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_basic_Timer_to_basicPanel);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (round_min_picker.getValue() + round_sec_picker.getValue() > 0) {
                    if (titletext.getText().equals("Custom Plan")) {
                        if (data.mainDao().getAllData().size() >= 10) {
                            Toast.makeText(getContext(), "Maximum number of time models are 10!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Navigation.findNavController(view).navigate(R.id.action_basic_Timer_to_addNewTimer);
                    } else {
                        dig.timeSet.setPrepareSec(sharedViewModel.getTimerData().getValue().getPrepareSec());
                        dig.timeSet.setPreparetMin(sharedViewModel.getTimerData().getValue().getPrepareMin());
                        dig.timeSet.setRestSec(sharedViewModel.getTimerData().getValue().getRestSec());
                        dig.timeSet.setRestMin(sharedViewModel.getTimerData().getValue().getRestMin());
                        dig.timeSet.setRoundSec(sharedViewModel.getTimerData().getValue().getRoundSec());
                        dig.timeSet.setRoundMin(sharedViewModel.getTimerData().getValue().getRoundMin());
                        dig.timeSet.setRoundNumber(sharedViewModel.getTimerData().getValue().getNumberOfRounds());
                        data.mainDao().update(dig.timeSet);
                        Toast.makeText(getContext(), "Plan has been successfully updated!", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(getContext(), "INVALID ROUND TIME OR COMBINATION LIST IS EMPTY!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(view).navigate(R.id.action_basic_Timer_to_basicPanel);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        preset.setOnClickListener(this::load_preset);
        sharedViewModel = ViewModelProviders.of(requireActivity()).get(TimerViewModel.class);
        sharedViewModel.init();
        round_min_picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                dig.modTime.setRoundMin(newVal);
                sharedViewModel.getTimerData().setValue(dig.modTime);
            }
        });
        round_sec_picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                dig.modTime.setRoundSec(newVal);
                sharedViewModel.getTimerData().setValue(dig.modTime);
            }
        });
        rest_min_picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                dig.modTime.setRestMin(newVal);
                sharedViewModel.getTimerData().setValue(dig.modTime);
            }
        });
        rest_sec_picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                dig.modTime.setRestSec(newVal);
                sharedViewModel.getTimerData().setValue(dig.modTime);
            }
        });
        prepare_min_picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                dig.modTime.setPrepareMin(newVal);
                sharedViewModel.getTimerData().setValue(dig.modTime);
            }
        });
        prepare_sec_picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                dig.modTime.setPrepareSec(newVal);
                sharedViewModel.getTimerData().setValue(dig.modTime);
            }
        });
        round_picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                dig.modTime.setNumberOfRounds(newVal);
                sharedViewModel.getTimerData().setValue(dig.modTime);
            }
        });
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dig.musicPlayer = isChecked;
                sharedViewModel.getMusic().setValue(isChecked);
            }
        });
        sharedViewModel.getTimerData().observe(getViewLifecycleOwner(), new Observer<ModelTimer>() {

            @Override
            public void onChanged(@Nullable ModelTimer modelTimer) {
                round_min_picker.setValue(modelTimer.getRoundMin());
                round_sec_picker.setValue(modelTimer.getRoundSec());
                rest_min_picker.setValue(modelTimer.getRestMin());
                rest_sec_picker.setValue(modelTimer.getRestSec());
                round_picker.setValue(modelTimer.getNumberOfRounds());
                prepare_min_picker.setValue(modelTimer.getPrepareMin());
                prepare_sec_picker.setValue(modelTimer.getPrepareSec());
            }
        });
        sharedViewModel.getMusic().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                switchCompat.setChecked(aBoolean);
            }
        });

        fight = binding.fightBtn;
        fight.setOnClickListener(this::click);

        title = dig.getTitle().getValue();
        if (title != null) {
            titletext.setText(title);

        } else {
            titletext.setText("Custom Plan");
        }


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    void load_preset(View v) {

        Navigation.findNavController(view).navigate(R.id.action_basic_Timer_to_timerChooser);
    }

    public void Set_Clock_values(NumberPicker nm) {
        nm.setMinValue(0);
        nm.setMaxValue(59);
        nm.setValue(0);
    }

    public void click(View v) {
        if (round_min_picker.getValue() + round_sec_picker.getValue() > 0) {
            BasicTimerDirections.ActionBasicTimerToTimer fm = BasicTimerDirections.actionBasicTimerToTimer();
            fm.setRoundTimerMin(round_min_picker.getValue());
            fm.setRoundTimerSec(round_sec_picker.getValue());
            fm.setRestTimerMin(rest_min_picker.getValue());
            fm.setRestTimerSec(rest_sec_picker.getValue());
            fm.setPrepareTimerMin(prepare_min_picker.getValue());
            fm.setPrepareTimerSec(prepare_sec_picker.getValue());
            fm.setRoundPicker(round_picker.getValue());
            fm.setMusic(switchCompat.isChecked());
            if (switchCompat.isChecked()) {
                MusicDataBase mdb = MusicDataBase.getInstance(getContext());
                if (mdb.mainDao().getAll().size() < 1) {
                    Toast.makeText(getContext(), "Please add atleast on song to the list before checking this option", Toast.LENGTH_SHORT).show();
                    switchCompat.setChecked(false);
                    return;
                }
            }
            Navigation.findNavController(view).navigate(fm);


        } else {
            Toast.makeText(getContext(), "INVALID ROUND TIME!", Toast.LENGTH_SHORT).show();
        }
    }
}