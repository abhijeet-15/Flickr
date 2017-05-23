package exercise.packagecom.flickrfetcher.newtwork.flickr;

import exercise.packagecom.flickrfetcher.Model.FlickrResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


 public interface ApiInterface {

    @GET("services/feeds/photos_public.gne")
    Call<FlickrResponse> getData(
            @Query("tags") String tags,
            @Query("per_page") Integer limit,
            @Query("format") String format,
            @Query("nojsoncallback") Integer noJsonCallBack
            );

}
