package com.intern.myapplication.Utils;

import com.intern.myapplication.Model.User;
import com.intern.myapplication.Retrofit.IDrinkShopApi;
import com.intern.myapplication.Retrofit.RetrofitClient;

public class Common {
  public static String BASE_URl="http://192.168.43.91/DrinkShop/";
  public   static User currentuser=null;

    public static IDrinkShopApi getAPI()
    {
        return RetrofitClient.getClient(BASE_URl).create(IDrinkShopApi.class);
    }
}
