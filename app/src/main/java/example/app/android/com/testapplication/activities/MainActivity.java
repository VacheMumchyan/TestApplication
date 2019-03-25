package example.app.android.com.testapplication.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.androidfung.geoip.GeoIpService;
import com.androidfung.geoip.ServicesManager;
import com.androidfung.geoip.model.GeoIpResponseModel;
import com.pixplicity.easyprefs.library.Prefs;

import example.app.android.com.testapplication.R;
import example.app.android.com.testapplication.listeners.DrawableClickListener;
import example.app.android.com.testapplication.utils.CustomEditText;
import example.app.android.com.testapplication.utils.ValidationHelper;
import retrofit2.Call;
import retrofit2.Callback;

import static example.app.android.com.testapplication.utils.Constants.KEY_CHECK_LOGIN;
import static example.app.android.com.testapplication.utils.Constants.LATITUDE;
import static example.app.android.com.testapplication.utils.Constants.LONGITUDE;


public class MainActivity extends AppCompatActivity {

    private CustomEditText etPassword;
    private Button btnSignIn;
    private EditText etEmail;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;
    private ValidationHelper validationHelper;
    private RelativeLayout relativeLyout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkCoordinates();

        /**
         * Check login
         */
        if (Prefs.getBoolean(KEY_CHECK_LOGIN, false)) {
            Intent intent = new Intent(this, WeatherActivity.class);
            startActivity(intent);
            finish();
        }

        /**
         * Initialization views
         */
        relativeLyout = findViewById(R.id.relativeLayout);
        etEmail = findViewById(R.id.edit_text_email);
        etPassword = findViewById(R.id.et_text_password);
        btnSignIn = findViewById(R.id.btnSignIn);
        textInputLayoutEmail = findViewById(R.id.text_input_layout_email);
        textInputLayoutPassword = findViewById(R.id.text_input_layout_password);


        validationHelper = new ValidationHelper(this);


        /**
         * click SignIn Button
         */
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validationHelper.isEditTextEmail(etEmail, textInputLayoutEmail) &&
                        validationHelper.isEditTextPassword(etPassword, textInputLayoutPassword)) {
                    Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    Prefs.putBoolean(KEY_CHECK_LOGIN, true);
                    finish();

                } else {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(relativeLyout.getWindowToken(), 0);
                    Snackbar snackbar = Snackbar
                            .make(relativeLyout, "Email or password incorrect",
                                    Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            }
        });


        /**
         * click ? icon edittext
         */
        etPassword.setDrawableClickListener(new DrawableClickListener() {
            @Override
            public void onClick(DrawablePosition target) {
                switch (target) {
                    case RIGHT:
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle(getResources().getString(R.string.аttention))
                                .setMessage(getResources().getString(R.string.message))

                                .setCancelable(false)
                                .setNegativeButton(getResources().getString(R.string.ok),
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                        break;
                }
            }
        });
    }

    /**
     * Check latitude and longitude  coordinates
     */
    private void checkCoordinates() {
        GeoIpService ipApiService = ServicesManager.getGeoIpService();
        ipApiService.getGeoIp().enqueue(new Callback<GeoIpResponseModel>() {
            @Override
            public void onResponse(Call<GeoIpResponseModel> call, retrofit2.Response<GeoIpResponseModel> response) {
                double latitude = response.body().getLatitude();
                double longitude = response.body().getLongitude();
                Prefs.putString(LATITUDE, String.valueOf(latitude));
                Prefs.putString(LONGITUDE, String.valueOf(longitude));
            }

            @Override
            public void onFailure(Call<GeoIpResponseModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
