package com.mtz.testwarna.network;

import android.os.Build;
import android.support.annotation.RequiresApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    private static Retrofit retrofit;
    private static final String BASE_URL = "http://freeg.thekingcorp.org/freeg_api/public/api/";

    /**
     * Create an instance of Retrofit object
     * */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
