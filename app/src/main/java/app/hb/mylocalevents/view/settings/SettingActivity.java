package app.hb.mylocalevents.view.settings;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.github.florent37.rxgps.RxGps;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import app.hb.mylocalevents.R;
import app.hb.mylocalevents.databinding.ActivitySettingBinding;
import app.hb.mylocalevents.models.Category;
import app.hb.mylocalevents.view.main.MainActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import static app.hb.mylocalevents.util.BaseConstant.CATEGORY;
import static app.hb.mylocalevents.util.BaseConstant.DATE;
import static app.hb.mylocalevents.util.BaseConstant.DISTANCE;
import static app.hb.mylocalevents.util.BaseConstant.LATITUDE;
import static app.hb.mylocalevents.util.BaseConstant.LONGITUDE;

public class SettingActivity extends AppCompatActivity implements SettingContract.ISettingView {


    private ActivitySettingBinding activitySettingBinding;
    private int valueRange = 100;
    private Location mCurrentLocation;
    private Context mContext;
    private Boolean isLocationNull = true;
    private String idCategory = "0";
    private DatePickerDialog mDatePickerDialog;
    private Calendar mDateCalendar;

    private SettingPresenter settingPresenter;


    public static boolean isLocationEnabled(Context context) {
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return !manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySettingBinding = DataBindingUtil.setContentView(this, R.layout.activity_setting);
        activitySettingBinding.setSettingsActivity(this);
        mContext = getApplicationContext();

        settingPresenter = new SettingPresenter(this);
        initEvents();


    }


    public void initEvents() {
        settingPresenter.getCategories(mContext.getString(R.string.eventbrit_token));
        setCalendar(mContext);
        getPosition();
        initSeekBar();
    }

    public void setCalendar(Context mContext) {
        String currentDate = new SimpleDateFormat(mContext.getString(R.string.format_date)).format(new Date());
        setDate(currentDate);

        // init date picker
        DatePickerDialog.OnDateSetListener mDatePickerDialogListener = (view, year, month, dayOfMonth) -> {
            mDateCalendar.set(Calendar.YEAR, year);
            mDateCalendar.set(Calendar.MONTH, month);
            mDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = mContext.getString(R.string.format_date);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(myFormat, Locale.getDefault());
            setDate(simpleDateFormat.format(mDateCalendar.getTime()));

        };
        mDateCalendar = Calendar.getInstance();
        mDatePickerDialog = new DatePickerDialog(mContext, R.style.DatePickerTheme, mDatePickerDialogListener, mDateCalendar.get(Calendar.YEAR),
                mDateCalendar.get(Calendar.MONTH), mDateCalendar.get(Calendar.DAY_OF_MONTH));
        mDatePickerDialog.getDatePicker().setMinDate(mDateCalendar.getTimeInMillis());
        mDatePickerDialog.setCancelable(false);
    }


    @SuppressLint("CheckResult")
    public void getPosition() {
        int permissionLocation = ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
            if (isLocationEnabled(mContext)) {
                OpenGPSRequestDialog();
            }
        }

        new RxGps(this).locationLowPower()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(location -> {
                    isLocationNull = false;
                    mCurrentLocation = location;

                    Timber.e("location found");

                    Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
                    List<Address> addresses = geocoder.getFromLocation(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), 1);
                    String addressLineName = addresses.get(0).getAddressLine(0);
                    String countryName = addresses.get(0).getCountryName();
                    String localityName = addresses.get(0).getLocality();

                    Timber.e("addressLineName:: " + addressLineName);

                    activitySettingBinding.currentLocation.setVisibility(View.VISIBLE);
                    activitySettingBinding.currentLocation.setText(mContext.getString(R.string.location_text, localityName, countryName));


                }, throwable -> {
                    if (throwable instanceof RxGps.PermissionException) {
                        isLocationNull = true;
                        Timber.e("the user does not allow the permission");
                    } else if (throwable instanceof RxGps.PlayServicesNotAvailableException) {
                        isLocationNull = true;
                        Timber.e("the user do not have play services");
                    }
                });


    }

    private void initSeekBar() {
        activitySettingBinding.tvRange.setText(mContext.getString(R.string.search_range, "100"));

        activitySettingBinding.discreteSeekbarRange.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                valueRange = value;
                activitySettingBinding.tvRange.setText(mContext.getString(R.string.search_range, String.valueOf(valueRange)));
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {
            }
        });
    }


    public void chooseDate(View view) {
        if (mDatePickerDialog != null)
            mDatePickerDialog.show();
    }

    public void getEvents(View view) {
        if (!isLocationNull) {
            Intent intent = new Intent(mContext, MainActivity.class);
            intent.putExtra(LATITUDE, String.valueOf(mCurrentLocation.getLatitude()));
            intent.putExtra(LONGITUDE, String.valueOf(mCurrentLocation.getLongitude()));
            intent.putExtra(DATE, String.valueOf(activitySettingBinding.btnCalendar.getText().toString()));
            intent.putExtra(DISTANCE, String.valueOf(valueRange));
            intent.putExtra(CATEGORY, idCategory);
            startActivity(intent);
        } else if (isLocationEnabled(mContext)) {
            Toast.makeText(this, mContext.getString(R.string.open_gps), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, mContext.getString(R.string.get_location), Toast.LENGTH_SHORT).show();
        }
    }

    public void OpenGPSRequestDialog() {
        AlertDialog.Builder altdial = new AlertDialog.Builder(this);
        altdial.setMessage(R.string.open_gps).setCancelable(false)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    if (isLocationEnabled(mContext)) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    } else {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.no, (dialog, which) -> dialog.cancel());

        AlertDialog alert = altdial.create();
        alert.setTitle(mContext.getString(R.string.gps));
        alert.show();
    }


    @Override
    public void showCategoryError(String error) {
        Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void displayCategories(ArrayList<Category> categories) {
        if (categories != null) {
            ArrayList<Integer> mIntCategory = new ArrayList<>();
            ArrayList<String> mShortCategory = new ArrayList<>();
            mShortCategory.add("All Categories");
            mIntCategory.add(0);

            for (int i = 0; i < categories.size(); i++) {
                mShortCategory.add(categories.get(i).getShort_name());
                mIntCategory.add(Integer.parseInt(categories.get(i).getId()));
            }

            ArrayAdapter<String> CategoryArray = new ArrayAdapter<String>(mContext, R.layout.item_spinner, mShortCategory);
            CategoryArray.setDropDownViewResource(R.layout.item_spinner);
            activitySettingBinding.niceSpinner.setAdapter(CategoryArray);

            activitySettingBinding.niceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    idCategory = mIntCategory.get(position).toString();
                }

                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }
    }

    public void setDate(String date) {
        activitySettingBinding.btnCalendar.setText(date);

    }
}
