package iot.project.caretaker.interfacing_bridge.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Akrit on 30-Apr-18.
 */

public class ServerData {

    @SerializedName("coffee")
    @Expose
    private String coffee;
    @SerializedName("drink")
    @Expose
    private String drink;
    @SerializedName("meds")
    @Expose
    private String medicines;
    @SerializedName("milk")
    @Expose
    private  String milk;

    public String getCoffee() {
        return coffee;
    }

    public String getDrink() {
        return drink;
    }

    public String getMedicines() {
        return medicines;
    }

    public String getMilk() {
        return milk;
    }
}

