package app.hb.mylocalevents.network;

import app.hb.mylocalevents.network.response.BritEventListResponse;
import app.hb.mylocalevents.network.response.CategoriesListResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface EventBriteApiInterface {

    @GET("events/search/")
    Call<BritEventListResponse> getAllEvents(@Query("token") String token,
                                             @Query("location.latitude") String latitude,
                                             @Query("location.longitude") String longitude,
                                             @Query("start_date.range_start") String startDate,
                                             @Query("location.within") String within,
                                             @Query("sort_by") String sortby,
                                             @Query("categories") String categories,
                                             @Query("expand") String venue_category);

    @GET("events/search/")
    Call<BritEventListResponse> getAllEvents(@Query("token") String token,
                                             @Query("location.latitude") String latitude,
                                             @Query("location.longitude") String longitude,
                                             @Query("start_date.range_start") String startDate,
                                             @Query("location.within") String within,
                                             @Query("sort_by") String sortby,
                                             @Query("expand") String venue_category);


    @GET("categories/")
    Call<CategoriesListResponse> getCategories(@Query("token") String token);


}
