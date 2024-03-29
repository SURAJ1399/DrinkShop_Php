package com.intern.myapplication.Retrofit;

import com.intern.myapplication.Model.User;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
  public   static   Retrofit retrofit=null;
  static User currentuser=null;
   public static Retrofit getClient(String baseUrl)
   {
       if(retrofit==null)
       {
         retrofit=new Retrofit.Builder()
         .baseUrl(baseUrl)
         .addConverterFactory(GsonConverterFactory.create())
                 .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
         .build();
       }
       return retrofit;
   }
}
