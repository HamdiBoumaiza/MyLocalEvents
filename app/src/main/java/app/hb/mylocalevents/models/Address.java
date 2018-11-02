package app.hb.mylocalevents.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Address implements Serializable {

    @SerializedName("address_1")
    @Expose
    private String address1;

    public String getAddress1() {
        return address1;
    }

}
