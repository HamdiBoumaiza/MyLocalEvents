package app.hb.mylocalevents.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Original implements Serializable {

    @SerializedName("url")
    private String url;

    public String getUrl() {
        return url;
    }

}
