package app.hb.mylocalevents.activities;

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


public class DetailsEventActivity extends AppCompatActivity implements IDetailsEventClickListener {

    private Event event;
    private ActivityDetailsEventBinding activityDetailsEventBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDetailsEventBinding = DataBindingUtil.setContentView(this, R.layout.activity_details_event);

        event = (Event) getIntent().getSerializableExtra(BaseConstant.EVENT_OBJ);
        setValues();
        activityDetailsEventBinding.setDetailsEventClickListener(this);
    }

    private void setValues() {
        if (event != null) {
            activityDetailsEventBinding.setEvent(event);
        }
    }


    @Override
    public void onMapClicked() {
        goToMaps();
    }

    @Override
    public void onReservationClicked() {
        openWebReservation();
    }

    @Override
    public void onBackClicked() {
        onBackPressed();
    }

    private void goToMaps() {
        if (event.getVenue() != null) {
            String lat = event.getVenue().getLatitude();
            String lng = event.getVenue().getLongitude();
            String namePlace = event.getVenue().getName();

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + lat + "," + lng + "(" + namePlace + ")"));
            startActivity(intent);
        } else {
            Snackbar.make(activityDetailsEventBinding.parent, getString(R.string.adresse_not_available), Snackbar.LENGTH_LONG).show();
        }
    }

    private void openWebReservation() {
        if (event.getUrl() != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(event.getUrl()));
            startActivity(intent);
        }
    }
}
