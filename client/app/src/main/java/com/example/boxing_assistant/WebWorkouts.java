package com.example.boxing_assistant;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.boxing_assistant.API.APIClient;
import com.example.boxing_assistant.API.APIInterface;
import com.example.boxing_assistant.API.GetWorkoutByName;
import com.example.boxing_assistant.Adapters.RecyclerAdapterForWorkout;
import com.example.boxing_assistant.Adapters.RecyclerViewForWeb;
import com.example.boxing_assistant.DatabaseCombo.Combo;
import com.example.boxing_assistant.DatabaseCombo.ComboDatabase;
import com.example.boxing_assistant.Models.Model;
import com.example.boxing_assistant.databinding.FragmentWebWorkoutsBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WebWorkouts extends DialogFragment implements RecyclerViewForWeb.OnNoteListener, RecyclerAdapterForWorkout.OnNoteListener {
    public ArrayList<String> workoutNames;
    APIInterface apiInterface;
    FragmentWebWorkoutsBinding binding;
    View view;
    RecyclerViewForWeb recyclerViewForWeb;
    RecyclerView recyclerView;
    ComboDatabase main;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        main = ComboDatabase.getInstance(getContext());
        binding = FragmentWebWorkoutsBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        recyclerView = binding.webworkoutRecyclerview;
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<ArrayList<String>> call = apiInterface.getAllNames();
        call.enqueue(new Callback<ArrayList<String>>() {
            @Override
            public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                workoutNames = response.body();
                recyclerViewForWeb = new RecyclerViewForWeb(workoutNames, WebWorkouts.this);
                recyclerView.setAdapter(recyclerViewForWeb);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


            }

            @Override
            public void onFailure(Call<ArrayList<String>> call, Throwable t) {
                Toast.makeText(getContext(), "Please reload dialog!", Toast.LENGTH_SHORT).show();
            }
        });


        return view;

    }

    @Override
    public void onNoteClick(int position) {
        Call<GetWorkoutByName> call = apiInterface.doGetUserList(workoutNames.get(position));
        call.enqueue(new Callback<GetWorkoutByName>() {
            @Override
            public void onResponse(Call<GetWorkoutByName> call, Response<GetWorkoutByName> response) {

            }

            @Override
            public void onFailure(Call<GetWorkoutByName> call, Throwable t) {

            }
        });
        //Toast.makeText(getContext(), workoutNames.get(position), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onButtonClick(int position) {
        Call<GetWorkoutByName> call = apiInterface.doGetUserList(workoutNames.get(position));

        call.enqueue(new Callback<GetWorkoutByName>() {
            @Override
            public void onResponse(Call<GetWorkoutByName> call, Response<GetWorkoutByName> response) {
                if (main.mainDao().getAllNames().size() > 0) {
                    if (!main.mainDao().getAllNames().contains(workoutNames.get(position))) {
                        loadWorkout(workoutNames.get(position), response.body().roundNumber, response.body().roundSec, response.body().roundMin, response.body().restSec, response.body().restMin, response.body().prepareSec, response.body().prepareMin, response.body().moves, response.body().movesTime);
                    } else {
                        //Toast.makeText(getContext(), "Training plan already exist", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    loadWorkout(workoutNames.get(position), response.body().roundNumber, response.body().roundSec, response.body().roundMin, response.body().restSec, response.body().restMin, response.body().prepareSec, response.body().prepareMin, response.body().moves, response.body().movesTime);
                }
            }

            @Override
            public void onFailure(Call<GetWorkoutByName> call, Throwable t) {

            }
        });
    }

    @Override
    public void onShareClick(int position) {

    }

    @Override
    public void onRenameClick(int position) {

    }

    private void loadWorkout(String name, int roundNumber, int roundSec, int roundMin, int restSec, int restMin, int prepSec, int prepMin, ArrayList<String> moves, ArrayList<Long> movesTime) {
        Combo entity = new Combo();
        Model modelInit;
        entity.setName(name);
        entity.setRoundNumber(roundNumber);
        entity.setRoundSec(roundSec);
        entity.setRoundMin(roundMin);
        entity.setRestSec(restSec);
        entity.setRestMin(restMin);
        entity.setPrepareSec(prepSec);
        entity.setPreparetMin(prepMin);
        ArrayList<Model> model = new ArrayList<>();
        for (int i = 0; i < moves.size(); i++) {
            modelInit = new Model(moves.get(i), movesTime.get(i));
            model.add(modelInit);
        }

        entity.setMoves(model);
        main.mainDao().insert(entity);
        //Toast.makeText(getContext(), "Training plan " + name + "is successfully added", Toast.LENGTH_SHORT).show();
        NavHostFragment.findNavController(this).navigate(R.id.action_webWorkouts_to_workoutChooser);
    }


}