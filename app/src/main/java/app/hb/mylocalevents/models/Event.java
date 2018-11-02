package app.hb.mylocalevents.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Event implements Serializable {

    @SerializedName("name")
    private NameDescription name;
    @SerializedName("description")
    private NameDescription description;
    @SerializedName("url")
    private String url;
    @SerializedName("logo")
    private Logo logo;
    @SerializedName("is_free")
    private boolean isFree;
    @SerializedName("id")
    private String id;
    @SerializedName("venue")
    private Venue venue;
    @SerializedName("venue_id")
    private String venue_id;
    @SerializedName("category")
    private Category category;


    public NameDescription getName() {
        return name;
    }

    public NameDescription getDescription() {
        return description;
    }

    public Logo getLogo() {
        return logo;
    }

    public boolean isFree() {
        return isFree;
    }

    public String getId() {
        return id;
    }

    public Venue getVenue() {
        return venue;
    }

    public String getUrl() {
        return url;
    }

    public Category getCategory() {
        return category;
    }
}
