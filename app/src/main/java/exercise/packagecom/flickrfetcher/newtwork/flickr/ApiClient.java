package exercise.packagecom.flickrfetcher.newtwork.flickr;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



abstract public class ApiClient {

    public static final String BASE_URL = "https://api.flickr.com/";

    public static ApiInterface apiInterface = null;

    public static ApiInterface getClient() {

        if (apiInterface == null) {

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(30, TimeUnit.SECONDS);
            builder.writeTimeout(1, TimeUnit.MINUTES);
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.interceptors().add(logging);
            OkHttpClient client = builder.build();

            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .create();

            apiInterface = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build().create(ApiInterface.class);
        }
        return apiInterface;
    }

    public static abstract class Callback<T> implements retrofit2.Callback<T> {

        @Override
        public void onResponse(Call<T> call, Response<T> response) {

            if (response.isSuccessful()) {
                sucess(response.body());
            } else {
                JSONObject jsonObject = null;

                try {
                    jsonObject = new JSONObject(response.errorBody().string());
                    sucessError(jsonObject.getJSONObject("error").getJSONArray("errors").getJSONObject(0).getString("reason"));

                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                    failure("Unknown Error");
                }
            }
        }

        @Override
        public void onFailure(Call<T> call, Throwable t) {
            Log.d("debug", "Api Fail :" + t.getMessage());
            failure(t.getMessage());
        }

        abstract public void sucess(T response);

        abstract public void failure(String error);

        abstract public void sucessError(String reason);
    }

}
