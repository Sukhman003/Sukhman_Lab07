package com.example.sukhman_lab07;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    private String apiKey = "7e943c97096a9784391a981c4d878b22";
    private RequestQueue queue;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        queue = Volley.newRequestQueue(this);
        progressBar = findViewById(R.id.progressBar);

        Button buttonGetForecast = findViewById(R.id.buttonGetForecast);
        buttonGetForecast.setOnClickListener(view -> {
            String cityName = ((EditText) findViewById(R.id.editTextCity)).getText().toString();
            if (cityName.isEmpty()) {
                Toast.makeText(this, "Please enter a city name", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                cityName = URLEncoder.encode(cityName, "UTF-8");
                String url = "https://api.openweathermap.org/data/2.5/weather?q=" + cityName +
                        "&appid=" + apiKey + "&units=metric";
                fetchWeatherData(url);
            } catch (UnsupportedEncodingException e) {
                Toast.makeText(this, "Error encoding city name", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchWeatherData(String url) {
        progressBar.setVisibility(ProgressBar.VISIBLE);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        // Parsing JSON response
                        JSONObject main = response.getJSONObject("main");
                        double currentTemp = main.getDouble("temp");
                        double maxTemp = main.getDouble("temp_max");
                        double minTemp = main.getDouble("temp_min");
                        int humidity = main.getInt("humidity");

                        JSONArray weatherArray = response.getJSONArray("weather");
                        JSONObject weather = weatherArray.getJSONObject(0);
                        String description = weather.getString("description");
                        String icon = weather.getString("icon");

                        // Update UI
                        runOnUiThread(() -> updateUI(currentTemp, maxTemp, minTemp, humidity, description, icon));
                    } catch (JSONException e) {
                        Toast.makeText(this, "JSON Parsing error", Toast.LENGTH_SHORT).show();
                    } finally {
                        progressBar.setVisibility(ProgressBar.GONE);
                    }
                },
                error -> {
                    progressBar.setVisibility(ProgressBar.GONE);
                    Toast.makeText(this, "Request failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                });

        queue.add(request);
    }

    private void updateUI(double currentTemp, double maxTemp, double minTemp, int humidity, String description, String icon) {
        ((TextView) findViewById(R.id.textCurrentTemp)).setText("Current Temp: " + currentTemp + "°C");
        ((TextView) findViewById(R.id.textMaxTemp)).setText("Max Temp: " + maxTemp + "°C");
        ((TextView) findViewById(R.id.textMinTemp)).setText("Min Temp: " + minTemp + "°C");
        ((TextView) findViewById(R.id.textHumidity)).setText("Humidity: " + humidity + "%");
        ((TextView) findViewById(R.id.textDescription)).setText("Description: " + description);
        findViewById(R.id.textCurrentTemp).setVisibility(TextView.VISIBLE);
        findViewById(R.id.textMaxTemp).setVisibility(TextView.VISIBLE);
        findViewById(R.id.textMinTemp).setVisibility(TextView.VISIBLE);
        findViewById(R.id.textHumidity).setVisibility(TextView.VISIBLE);
        findViewById(R.id.textDescription).setVisibility(TextView.VISIBLE);

        // Fetch and display the weather icon
        String iconUrl = "https://openweathermap.org/img/w/" + icon + ".png";
        ImageRequest imageRequest = new ImageRequest(iconUrl, bitmap -> {
            ImageView iconView = findViewById(R.id.imageWeatherIcon);
            iconView.setImageBitmap(bitmap);
            iconView.setVisibility(ImageView.VISIBLE);
        }, 0, 0, ImageView.ScaleType.CENTER, null,
                error -> Toast.makeText(this, "Failed to load icon", Toast.LENGTH_SHORT).show());

        queue.add(imageRequest);
    }
}
