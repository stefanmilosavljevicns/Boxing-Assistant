package com.example.boxing_assistant;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.boxing_assistant.Adapters.MyItemTouchHelperForCombo;
import com.example.boxing_assistant.Adapters.RecyclerViewAdapterForCombos;
import com.example.boxing_assistant.Models.Model;
import com.example.boxing_assistant.Models.PrimitiveTimer;
import com.example.boxing_assistant.ViewModel.DialogComboRepository;
import com.example.boxing_assistant.ViewModel.DialogComboViewModel;
import com.example.boxing_assistant.ViewModel.DialogRepository;
import com.example.boxing_assistant.ViewModel.DialogViewModel;
import com.example.boxing_assistant.databinding.FragmentDialogForComboBinding;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class DialogForCombo extends DialogFragment {
    View view;
    NumberPicker numberPicker;
    RecyclerView rc;
    Button btn, addit;
    NumberPicker sec, min;
    EditText combo_name;
    long total;
    DialogComboRepository dg;
    String export;
    DialogRepository dig;
    RecyclerViewAdapterForCombos adapter;
    FragmentDialogForComboBinding binding;
    private DialogViewModel sharedDialogViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDialogForComboBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        sharedDialogViewModel = ViewModelProviders.of(requireActivity()).get(DialogViewModel.class);
        sharedDialogViewModel.init();
        numberPicker = binding.numberOfRep;
        dig = DialogRepository.getInstance();
        addit = binding.addToList;
        sec = binding.secComboDialog;
        min = binding.minComboDialog;
        numberPicker.setMaxValue(10);
        numberPicker.setMinValue(1);
        sec.setMaxValue(59);
        sec.setMinValue(0);
        sec.setValue(5);
        min.setMaxValue(59);
        min.setMinValue(0);
        min.setValue(0);
        btn = binding.addCombination;
        combo_name = binding.comboName;
        rc = binding.kombinacion;
        combo_name.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        adapter = new RecyclerViewAdapterForCombos(sharedDialogViewModel.getNameData().getValue(), getContext());
        btn.setOnClickListener(this::add);
        ItemTouchHelper.Callback callback = new MyItemTouchHelperForCombo(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        adapter.setmTouchHelper(itemTouchHelper);
        addit.setOnClickListener(this::finish);
        itemTouchHelper.attachToRecyclerView(rc);
        rc.setAdapter(adapter);
        final GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        sec.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                dig.prim.setSec(newVal);
            }
        });
        min.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                dig.prim.setMin(newVal);
            }
        });
        sharedDialogViewModel.getNameData().observe(getViewLifecycleOwner(), new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> strings) {
                adapter.notifyDataSetChanged();
            }
        });
        sharedDialogViewModel.getTimerData().observe(getViewLifecycleOwner(), new Observer<PrimitiveTimer>() {
            @Override
            public void onChanged(PrimitiveTimer primitiveTimer) {
                min.setValue(primitiveTimer.getMin());
                sec.setValue(primitiveTimer.getSec());
            }
        });
        rc.setLayoutManager(layoutManager);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
    }

    public void add(View v) {

        if (adapter.getItemCount() >= 20) {
            Toast.makeText(getContext(), "Maximum number of moves are 20!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!combo_name.getText().toString().contains("|")) {
            if (combo_name.getText().length() > 0) {
                sharedDialogViewModel.getNameData().getValue().add(combo_name.getText().toString());
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getContext(), "Please enter valid move name", Toast.LENGTH_LONG).show();

            }
        } else {
            Toast.makeText(getContext(), "Please avoid using character | in your combinations ", Toast.LENGTH_LONG).show();
        }
        combo_name.setText(null);
    }

    public void finish(View v) {
        total = TimeUnit.MINUTES.toMillis(min.getValue()) + TimeUnit.SECONDS.toMillis(sec.getValue());

        if (sharedDialogViewModel.getNameData().getValue().size() > 0 & total > 0) {
            export = sharedDialogViewModel.getNameData().getValue().get(0);
            for (int i = 1; i < sharedDialogViewModel.getNameData().getValue().size(); i++) {

                export = export + " | " + sharedDialogViewModel.getNameData().getValue().get(i);
            }


            Model model = new Model(export, total);
            dg = DialogComboRepository.getInstance();
            if (numberPicker.getValue() <= 1) {
                dg.dataSet.add(model);
            } else {
                for (int i = 0; i < numberPicker.getValue(); i++) {
                    dg.dataSet.add(model);
                }
            }

            dig.clear();
            NavHostFragment.findNavController(this).navigate(R.id.action_dialog_for_combo_to_timer_Sa_Kombinacijama);
        } else {
            Toast.makeText(getContext(), "Please enter atleast one combination and valid time before adding it to list", Toast.LENGTH_LONG).show();
        }
    }
}