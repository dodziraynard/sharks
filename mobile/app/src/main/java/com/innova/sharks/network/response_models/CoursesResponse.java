package com.innova.sharks.network.response_models;

import com.google.gson.annotations.SerializedName;
import com.innova.sharks.models.Course;

import java.util.ArrayList;

public class CoursesResponse {
    private static final String TAG = "CoursesResponse";

    @SerializedName("results")
    private ArrayList<Course> results;

    public ArrayList<Course> getResults() {
        return results;
    }
}
