package app.hb.mylocalevents.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import app.hb.mylocalevents.models.Event;


public class BritEventListResponse {

    @SerializedName("events")
    @Expose
    private ArrayList<Event> events;

    public ArrayList<Event> getEvents() {
        return events;
    }

}
