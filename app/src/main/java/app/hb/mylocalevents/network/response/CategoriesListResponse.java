package app.hb.mylocalevents.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import app.hb.mylocalevents.models.Category;
import app.hb.mylocalevents.models.Event;


public class CategoriesListResponse {

    @SerializedName("categories")
    @Expose
    private ArrayList<Category> categories;

    public ArrayList<Category> getEvents() {
        return categories;
    }

}
