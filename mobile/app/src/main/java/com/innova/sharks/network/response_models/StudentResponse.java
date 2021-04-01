package com.innova.sharks.network.response_models;

import com.google.gson.annotations.SerializedName;
import com.innova.sharks.models.Student;

public class StudentResponse {
    @SerializedName("student")
    private Student student;

    @SerializedName("error_message")
    private String errorMessage;

    @SerializedName("token")
    private String token;

    public Student getStudent() {
        return student;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getToken() {
        return token;
    }
}
