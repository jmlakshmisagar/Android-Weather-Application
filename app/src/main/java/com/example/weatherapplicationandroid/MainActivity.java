package com.example.weatherapplicationandroid;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private EditText cityEditText;
    private Button checkButton;
    private TextView cityNameTextView;
    private ImageView weatherIconImageView;
    private TextView temperatureTextView;
    private TextView weatherDescriptionTextView;
    private TextView humidityTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityEditText = findViewById(R.id.cityEditText);
        checkButton = findViewById(R.id.checkButton);
        cityNameTextView = findViewById(R.id.cityNameTextView);
        weatherIconImageView = findViewById(R.id.weatherIconImageView);
        temperatureTextView = findViewById(R.id.temperatureTextView);
        weatherDescriptionTextView = findViewById(R.id.weatherDescriptionTextView);
        humidityTextView = findViewById(R.id.humidityTextView);

        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cityName = cityEditText.getText().toString();
                getWeatherData(cityName);
            }
        });
    }

    private void getWeatherData(String cityName) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<WeatherResponse> call = apiService.getWeather(cityName, "metric", "8925502a781648f4443f9e01d96c7ff5");

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful()) {
                    WeatherResponse weatherResponse = response.body();
                    cityNameTextView.setText(weatherResponse.getName());
                    temperatureTextView.setText(String.format("%s°C", weatherResponse.getMain().getTemp()));
                    weatherDescriptionTextView.setText(weatherResponse.getWeather().get(0).getDescription());
                    humidityTextView.setText(String.format("Humidity: %s%%", weatherResponse.getMain().getHumidity()));

                    // Set weather icon based on condition
                    String weatherCondition = weatherResponse.getWeather().get(0).getDescription().toLowerCase();
                    switch (weatherCondition) {
                        case "clear sky":
                            weatherIconImageView.setImageResource(R.drawable.clear);
                            break;
                        case "few clouds":
                        case "scattered clouds":
                        case "broken clouds":
                        case "overcast clouds":
                            weatherIconImageView.setImageResource(R.drawable.clouds);
                            break;
                        case "shower rain":
                        case "rain":
                            weatherIconImageView.setImageResource(R.drawable.rain);
                            break;
                        case "thunderstorm":
                            weatherIconImageView.setImageResource(R.drawable.drizzle);
                            break;
                        case "snow":
                            weatherIconImageView.setImageResource(R.drawable.snow);
                            break;
                        case "mist":
                        case "fog":
                            weatherIconImageView.setImageResource(R.drawable.mist);
                            break;
                        default:
                            weatherIconImageView.setImageResource(R.drawable.weather);
                            break;
                    }
                } else {
                    cityNameTextView.setText("City not found");
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                cityNameTextView.setText("Error: " + t.getMessage());
            }
        });
    }
}
