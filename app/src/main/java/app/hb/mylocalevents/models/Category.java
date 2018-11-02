package app.hb.mylocalevents.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Category implements Serializable {

    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("short_name")
    private String short_name;


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getShort_name() {
        return short_name;
    }
}
