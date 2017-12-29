package xebia.ismail.firstblood.controller;

import com.squareup.okhttp.ResponseBody;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Admin on 12/29/2017.
 */

public interface BaseApiService {

    @FormUrlEncoded
    @POST("auth/login")// ini di masukin url apa val ?
    Call<ResponseBody> loginRequest(@Field("email") String email,
                                    @Field("password") String password);

    @FormUrlEncoded
    @POST("auth/register")
    Call<ResponseBody> registerRequest(@Field("nama") String nama,
                                       @Field("email") String email,
                                       @Field("password") String password);
}
