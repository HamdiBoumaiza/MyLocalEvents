package app.hb.mylocalevents.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NameDescription implements Serializable {

    @SerializedName("text")
    private String text;

    public String getText() {
        return text;
    }


}
