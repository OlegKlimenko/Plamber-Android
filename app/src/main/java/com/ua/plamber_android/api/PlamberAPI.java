package com.ua.plamber_android.api;

import com.ua.plamber_android.model.Book;
import com.ua.plamber_android.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface PlamberAPI {

    String ENDPOINT = "http://ec2-52-201-246-230.compute-1.amazonaws.com/";

    @Headers("Content-Type: application/json")
    @POST("api/v1/user-login/")
    Call<User.UserRespond> login(@Body User.UserRequest userRequest);

    @Headers("Content-Type: application/json")
    @POST("/api/v1/home/")
    Call<Book.BookRespond> getUserBook(@Body Book.BookRequest bookRequest);

}
