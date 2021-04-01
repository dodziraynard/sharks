package com.innova.sharks.network.response_models;

import com.google.gson.annotations.SerializedName;
import com.innova.sharks.models.Quiz;

import java.util.ArrayList;

public class QuizzesResponse {
    @SerializedName("results")
    private ArrayList<Quiz> results;

    public ArrayList<Quiz> getResults() {
        return results;
    }
}
