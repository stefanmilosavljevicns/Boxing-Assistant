package com.example.boxing_assistant;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.boxing_assistant.Adapters.RecyclerAdapterForTimer;
import com.example.boxing_assistant.DataBaseTimer.Time;
import com.example.boxing_assistant.DataBaseTimer.TimeDatabase;
import com.example.boxing_assistant.ViewModel.BasicTimerRepository;
import com.example.boxing_assistant.databinding.FragmentTimerChooserBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
public class TimerChooser extends Fragment implements RecyclerAdapterForTimer.OnNoteListener {
    private RecyclerView recyclerView;
    RecyclerAdapterForTimer recyclerAdapterForTimer;
    View view;
    FloatingActionButton backTimechooser;
    List<Time> plansArrayList;
    public TimeDatabase main;
    Button newplan;
    int id;
    BasicTimerRepository btr;
    FragmentTimerChooserBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTimerChooserBinding.inflate(inflater,container,false);
        view = binding.getRoot();
        btr = BasicTimerRepository.getInstance();
        newplan = binding.createnewtimer;
        backTimechooser = binding.timerPresetBack;
        main = TimeDatabase.getInstance(getContext());
        recyclerView = binding.timerchooserRecyclerview;
        plansArrayList = main.mainDao().getAllData();
        recyclerAdapterForTimer = new RecyclerAdapterForTimer(plansArrayList,this);
        recyclerView.setAdapter(recyclerAdapterForTimer);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        backTimechooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_timerChooser_to_basic_Timer);
                if(btr.titleSet != null){
                    Log.d("ID",btr.titleSet);
                }
                else{
                    Log.d("ID","NEMA");
                }

            }
        });
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(view).navigate(R.id.action_timerChooser_to_basic_Timer);
                if(btr.titleSet != null){
                    Log.d("ID",btr.titleSet);
                }
                else{
                    Log.d("ID","NEMA");
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        newplan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove();
            }
        });
        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onNoteClick(int position) {


            btr.modTime.setNumberOfRounds(plansArrayList.get(position).getRoundNumber());
            btr.modTime.setRoundMin(plansArrayList.get(position).getRoundMin());
            btr.modTime.setRoundSec(plansArrayList.get(position).getRoundSec());
            btr.modTime.setRestMin(plansArrayList.get(position).getRestMin());
            btr.modTime.setRestSec(plansArrayList.get(position).getRestSec());
            btr.modTime.setPrepareMin(plansArrayList.get(position).getPreparetMin());
            btr.modTime.setPrepareSec(plansArrayList.get(position).getPrepareSec());
            btr.titleSet = plansArrayList.get(position).getName();
            btr.timeSet = plansArrayList.get(position);
            btr.timeSet.setId(plansArrayList.get(position).getId());
            Navigation.findNavController(view).navigate(R.id.action_timerChooser_to_basic_Timer);

    }
    void remove(){
        btr.titleSet = null;
        btr.modTime.setPrepareSec(0);
        btr.modTime.setPrepareMin(0);
        btr.modTime.setNumberOfRounds(1);
        btr.modTime.setRestSec(0);
        btr.modTime.setRestMin(0);
        btr.modTime.setRoundSec(0);
        btr.modTime.setRoundMin(0);
        Navigation.findNavController(view).navigate(R.id.action_timerChooser_to_basic_Timer);
    }
    @Override
    public void onButtonClick(int position) {
        id = plansArrayList.get(position).getId();
        TimerChooserDirections.ActionTimerChooserToDeletetimer tm = TimerChooserDirections.actionTimerChooserToDeletetimer(id);
        tm.setID(id);
        NavHostFragment.findNavController(this).navigate(tm);
    }

    @Override
    public void onRenameClick(int position) {
        int id;
        id = plansArrayList.get(position).getId();
        TimerChooserDirections.ActionTimerChooserToRenameWorkout tm = TimerChooserDirections.actionTimerChooserToRenameWorkout();
        tm.setId(id);
        tm.setIsTimer(true);
        NavHostFragment.findNavController(this).navigate(tm);
    }
}