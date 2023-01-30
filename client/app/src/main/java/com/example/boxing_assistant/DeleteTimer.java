package com.example.boxing_assistant;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.boxing_assistant.DataBaseTimer.Time;
import com.example.boxing_assistant.DataBaseTimer.TimeDatabase;
import com.example.boxing_assistant.ViewModel.BasicTimerRepository;
import com.example.boxing_assistant.databinding.FragmentDeletetimerBinding;


public class DeleteTimer extends DialogFragment {
    FragmentDeletetimerBinding binding;
    View view;
    int id;
    TimeDatabase data;
    Time time;
    BasicTimerRepository dg;
    TextView title;
    Button yes, no;
    String text;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDeletetimerBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        data = TimeDatabase.getInstance(getContext());
        title = binding.confirmTimer;
        yes = binding.yesTimer;
        no = binding.noTimer;
        no.setOnClickListener(this::setNo);
        id = DeleteTimerArgs.fromBundle(getArguments()).getID();
        time = data.mainDao().findByUserId(id);
        text = "Are you sure you want to delete " + time.getName() + " ?";
        title.setText(text);
        yes.setOnClickListener(this::setYes);
        no.setOnClickListener(this::setNo);


        return view;
    }

    void setYes(View view) {
        dg = BasicTimerRepository.getInstance();

        if (dg.getTime().getValue() != null) {
            if (dg.getTime().getValue().getId() == id) {

                clear();
            } else if (dg.getTime().getValue().getId() == 0 && dg.getTime().getValue().getName().equals(time.getName())) {
                clear();
            }
        }

        data.mainDao().delete(time);
        NavHostFragment.findNavController(this).navigate(R.id.action_deletetimer_to_timerChooser);
    }

    void setNo(View view) {
        NavHostFragment.findNavController(this).navigate(R.id.action_deletetimer_to_timerChooser);
    }

    void clear() {
        dg.titleSet = null;
        dg.timeSet = null;
        dg.modTime.setPrepareSec(0);
        dg.modTime.setPrepareMin(0);
        dg.modTime.setNumberOfRounds(1);
        dg.modTime.setRestSec(0);
        dg.modTime.setRestMin(0);
        dg.modTime.setRoundSec(0);
        dg.modTime.setRoundMin(0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}