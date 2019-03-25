package example.app.android.com.testapplication.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.pixplicity.easyprefs.library.Prefs;

import java.text.DecimalFormat;

import example.app.android.com.testapplication.R;
import example.app.android.com.testapplication.listeners.GetWeatherListener;
import example.app.android.com.testapplication.models.WeatherResponse;
import example.app.android.com.testapplication.networkManager.NetworkManager;

import static example.app.android.com.testapplication.utils.Constants.KEY_CHECK_LOGIN;

public class WeatherActivity extends AppCompatActivity {

    private RelativeLayout coordinatorLayout;
    private TextView tvCity;
    private TextView tvTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weater);

        Toolbar toolbar = findViewById(R.id.weather_tool_bar);
        ImageView imageView = toolbar.findViewById(R.id.imgLogOut);
        tvCity = findViewById(R.id.tv_city);
        tvTemp = findViewById(R.id.tv_temp);

        coordinatorLayout = findViewById(R.id.coordinatorLayout);

        checkTemperature();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WeatherActivity.this, MainActivity.class);
                startActivity(intent);
                Prefs.putBoolean(KEY_CHECK_LOGIN, false);
                finish();
            }
        });

    }

    private void checkTemperature() {
        NetworkManager.getInstance().getWeather(new GetWeatherListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(WeatherResponse weatherResponse) {
                double temp = (277 - Double.parseDouble(String.valueOf(weatherResponse.main.temp)));
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Country : " + weatherResponse.name
                                        + "\nTemperature : " + new DecimalFormat("##.##").format(temp) + " C" + (char) 0x00B0,
                                Snackbar.LENGTH_LONG);
                snackbar.show();

                tvCity.setText(weatherResponse.name);
                tvTemp.setText(" " + new DecimalFormat("##.##").format(temp) + "C" + (char) 0x00B0);
            }

            @Override
            public void onFailure(String error) {
                System.out.println(error);
            }
        });
    }


}
