package com.innova.sharks.network.response_models;

import com.google.gson.annotations.SerializedName;
import com.innova.sharks.models.Teacher;

import java.util.ArrayList;

public class TeachersResponse {
    @SerializedName("results")
    private ArrayList<Teacher> results;

    public ArrayList<Teacher> getResults() {
        return results;
    }
}
