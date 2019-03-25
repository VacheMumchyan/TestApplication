package example.app.android.com.testapplication.listeners;

import example.app.android.com.testapplication.models.WeatherResponse;

public interface GetWeatherListener {

    void onSuccess(WeatherResponse weatherResponse);
    void onFailure(String error);

}
