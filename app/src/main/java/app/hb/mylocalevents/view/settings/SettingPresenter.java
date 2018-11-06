package app.hb.mylocalevents.view.settings;

import app.hb.mylocalevents.network.EventBriteApiInterface;
import app.hb.mylocalevents.network.EventBriteClient;
import app.hb.mylocalevents.network.response.CategoriesListResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class SettingPresenter implements SettingContract.ISettingPresenter {

    private SettingContract.ISettingView settingView;


    public SettingPresenter(SettingContract.ISettingView settingView) {
        this.settingView = settingView;
    }

    @Override
    public void getCategories(String token) {

        EventBriteApiInterface mEventBriteApiInterface = EventBriteClient.getInstance().getService();
        Call<CategoriesListResponse> call = mEventBriteApiInterface.getCategories(token);

        call.enqueue(new Callback<CategoriesListResponse>() {
            @Override
            public void onResponse(final Call<CategoriesListResponse> call, Response<CategoriesListResponse> response) {
                if (response.body() != null)
                    settingView.displayCategories(response.body().getEvents());
            }

            @Override
            public void onFailure(Call<CategoriesListResponse> call, Throwable t) {
                Timber.e("onFailure  ::\n" + t.getMessage());
                settingView.showCategoryError("onFailure  ::\n" + t.getMessage());
            }
        });

    }


}
