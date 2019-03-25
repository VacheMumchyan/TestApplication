package example.app.android.com.testapplication.networkManager;

import com.pixplicity.easyprefs.library.Prefs;

import example.app.android.com.testapplication.api.WeatherService;
import example.app.android.com.testapplication.listeners.GetWeatherListener;
import example.app.android.com.testapplication.models.WeatherResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static example.app.android.com.testapplication.utils.Constants.LATITUDE;
import static example.app.android.com.testapplication.utils.Constants.LONGITUDE;

public class NetworkManager {

    private static NetworkManager instance;

    private static final String BASE_URL = "http://api.openweathermap.org/";
    public static String AppId = "2e65127e909e178d0af311a81f39948c";
    WeatherService api;

    private NetworkManager(WeatherService api) {
        this.api = api;
    }


    private NetworkManager() {
    }

    public static NetworkManager getInstance() {
        if (instance == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            instance = new NetworkManager(retrofit.create(WeatherService.class));
        }
        return instance;
    }


    public void getWeather(final GetWeatherListener getWeatherListener) {
        String latitude = Prefs.getString(LATITUDE, "");
        String longitude = Prefs.getString(LONGITUDE, "");

        api.getCurrentWeatherData(latitude, longitude, AppId).enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    getWeatherListener.onSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                getWeatherListener.onFailure(t.getMessage());
            }
        });
    }

}
