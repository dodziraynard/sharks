package com.innova.sharks.network.response_models;

import com.google.gson.annotations.SerializedName;
import com.innova.sharks.models.Book;

import java.util.ArrayList;

public class BooksResponse {
    @SerializedName("results")
    private ArrayList<Book> results;

    public ArrayList<Book> getResults() {
        return results;
    }
}
