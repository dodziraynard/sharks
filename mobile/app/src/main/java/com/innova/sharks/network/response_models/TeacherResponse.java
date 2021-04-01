package com.innova.sharks.network.response_models;

import com.google.gson.annotations.SerializedName;
import com.innova.sharks.models.Teacher;

public class TeacherResponse {
    @SerializedName("teacher")
    private Teacher teacher;

    @SerializedName("error_message")
    private String errorMessage;

    @SerializedName("token")
    private String token;

    public Teacher getTeacher() {
        return teacher;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getToken() {
        return token;
    }
}
