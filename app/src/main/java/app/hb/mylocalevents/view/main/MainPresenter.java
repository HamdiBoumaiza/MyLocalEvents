package app.hb.mylocalevents.view.main;

import app.hb.mylocalevents.network.EventBriteApiInterface;
import app.hb.mylocalevents.network.EventBriteClient;
import app.hb.mylocalevents.network.response.BritEventListResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static app.hb.mylocalevents.util.BaseConstant.DISTANCE;
import static app.hb.mylocalevents.util.BaseConstant.EVENT_CATEGORY;
import static app.hb.mylocalevents.util.BaseConstant.EVENT_VENUE;

public class MainPresenter implements MainContract.IMainPresenter {

    private static final String KM = "km";
    private static final String DATE_PREFIX = "T00:00:01";

    private MainContract.IMainView mainView;

    public MainPresenter(MainContract.IMainView mainView) {
        this.mainView = mainView;
    }

    @Override
    public void getEvents(String token, String longitude, String latitude, String date, String range, String idCategory) {

        EventBriteApiInterface mEventBriteApiInterface = EventBriteClient.getInstance().getService();
        Call<BritEventListResponse> call;
        if (idCategory.equals("0")) {
            call = mEventBriteApiInterface.getAllEvents(
                    token,
                    String.valueOf(latitude),
                    String.valueOf(longitude),
                    date + DATE_PREFIX,
                    range + KM,
                    DISTANCE,
                    EVENT_VENUE + "," + EVENT_CATEGORY);
        } else {
            call = mEventBriteApiInterface.getAllEvents(
                    token,
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
                mainView.hideProgressBar();
                mainView.showEvents(response.body().getEvents());
            }

            @Override
            public void onFailure(Call<BritEventListResponse> call, Throwable t) {
                Timber.e("onFailure  ::\n" + t.getMessage());
                mainView.hideProgressBar();
            }
        });


    }
}
