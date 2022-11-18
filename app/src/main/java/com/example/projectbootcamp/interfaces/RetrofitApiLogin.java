package com.example.projectbootcamp.interfaces;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RetrofitApiLogin {
    @FormUrlEncoded
    @POST("login.php")

//a method to post our data.
    Call<String> STRING_CALL(
            @Field("email") String email,
            @Field("password") String password
    );

    Call<String> REGISTER_CALL(
      @Field("nama") String nama,
      @Field("email") String email,
      @Field("password") String password
    );
}
