package com.stemgon.autheply.webApi.services;

import com.stemgon.autheply.webApi.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserAPIService {
    @GET("api/v1/contacts/")
    Call<List<User>> getUser();

    @POST("api/v1/auth/users/")
//    @POST("api/v1/users/")
    Call<User> createUser(@Body User user);
}
