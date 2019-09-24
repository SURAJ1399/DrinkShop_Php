package com.intern.myapplication.Retrofit;

import com.intern.myapplication.Model.Banner;
import com.intern.myapplication.Model.Category;
import com.intern.myapplication.Model.CheckUserResponse;
import com.intern.myapplication.Model.User;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface IDrinkShopApi {
    @FormUrlEncoded
    @POST("checkuser.php")
    Call<CheckUserResponse> checkUserExists(@Field("phone") String phone);
    @FormUrlEncoded
    @POST("register.php")
    Call<User> registerNewUser(@Field("phone") String phone,
                               @Field("name") String name,
                               @Field("birthdate") String birthdate,
                               @Field("address") String address);
    @FormUrlEncoded
    @POST("getuser.php")
    Call<User> getUserInformation(@Field("phone") String phone);
    @GET("getbanner.php")
    Observable<List<Banner>> getBanners();
    @GET("getmenu.php")
    Observable<List<Category>> getMenu();

}
