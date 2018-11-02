package app.hb.mylocalevents.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Venue implements Serializable {

    @SerializedName("address")
    private Address address;
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("latitude")
    private String latitude;
    @SerializedName("longitude")
    private String longitude;

    public Address getAddress() {
        return address;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
}
