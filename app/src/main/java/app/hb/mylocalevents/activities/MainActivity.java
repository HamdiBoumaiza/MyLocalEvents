package app.hb.mylocalevents.activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import java.util.ArrayList;

import app.hb.mylocalevents.R;
import app.hb.mylocalevents.adapter.EventAdapter;
import app.hb.mylocalevents.callback.IEventClickListener;
import app.hb.mylocalevents.databinding.ActivityMainBinding;
import app.hb.mylocalevents.models.Event;
import app.hb.mylocalevents.network.EventBriteApiInterface;
import app.hb.mylocalevents.network.EventBriteClient;
import app.hb.mylocalevents.network.response.BritEventListResponse;
import app.hb.mylocalevents.util.BaseConstant;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static app.hb.mylocalevents.util.BaseConstant.CATEGORY;
import static app.hb.mylocalevents.util.BaseConstant.DATE;
import static app.hb.mylocalevents.util.BaseConstant.DISTANCE;
import static app.hb.mylocalevents.util.BaseConstant.EVENT_CATEGORY;
import static app.hb.mylocalevents.util.BaseConstant.EVENT_VENUE;
import static app.hb.mylocalevents.util.BaseConstant.LATITUDE;
import static app.hb.mylocalevents.util.BaseConstant.LONGITUDE;


public class MainActivity extends AppCompatActivity implements IEventClickListener {

    private static final String KM = "km";
    private static final String DATE_PREFIX = "T00:00:01";

    private EventAdapter eventAdapter;

    private Context mcontext;
    private ArrayList<Event> mEventList = new ArrayList<>();
    private ActivityMainBinding activityMainBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mcontext = MainActivity.this;
        setSupportActionBar(activityMainBinding.toolbar);
        activityMainBinding.toolbar.setTitle("hello");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        Bundle values = getIntent().getExtras();
        if (values != null) {
            String longitude = values.getString(LONGITUDE);
            String latitude = values.getString(LATITUDE);
            String date = values.getString(DATE);
            String range = values.getString(DISTANCE);
            String idCategory = values.getString(CATEGORY);


            initRecyclerView();
            getEventList(longitude, latitude, date, range, idCategory);
        }


    }

    private void initRecyclerView() {
        activityMainBinding.recyclerEvent.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        activityMainBinding.recyclerEvent.setHasFixedSize(false);
        eventAdapter = new EventAdapter(getApplicationContext(), mEventList,this);
        activityMainBinding.recyclerEvent.setAdapter(eventAdapter);


    }

    public void getEventList(String longitude, String latitude, String date, String range, String idCategory) {
        EventBriteApiInterface mEventBriteApiInterface = EventBriteClient.getInstance().getService();
        Call<BritEventListResponse> call;
        if (idCategory.equals("0")) {
            call = mEventBriteApiInterface.getAllEvents(
                    getString(R.string.eventbrit_token),
                    String.valueOf(latitude),
                    String.valueOf(longitude),
                    date + DATE_PREFIX,
                    range + KM,
                    DISTANCE,
                    EVENT_VENUE + "," + EVENT_CATEGORY);
        } else {
            call = mEventBriteApiInterface.getAllEvents(
                    getString(R.string.eventbrit_token),
                    String.valueOf(latitude),
                    String.valueOf(longitude),
                    date + DATE_PREFIX,
                    range + KM,
                    DISTANCE,
                    idCategory,
                    EVENT_VENUE + "," + EVENT_CATEGORY);
        }

        call.enqueue(new Callback<BritEventListResponse>() {
            @Override
            public void onResponse(final Call<BritEventListResponse> call, Response<BritEventListResponse> response) {
                activityMainBinding.progress.setVisibility(View.GONE);
                mEventList = response.body().getEvents();
                if (mEventList.size() == 0) {
                    Snackbar.make(activityMainBinding.parentLayout, mcontext.getString(R.string.no_events), Snackbar.LENGTH_LONG).show();
                } else {

                    eventAdapter.AddEvents(mEventList);
                    //activityMainBinding.recyclerEvent.setAdapter(eventAdapter);
                }
            }

            @Override
            public void onFailure(Call<BritEventListResponse> call, Throwable t) {
                Timber.e("onFailure  ::\n" + t.getMessage());
                activityMainBinding.progress.setVisibility(View.GONE);
            }
        });
    }


    @Override
    public void onItemClicked(int position) {
        Intent i = new Intent(mcontext, DetailsEventActivity.class);
        i.putExtra(BaseConstant.EVENT_OBJ, mEventList.get(position));
        startActivity(i);
    }
}
