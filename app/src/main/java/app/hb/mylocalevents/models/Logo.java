package app.hb.mylocalevents.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Logo implements Serializable {

    @SerializedName("original")
    private Original original;
    @SerializedName("id")
    private String id;
    @SerializedName("url")
    private String url;

    public Original getOriginal() {
        return original;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

}
