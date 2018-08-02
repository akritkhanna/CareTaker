package iot.project.caretaker.interfacing_bridge;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Akrit on 12-Mar-18.
 */

public class RetrofitClient {
    private Retrofit retrofit = null;

    public Retrofit getClient(String baseUrl){
        if(retrofit == null)
        {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
