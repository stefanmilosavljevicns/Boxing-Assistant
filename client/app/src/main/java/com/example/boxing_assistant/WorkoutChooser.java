package com.example.boxing_assistant;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.boxing_assistant.API.APIClient;
import com.example.boxing_assistant.API.APIInterface;
import com.example.boxing_assistant.API.GetWorkoutByName;
import com.example.boxing_assistant.API.GetWorkoutByNamePatch;
import com.example.boxing_assistant.Adapters.RecyclerAdapterForWorkout;
import com.example.boxing_assistant.DatabaseCombo.Combo;
import com.example.boxing_assistant.DatabaseCombo.ComboDatabase;
import com.example.boxing_assistant.ViewModel.DialogComboRepository;
import com.example.boxing_assistant.databinding.FragmentWorkoutChooserBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import com.example.boxing_assistant.database.Plans;


public class WorkoutChooser extends Fragment implements RecyclerAdapterForWorkout.OnNoteListener {

    public ComboDatabase main;
    FloatingActionButton fab;
    RecyclerAdapterForWorkout recyclerAdapter_forWorkout;
    Combo combo;
    View view;
    APIInterface apiInterface;
    List<Combo> plansArrayList;
    Button newplan;
    Button load;
    DialogComboRepository dcr;
    int id;
    FragmentWorkoutChooserBinding binding;
    private RecyclerView recyclerview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentWorkoutChooserBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        load = binding.load;
        fab = binding.workoutPresetBack;
        newplan = binding.createnew;
        recyclerview = binding.workoutchooseRecyclerview;
        apiInterface = APIClient.getClient().create(APIInterface.class);
        dcr = DialogComboRepository.getInstance();
        main = ComboDatabase.getInstance(getContext());
        plansArrayList = main.mainDao().getAllUsers();

        recyclerAdapter_forWorkout = new RecyclerAdapterForWorkout(plansArrayList, this);
        recyclerview.setAdapter(recyclerAdapter_forWorkout);
        newplan.setOnClickListener(this::onClick);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(view).navigate(R.id.action_workoutChooser_to_timer_Sa_Kombinacijama);
            }
        };
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_workoutChooser_to_webWorkouts);
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_workoutChooser_to_timer_Sa_Kombinacijama);
            }
        });
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    void remove() {
        dcr.dataSet.clear();
        dcr.titleSet = null;
        dcr.databSet = null;
        dcr.modTime.setPrepareSec(0);
        dcr.modTime.setPrepareMin(0);
        dcr.modTime.setNumberOfRounds(1);
        dcr.modTime.setRestSec(0);
        dcr.modTime.setRestMin(0);
        dcr.modTime.setRoundSec(0);
        dcr.modTime.setRoundMin(0);
        NavHostFragment.findNavController(this).navigate(R.id.action_workoutChooser_to_timer_Sa_Kombinacijama);
    }

    public void onClick(View view) {

        remove();
    }

    @Override
    public void onNoteClick(int position) {
        dcr.dataSet.clear();
        dcr.databSet = plansArrayList.get(position);
        dcr.titleSet = plansArrayList.get(position).getName();
        dcr.dataSet.addAll(plansArrayList.get(position).getMoves());
        dcr.modTime.setNumberOfRounds(plansArrayList.get(position).getRoundNumber());
        dcr.modTime.setRoundMin(plansArrayList.get(position).getRoundMin());
        dcr.modTime.setRoundSec(plansArrayList.get(position).getRoundSec());
        dcr.modTime.setRestMin(plansArrayList.get(position).getRestMin());
        dcr.modTime.setRestSec(plansArrayList.get(position).getRestSec());
        dcr.modTime.setPrepareMin(plansArrayList.get(position).getPreparetMin());
        dcr.modTime.setPrepareSec(plansArrayList.get(position).getPrepareSec());
        dcr.databSet.setID(plansArrayList.get(position).getID());
        Navigation.findNavController(view).navigate(R.id.action_workoutChooser_to_timer_Sa_Kombinacijama);
    }

    @Override
    public void onButtonClick(int position) {
        id = main.mainDao().getAllUsers().get(position).getID();
        WorkoutChooserDirections.ActionWorkoutChooserToDeleteconfirm tm = WorkoutChooserDirections.actionWorkoutChooserToDeleteconfirm(id);
        tm.setID(id);
        NavHostFragment.findNavController(this).navigate(tm);
    }

    @Override
    public void onShareClick(int position) {
        combo = plansArrayList.get(position);
        ArrayList<String> movesList = new ArrayList<>();
        ArrayList<Long> movesTime = new ArrayList<>();
        for (int i = 0; i < combo.getMoves().size(); i++){
            movesList.add(combo.getMoves().get(i).getTitle());
            movesTime.add(combo.getMoves().get(i).getTime());
        }
        GetWorkoutByName getWorkoutByName = new GetWorkoutByName(combo.getName(),movesList,movesTime,combo.getRoundNumber(),combo.getRoundMin(),combo.getRoundSec(),combo.getRestMin(),combo.getRestSec(),combo.getPreparetMin(),combo.getPrepareSec());
        Call<ArrayList<String>> call = apiInterface.getAllNames();
        call.enqueue(new Callback<ArrayList<String>>() {
            @Override
            public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                if(response.body().contains(combo.getName())){
                    //put
                    Log.d("ACTION","PUT");

                    Call<GetWorkoutByNamePatch> getUserId = apiInterface.getUserId(combo.getName());
                    getUserId.enqueue(new Callback<GetWorkoutByNamePatch>() {
                        @Override
                        public void onResponse(Call<GetWorkoutByNamePatch> call, Response<GetWorkoutByNamePatch> response) {
                            GetWorkoutByNamePatch patch = new GetWorkoutByNamePatch(response.body().getId(),combo.getName(),getWorkoutByName.getMoves(),getWorkoutByName.getMovesTime(),getWorkoutByName.getRoundNumber(),getWorkoutByName.getRoundMin(),getWorkoutByName.getRoundSec(),getWorkoutByName.getRestMin(),getWorkoutByName.getRestSec(),getWorkoutByName.getPrepareMin(),getWorkoutByName.getPrepareSec());
                            Call<GetWorkoutByNamePatch> patchCall = apiInterface.updateUserList(combo.getName(),patch);
                            patchCall.enqueue(new Callback<GetWorkoutByNamePatch>() {
                                @Override
                                public void onResponse(Call<GetWorkoutByNamePatch> call, Response<GetWorkoutByNamePatch> response) {
                                    Toast.makeText(getContext(), "Plan plan successfully updated.", Toast.LENGTH_SHORT).show();

                                }

                                @Override
                                public void onFailure(Call<GetWorkoutByNamePatch> call, Throwable t) {
                                    Toast.makeText(getContext(), "Please reload page or try again later.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onFailure(Call<GetWorkoutByNamePatch> call, Throwable t) {
                            Toast.makeText(getContext(), "Please reload page or try again later.", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                else{
                    //post

                    Log.d("ACTION","POST");
                    Call<GetWorkoutByName> callPost = apiInterface.postUserList(getWorkoutByName);
                    callPost.enqueue(new Callback<GetWorkoutByName>() {
                        @Override
                        public void onResponse(Call<GetWorkoutByName> call, Response<GetWorkoutByName> response) {

                            Toast.makeText(getContext(), "Workout plan successfully uploaded!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<GetWorkoutByName> call, Throwable t) {
                            Toast.makeText(getContext(), "Please reload page or try again later.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<ArrayList<String>> call, Throwable t) {
                Toast.makeText(getContext(), "Please reload page or try again later.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRenameClick(int position) {
        int id;
        id = main.mainDao().getAllUsers().get(position).getID();
        WorkoutChooserDirections.ActionWorkoutChooserToRenameWorkout fm = WorkoutChooserDirections.actionWorkoutChooserToRenameWorkout();
        fm.setId(id);
        fm.setIsWorkout(true);
        NavHostFragment.findNavController(this).navigate(fm);
    }
}