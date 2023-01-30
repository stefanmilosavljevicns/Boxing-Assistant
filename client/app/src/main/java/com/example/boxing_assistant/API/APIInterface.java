package com.example.boxing_assistant.API;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;


public interface APIInterface {
    @GET("api/v1/getAllNames")
    Call<ArrayList<String>> getAllNames();

    @GET("/api/v1/getWorkout/{name}")
    Call<GetWorkoutByName> doGetUserList(@Path("name") String name);

    @POST("/api/v1/addWorkout")
    Call<GetWorkoutByName> postUserList(@Body GetWorkoutByName name);

    @GET("/api/v1/getWorkout/{name}")
    Call<GetWorkoutByNamePatch> getUserId(@Path("name") String name);

    @PUT("/api/v1/updateWorkout/{name}")
    Call<GetWorkoutByNamePatch> updateUserList(@Path("name") String nameWorkout, @Body GetWorkoutByNamePatch name);
}
