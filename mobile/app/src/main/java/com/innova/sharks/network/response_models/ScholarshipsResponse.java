package com.innova.sharks.network.response_models;

import com.google.gson.annotations.SerializedName;
import com.innova.sharks.models.Scholarship;

import java.util.ArrayList;

public class ScholarshipsResponse {
    @SerializedName("results")
    private ArrayList<Scholarship> results;

    public ArrayList<Scholarship> getResults() {
        return results;
    }
}
