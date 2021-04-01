package com.innova.sharks.network;


import com.innova.sharks.network.response_models.AuthenticationResponse;
import com.innova.sharks.network.response_models.BooksResponse;
import com.innova.sharks.network.response_models.CoursesResponse;
import com.innova.sharks.network.response_models.LessonsResponse;
import com.innova.sharks.network.response_models.QuizzesResponse;
import com.innova.sharks.network.response_models.ScholarshipsResponse;
import com.innova.sharks.network.response_models.TeachersResponse;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiService {
    @GET("courses")
    Call<CoursesResponse> getCourses(
            @Query("query") String query,
            @Query("offset") long offset,
            @Query("limit") long limit
    );

    @GET("courses")
    Call<CoursesResponse> getMyCourses(
            @Query("level") int level
    );

    @GET("lessons")
    Call<LessonsResponse> getLessons(
            @Query("query") String query,
            @Query("course_id") long offset
    );

    @GET("quizzes")
    Call<QuizzesResponse> getQuizzes(@Query("lesson_id") long lessonId);

    @GET("scholarships")
    Call<ScholarshipsResponse> getScholarships(@Query("query") String query);

    @GET("books")
    Call<BooksResponse> getBooks(@Query("query") String query);

    @GET("teacher_profiles")
    Call<TeachersResponse> getTeachers(@Query("query") String query);

    @FormUrlEncoded
    @POST("auth/login")
    Call<AuthenticationResponse> login(
            @Field("username") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("auth/register")
    Call<AuthenticationResponse> register(
            @Field("username") String username,
            @Field("password") String password,
            @Field("is_teacher") boolean isTeacher
    );

    @POST("auth/logout")
    Call<Void> logOut();

    @Multipart
    @POST("auth/update_profile")
    Call<ResponseBody> updateProfileImage(@Part MultipartBody.Part image);

    @POST("auth/update_profile")
    Call<ResponseBody> updateProfileInfo(@Body HashMap<String, String> body);

    @GET("auth/profile")
    Call<AuthenticationResponse> getProfile(@Query("user_id") long id);
}