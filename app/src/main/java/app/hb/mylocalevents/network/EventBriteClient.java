package app.hb.mylocalevents.network;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import app.hb.mylocalevents.BuildConfig;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class EventBriteClient {
    private static final String API_URL = "https://www.eventbriteapi.com/v3/";

    private static EventBriteClient instance = null;
    private Retrofit mRetrofitClient;
    private OkHttpClient mOkHttpClient;

    private EventBriteApiInterface mService;

    private EventBriteClient() {

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            okHttpBuilder.addInterceptor(loggingInterceptor);
        }

        mOkHttpClient = okHttpBuilder.build();
        Gson gson = new GsonBuilder().setLenient().create();

        mRetrofitClient = new Retrofit.Builder().baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(mOkHttpClient)
                .build();

        mService = mRetrofitClient.create(EventBriteApiInterface.class);
    }

    public static EventBriteClient getInstance() {
        if (instance == null) {
            instance = new EventBriteClient();
        }

        return instance;
    }

    public EventBriteApiInterface getService() {
        return mService;
    }
}
