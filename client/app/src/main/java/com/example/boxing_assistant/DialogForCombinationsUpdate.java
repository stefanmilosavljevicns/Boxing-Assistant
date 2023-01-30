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
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.boxing_assistant.Adapters.MyItemTouchHelperForCombo;
import com.example.boxing_assistant.Adapters.RecyclerViewAdapterForCombos;
import com.example.boxing_assistant.Models.Model;
import com.example.boxing_assistant.ViewModel.DialogComboRepository;
import com.example.boxing_assistant.ViewModel.DialogComboViewModel;
import com.example.boxing_assistant.databinding.FragmentDialogForCombinationsUpdateBinding;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class DialogForCombinationsUpdate extends DialogFragment {
    View view;
    RecyclerView rc;
    Button btn, addit;
    NumberPicker sec, min;
    private DialogComboViewModel sharedViewModel;
    EditText combo_name;
    long total;
    DialogComboRepository dg;

    String export, editor;
    int index;

    ArrayList<String> List_of_moves = new ArrayList<>();
    RecyclerViewAdapterForCombos adapter;
    FragmentDialogForCombinationsUpdateBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentDialogForCombinationsUpdateBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        sharedViewModel = ViewModelProviders.of(requireActivity()).get(DialogComboViewModel.class);


        index = DialogForCombinationsUpdateArgs.fromBundle(getArguments()).getIndex();
        addit = binding.addToList;
        sec = binding.secComboDialog;
        min = binding.minComboDialog;

        sec.setMaxValue(59);
        sec.setMinValue(0);

        min.setMaxValue(59);
        min.setMinValue(0);

        init_rec_info();
        btn = binding.addCombination;
        combo_name = binding.comboName;
        rc = binding.kombinacion;
        combo_name.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        combo_name.setSingleLine(true);
        adapter = new RecyclerViewAdapterForCombos(List_of_moves, getContext());
        btn.setOnClickListener(this::add);
        ItemTouchHelper.Callback callback = new MyItemTouchHelperForCombo(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        adapter.setmTouchHelper(itemTouchHelper);
        addit.setOnClickListener(this::finish);
        itemTouchHelper.attachToRecyclerView(rc);
        rc.setAdapter(adapter);
        final GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);

        rc.setLayoutManager(layoutManager);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void init_rec_info() {
        sharedViewModel.init();
        int start_pos = 0;
        int is_true;
        int last_pos = 0;
        editor = sharedViewModel.getNameData().getValue().get(index).getTitle();
        long clock = sharedViewModel.getNameData().getValue().get(index).getTime();
        min.setValue((int) (TimeUnit.MILLISECONDS.toMinutes(clock)));
        sec.setValue((int) (TimeUnit.MILLISECONDS.toSeconds(clock) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(clock))));
        int count = editor.length() - editor.replaceAll("[|]", "").length();
        String results;

        for (int i = 0; i < editor.length(); i++) {
            if (count > 0) {
                is_true = Character.compare(editor.charAt(i), '|');
                if (is_true == 0) {
                    results = editor.substring(start_pos, i - 1);
                    start_pos = i + 2;
                    List_of_moves.add(results);
                    count--;
                    if (count == 0) {
                        last_pos = i + 2;
                    }
                }
            } else {
                break;
            }

        }
        results = editor.substring(last_pos);
        List_of_moves.add(results);


    }

    public void add(View v) {
        if (adapter.getItemCount() >= 20) {
            Toast.makeText(getContext(), "Maximum number of moves are 20!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!combo_name.getText().toString().contains("|")) {

            if (combo_name.getText().length() > 0) {

                List_of_moves.add(combo_name.getText().toString());

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

        if (List_of_moves.size() > 0 & total > 0) {
            export = List_of_moves.get(0);
            for (int i = 1; i < List_of_moves.size(); i++) {

                export = export + " | " + List_of_moves.get(i);
            }

            dg = DialogComboRepository.getInstance();
            Model model = new Model(export, total);
            dg.dataSet.set(index, model);
            NavHostFragment.findNavController(this).navigate(R.id.action_dialog_for_Combinations_Update_to_timer_Sa_Kombinacijama);
        } else {
            Toast.makeText(getContext(), "Please enter atleast one combination and valid time before adding it to list", Toast.LENGTH_LONG).show();
        }
    }
}
