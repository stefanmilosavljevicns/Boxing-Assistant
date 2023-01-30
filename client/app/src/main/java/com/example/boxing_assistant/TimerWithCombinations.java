package com.example.boxing_assistant;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.boxing_assistant.Adapters.MyItemTouchHelper;
import com.example.boxing_assistant.Adapters.RecyclerViewAdapter;
import com.example.boxing_assistant.DataBaseTimer.TimeDatabase;
import com.example.boxing_assistant.DatabaseCombo.ComboDatabase;
import com.example.boxing_assistant.Models.Model;
import com.example.boxing_assistant.Models.ModelTimer;
import com.example.boxing_assistant.MusicDataBase.MusicDataBase;
import com.example.boxing_assistant.ViewModel.DialogComboRepository;
import com.example.boxing_assistant.ViewModel.DialogComboViewModel;
import com.example.boxing_assistant.databinding.FragmentTimerSaKombinacijamaBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class TimerWithCombinations extends Fragment implements RecyclerViewAdapter.OnNoteListener{
    View view;
    SwitchCompat switchCompat;
    RecyclerView recyclerView;
    private long lastClickTime = 0;
    private ArrayList<Model> mText = new ArrayList<>();
    List<Integer> idList;
    List<String> titleList;
    int getId;
    com.example.boxing_assistant.DataBaseTimer.Time time;
    TimeDatabase timeDataBase;
    private DialogComboViewModel sharedViewModel;
    RecyclerViewAdapter adapter;
    Button fightBtn;
    FloatingActionButton addCombo, backCombo;
    ImageButton save,select_preset;
    NumberPicker roundPicker;
    NumberPicker roundMinPicker;
    NumberPicker roundSecPicker;
    TextView titletext;
    NumberPicker restMinPicker;
    NumberPicker restSecPicker;
    NumberPicker prepareMinPicker;

    ComboDatabase main;
    NumberPicker prepareSecPicker;
    String title;
    DialogComboRepository dg;
    Spinner roundCounter;
    FragmentTimerSaKombinacijamaBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentTimerSaKombinacijamaBinding.inflate(inflater,container,false);
        view = binding.getRoot();
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(view).navigate(R.id.action_timer_Sa_Kombinacijama_to_basicPanel);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        dg = DialogComboRepository.getInstance();
        save = binding.SAVE;
        timeDataBase = TimeDatabase.getInstance(getContext());

        idList = timeDataBase.mainDao().getAllId();
        main = ComboDatabase.getInstance(getContext());
        backCombo = binding.comboBack;
        select_preset = binding.workoutPreset;
        switchCompat = binding.musicSwitchCombo;
        roundPicker = binding.roundCounterForcombo;
        roundMinPicker = binding.minRoundCounterForcombo;
        roundSecPicker = binding.secondRoundCounterForcombo;
        restMinPicker = binding.restCounterMinutesForcombo;
        restSecPicker = binding.restCounterSecondsForcombo;
        prepareMinPicker = binding.prepareCounterMinutesForcombo;
        prepareSecPicker = binding.prepareCounterSecondsForcombo;
        titletext = binding.title;
        Set_Clock_values(roundMinPicker);
        Set_Clock_values(roundSecPicker);
        Set_Clock_values(restMinPicker);
        Set_Clock_values(restSecPicker);
        prepareMinPicker.setMinValue(0);
        prepareMinPicker.setMaxValue(59);
        prepareMinPicker.setValue(0);
        prepareSecPicker.setMinValue(0);
        prepareSecPicker.setMaxValue(59);
        prepareSecPicker.setValue(15);
        roundPicker.setMaxValue(100);
        roundPicker.setMinValue(1);
        roundPicker.setValue(1);
        roundCounter = binding.spinnerCategoryForcombo;
        fightBtn = binding.fightBtn;
        addCombo = binding.addComb;
        titleList = timeDataBase.mainDao().getAlltitles();
        backCombo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_timer_Sa_Kombinacijama_to_basicPanel);
            }
        });
        titleList.add(0,"SELECT TIMER PLAN ");
        idList.add(0,0);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item, titleList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addCombo.setOnClickListener(this::add_combination);
        fightBtn.setOnClickListener(this::click);
        select_preset.setOnClickListener(this::select_preset);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(roundMinPicker.getValue() + roundSecPicker.getValue() > 0 && sharedViewModel.getNameData().getValue().size() > 0) {
                    if(titletext.getText().equals("Custom Plan")){
                        if (main.mainDao().getAllUsers().size() >= 10){
                            Toast.makeText(getContext(),"Maximum number of workout models are 10!",Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Navigation.findNavController(view).navigate(R.id.action_timer_Sa_Kombinacijama_to_dialogAddNewPlan);
                    }
                    else{
                        dg.databSet.setMoves(sharedViewModel.getNameData().getValue());
                        dg.databSet.setPrepareSec(sharedViewModel.getTimerData().getValue().getPrepareSec());
                        dg.databSet.setPreparetMin(sharedViewModel.getTimerData().getValue().getPrepareMin());
                        dg.databSet.setRestSec(sharedViewModel.getTimerData().getValue().getRestSec());
                        dg.databSet.setRestMin(sharedViewModel.getTimerData().getValue().getRestMin());
                        dg.databSet.setRoundSec(sharedViewModel.getTimerData().getValue().getRoundSec());
                        dg.databSet.setRoundMin(sharedViewModel.getTimerData().getValue().getRoundMin());
                        dg.databSet.setRoundNumber(sharedViewModel.getTimerData().getValue().getNumberOfRounds());
                        main.mainDao().update(dg.databSet);
                        Toast.makeText(getContext(),"Plan has been successfully updated!", Toast.LENGTH_SHORT).show();
                    }

                }
                else{
                    Toast.makeText(getContext(),"INVALID ROUND TIME OR COMBINATION LIST IS EMPTY!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        roundCounter.setAdapter(arrayAdapter);
        roundCounter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position !=0){
                    getId = idList.get(position);
                    time = timeDataBase.mainDao().findByUserId(getId);
                    dg.modTime.setRoundMin(time.getRoundMin());
                    dg.modTime.setRoundSec(time.getRoundSec());
                    dg.modTime.setNumberOfRounds(time.getRoundNumber());
                    dg.modTime.setRestMin(time.getRestMin());
                    dg.modTime.setRestSec(time.getRestSec());
                    dg.modTime.setPrepareMin(time.getPreparetMin());
                    dg.modTime.setPrepareSec(time.getPrepareSec());
                    roundPicker.setValue(dg.modTime.getNumberOfRounds());
                    roundMinPicker.setValue(dg.modTime.getRoundMin());
                    roundSecPicker.setValue(dg.modTime.getRoundSec());
                    restMinPicker.setValue(dg.modTime.getRestMin());
                    restSecPicker.setValue(dg.modTime.getRestSec());
                    prepareMinPicker.setValue(dg.modTime.getPrepareMin());
                    prepareSecPicker.setValue(dg.modTime.getPrepareSec());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        roundCounter.setBackgroundColor(Color.WHITE);
        sharedViewModel = ViewModelProviders.of(requireActivity()).get(DialogComboViewModel.class);
        sharedViewModel.init();
        title = dg.getTitle().getValue();
        if(title != null){
            titletext.setText(title);

        }
        else{
            titletext.setText("Custom Plan");

        }


        roundMinPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                dg.modTime.setRoundMin(newVal);
                //sharedviewmodel
            }
        });
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dg.musicPlayer = isChecked;
                sharedViewModel.getMusic().setValue(isChecked);
            }
        });
        roundSecPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                dg.modTime.setRoundSec(newVal);
                sharedViewModel.getTimerData().setValue(dg.modTime);
            }
        });
        restMinPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                dg.modTime.setRestMin(newVal);
                sharedViewModel.getTimerData().setValue(dg.modTime);
            }
        });
        restSecPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                dg.modTime.setRestSec(newVal);
                sharedViewModel.getTimerData().setValue(dg.modTime);
            }
        });
        prepareMinPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                dg.modTime.setPrepareMin(newVal);
                sharedViewModel.getTimerData().setValue(dg.modTime);
            }
        });
        prepareSecPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                dg.modTime.setPrepareSec(newVal);
                sharedViewModel.getTimerData().setValue(dg.modTime);
            }
        });
        roundPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                dg.modTime.setNumberOfRounds(newVal);
                sharedViewModel.getTimerData().setValue(dg.modTime);
            }
        });
        sharedViewModel.getMusic().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                switchCompat.setChecked(aBoolean);
            }
        });

        sharedViewModel.getTimerData().observe(getViewLifecycleOwner(), new Observer<ModelTimer>() {

            @Override
            public void onChanged(@Nullable ModelTimer modelTimer) {
                roundMinPicker.setValue(modelTimer.getRoundMin());
                roundSecPicker.setValue(modelTimer.getRoundSec());
                restMinPicker.setValue(modelTimer.getRestMin());
                restSecPicker.setValue(modelTimer.getRestSec());
                roundPicker.setValue(modelTimer.getNumberOfRounds());
                prepareMinPicker.setValue(modelTimer.getPrepareMin());
                prepareSecPicker.setValue(modelTimer.getPrepareSec());
            }
        });
        sharedViewModel.getNameData().observe(getViewLifecycleOwner(), new Observer<ArrayList<Model>>() {

            @Override
            public void onChanged(ArrayList<Model> models) {
                adapter.notifyDataSetChanged();
            }
        });
        initValues();
        roundCounter.setSelection(0);
        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    public void click(View v){
        if(roundMinPicker.getValue() + roundSecPicker.getValue() > 0 && sharedViewModel.getNameData().getValue().size() > 0){

            TimerWithCombinationsDirections.ActionTimerSaKombinacijamaToTimerComb fm = TimerWithCombinationsDirections.actionTimerSaKombinacijamaToTimerComb();
            fm.setRoundTimerMin(roundMinPicker.getValue());
            fm.setRoundTimerSec(roundSecPicker.getValue());
            fm.setRestTimerMin(restMinPicker.getValue());
            fm.setRestTimerSec(restSecPicker.getValue());
            fm.setPrepareTimerMin(prepareMinPicker.getValue());
            fm.setPrepareTimerSec(prepareSecPicker.getValue());
            fm.setRoundPicker(roundPicker.getValue());
            fm.setMusic(switchCompat.isChecked());
            if(switchCompat.isChecked()){
                MusicDataBase mdb = MusicDataBase.getInstance(getContext());
                if(mdb.mainDao().getAll().size() < 1){
                    Toast.makeText(getContext(),"Please add atleast on song to the list before checking this option",Toast.LENGTH_SHORT).show();
                    switchCompat.setChecked(false);
                    return;
                }
            }
            Navigation.findNavController(v).navigate(fm);
        }
        else{
            Toast.makeText(getContext(),"INVALID ROUND TIME OR COMBINATION LIST IS EMPTY!", Toast.LENGTH_SHORT).show();
        }
    }
    public void select_preset(View v){
        Navigation.findNavController(view).navigate(R.id.action_timer_Sa_Kombinacijama_to_workoutChooser);
    }
    public void add_combination(View v){
        if(adapter.getItemCount() >= 50) {
            Toast.makeText(getContext(),"Maximum number of moves are 50!",Toast.LENGTH_SHORT).show();
            return;
        }
        Navigation.findNavController(view).navigate(R.id.action_timer_Sa_Kombinacijama_to_dialog_for_combo);
    }
    private void initValues(){
       initRecyclerView();
    }
    private void initRecyclerView(){
        recyclerView =  binding.recikler;
       adapter = new RecyclerViewAdapter(sharedViewModel.getNameData().getValue(),this);
        ItemTouchHelper.Callback callback = new MyItemTouchHelper(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        adapter.setmTouchHelper(itemTouchHelper);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void Set_Clock_values(NumberPicker nm){
        nm.setMinValue(0);
        nm.setMaxValue(59);
        nm.setValue(0);
    }

    @Override
    public void onNoteClick(int position) {
        TimerWithCombinationsDirections.ActionTimerSaKombinacijamaToDialogForCombinationsUpdate tm = TimerWithCombinationsDirections.actionTimerSaKombinacijamaToDialogForCombinationsUpdate();
        tm.setIndex(position);

        Navigation.findNavController(view).navigate(tm);
    }




}