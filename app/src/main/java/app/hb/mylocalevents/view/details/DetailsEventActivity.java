package app.hb.mylocalevents.view.details;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import app.hb.mylocalevents.R;
import app.hb.mylocalevents.callback.IDetailsEventClickListener;
import app.hb.mylocalevents.databinding.ActivityDetailsEventBinding;
import app.hb.mylocalevents.models.Event;
import app.hb.mylocalevents.util.BaseConstant;


public class DetailsEventActivity extends AppCompatActivity implements IDetailsEventClickListener, DetailsContract.IDetailsView {

    private Event event;
    private ActivityDetailsEventBinding activityDetailsEventBinding;

    private Context mContext;
    private DetailsPresenter detailsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDetailsEventBinding = DataBindingUtil.setContentView(this, R.layout.activity_details_event);

        mContext = DetailsEventActivity.this;
        event = (Event) getIntent().getSerializableExtra(BaseConstant.EVENT_OBJ);
        setValues();
        activityDetailsEventBinding.setDetailsEventClickListener(this);
        detailsPresenter = new DetailsPresenter(this);


    }

    private void setValues() {
        if (event != null) {
            activityDetailsEventBinding.setEvent(event);
        }
    }


    @Override
    public void onMapClicked() {
        if (event.getVenue() != null) {
            String lat = event.getVenue().getLatitude();
            String lng = event.getVenue().getLongitude();
            String namePlace = event.getVenue().getName();
            detailsPresenter.goToMaps(namePlace, lat, lng);
        } else {
            Snackbar.make(activityDetailsEventBinding.parent, getString(R.string.adresse_not_available), Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onReservationClicked() {
        detailsPresenter.goToReservation(event.getUrl());
    }

    @Override
    public void onBackClicked() {
        onBackPressed();
    }


    @Override
    public void openWebReservation(String url) {
        if (url != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        }
    }

    @Override
    public void openMaps(String namePlace, String lat, String lng) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + lat + "," + lng + "(" + namePlace + ")"));
        startActivity(intent);
    }
}
