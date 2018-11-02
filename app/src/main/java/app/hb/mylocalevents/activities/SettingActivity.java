package app.hb.mylocalevents.activities;

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
import android.widget.DatePicker;
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
import app.hb.mylocalevents.network.EventBriteApiInterface;
import app.hb.mylocalevents.network.EventBriteClient;
import app.hb.mylocalevents.network.response.CategoriesListResponse;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static app.hb.mylocalevents.util.BaseConstant.CATEGORY;
import static app.hb.mylocalevents.util.BaseConstant.DATE;
import static app.hb.mylocalevents.util.BaseConstant.DISTANCE;
import static app.hb.mylocalevents.util.BaseConstant.LATITUDE;
import static app.hb.mylocalevents.util.BaseConstant.LONGITUDE;

public class SettingActivity extends AppCompatActivity {


    private ActivitySettingBinding activitySettingBinding;
    private int valueRange = 100;
    private Location mCurrentLocation;
    private Context mContext;
    private DatePickerDialog mDatePickerDialog;
    private Calendar mDateCalendar;
    private Boolean isLocationNull = true;
    private String idCategory = "0";

    private ArrayList<Category> mListCategories = new ArrayList<>();


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
        initEvents();
    }

    public void initEvents() {
        getCurrentPosition();
        initCalendar();
        initSeekBar();
        getListCategories();
    }


    public void getListCategories() {
        EventBriteApiInterface mEventBriteApiInterface = EventBriteClient.getInstance().getService();
        Call<CategoriesListResponse> call = mEventBriteApiInterface.getCategories(
                getString(R.string.eventbrit_token));

        call.enqueue(new Callback<CategoriesListResponse>() {
            @Override
            public void onResponse(final Call<CategoriesListResponse> call, Response<CategoriesListResponse> response) {
                mListCategories = response.body().getEvents();
                if (mListCategories != null) {
                    ArrayList<Integer> mIntCategory = new ArrayList<>();
                    ArrayList<String> mShortCategory = new ArrayList<>();
                    mShortCategory.add("All Categories");
                    mIntCategory.add(0);

                    for (int i = 0; i < mListCategories.size(); i++) {
                        mShortCategory.add(mListCategories.get(i).getShort_name());
                        mIntCategory.add(Integer.parseInt(mListCategories.get(i).getId()));
                    }

                    ArrayAdapter<String> SubCategoryArray = new ArrayAdapter<String>(mContext, R.layout.item_spinner, mShortCategory);
                    SubCategoryArray.setDropDownViewResource(R.layout.item_spinner);
                    activitySettingBinding.niceSpinner.setAdapter(SubCategoryArray);

                    activitySettingBinding.niceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            idCategory = mIntCategory.get(position).toString();
                        }

                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<CategoriesListResponse> call, Throwable t) {
                Timber.e("onFailure  ::\n" + t.getMessage());
            }
        });
    }


    private void initCalendar() {
        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        activitySettingBinding.btnCalendar.setText(currentDate);

        // init date picker
        DatePickerDialog.OnDateSetListener mDatePickerDialogListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mDateCalendar.set(Calendar.YEAR, year);
                mDateCalendar.set(Calendar.MONTH, month);
                mDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = getString(R.string.format_date);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(myFormat, Locale.getDefault());
                activitySettingBinding.btnCalendar.setText(simpleDateFormat.format(mDateCalendar.getTime()));

            }
        };

        mDateCalendar = Calendar.getInstance();
        mDatePickerDialog = new DatePickerDialog(this, R.style.DatePickerTheme, mDatePickerDialogListener, mDateCalendar.get(Calendar.YEAR),
                mDateCalendar.get(Calendar.MONTH), mDateCalendar.get(Calendar.DAY_OF_MONTH));
        mDatePickerDialog.getDatePicker().setMinDate(mDateCalendar.getTimeInMillis());
        mDatePickerDialog.setCancelable(false);
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

    @SuppressLint("CheckResult")
    public void getCurrentPosition() {
        int permissionLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
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

                    Geocoder geocoder = new Geocoder(this, Locale.getDefault());
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

    public void chooseDate(View view) {
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

    // alert dialog for opening gps
    private void OpenGPSRequestDialog() {
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


}
