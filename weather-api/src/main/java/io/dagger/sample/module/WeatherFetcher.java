package io.dagger.sample.module;

import java.io.IOException;

import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeatherFetcher {
    private static final String BASE_URL = "http://api.weatherapi.com/v1/current.json";

    private final String apiKey;

    public WeatherFetcher(String apiKey) {
        this.apiKey = apiKey;
    }

    public String fetchWeather(String city) throws IOException {
        String weatherData = getWeatherFromAPI(city);
        return parseAndDisplayWeather(weatherData);
    }

    public String getWeatherFromAPI(String city) throws IOException {
        OkHttpClient client = new OkHttpClient();
        String url = "%s?key=%s&q=%s".formatted(BASE_URL, apiKey, city);
        
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected server response: " + response);
            }
            return response.body().string();
        }
    }

    public String parseAndDisplayWeather(String jsonResponse) {
        JSONObject json = new JSONObject(jsonResponse);
        String location = json.getJSONObject("location").getString("name");
        String country = json.getJSONObject("location").getString("country");
        double temperature = json.getJSONObject("current").getDouble("temp_c");
        String condition = json.getJSONObject("current").getJSONObject("condition").getString("text");
        
        StringBuilder sb = new StringBuilder();
        sb.append("Current weather at " + location + ", " + country + " :").append("\n");
        sb.append("Temperature : " + temperature + "°C").append("\n");
        sb.append("Condition : " + condition);

        return sb.toString();
    }
}
