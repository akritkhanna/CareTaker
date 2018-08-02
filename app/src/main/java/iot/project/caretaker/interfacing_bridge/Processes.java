package iot.project.caretaker.interfacing_bridge;

import iot.project.caretaker.interfacing_bridge.model.ServerData;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Akrit on 12-Mar-18.
 */

public interface Processes {
    @GET("/cofe")
    Call<ServerData> getCoffee();

    @GET("/drn")
    Call<ServerData> getdrink();

    @GET("/mlk")
    Call<ServerData> getMilk();

    @GET("/medi")
    Call<ServerData> getMedi();
}
