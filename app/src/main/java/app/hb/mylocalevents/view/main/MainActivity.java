package app.hb.mylocalevents.view.main;

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
import app.hb.mylocalevents.view.details.DetailsEventActivity;
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


public class MainActivity extends AppCompatActivity implements IEventClickListener,MainContract.IMainView {

    private static final String KM = "km";
    private static final String DATE_PREFIX = "T00:00:01";

    private EventAdapter eventAdapter;

    private Context mcontext;
    private ArrayList<Event> mEventList = new ArrayList<>();
    private ActivityMainBinding activityMainBinding;

    private MainPresenter mainPresenter ;


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

        mainPresenter = new MainPresenter(this);


        Bundle values = getIntent().getExtras();
        if (values != null) {
            String longitude = values.getString(LONGITUDE);
            String latitude = values.getString(LATITUDE);
            String date = values.getString(DATE);
            String range = values.getString(DISTANCE);
            String idCategory = values.getString(CATEGORY);

            initRecyclerView();
            mainPresenter.getEvents(mcontext.getString(R.string.eventbrit_token),longitude, latitude, date, range, idCategory);
        }


    }

    private void initRecyclerView() {
        activityMainBinding.recyclerEvent.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        activityMainBinding.recyclerEvent.setHasFixedSize(false);
        eventAdapter = new EventAdapter(getApplicationContext(), mEventList,this);
        activityMainBinding.recyclerEvent.setAdapter(eventAdapter);


    }

    @Override
    public void onItemClicked(int position) {
        Intent i = new Intent(mcontext, DetailsEventActivity.class);
        i.putExtra(BaseConstant.EVENT_OBJ, mEventList.get(position));
        startActivity(i);
    }

    @Override
    public void hideProgressBar() {
        activityMainBinding.progress.setVisibility(View.GONE);
    }

    @Override
    public void showEvents(ArrayList<Event> mEventList) {
        if (mEventList != null){
            if (mEventList.size() == 0) {
                Snackbar.make(activityMainBinding.parentLayout, mcontext.getString(R.string.no_events), Snackbar.LENGTH_LONG).show();
            } else {
                eventAdapter.AddEvents(mEventList);
            }
        }
    }
}
