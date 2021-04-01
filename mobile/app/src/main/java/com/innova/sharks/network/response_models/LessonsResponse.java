package com.innova.sharks.network.response_models;

import com.google.gson.annotations.SerializedName;
import com.innova.sharks.models.Lesson;

import java.util.ArrayList;

public class LessonsResponse {
    @SerializedName("results")
    private ArrayList<Lesson> results;

    public ArrayList<Lesson> getResults() {
        return results;
    }
}
