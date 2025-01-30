package io.dagger.sample.module;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeatherFetcher {
    private static final String BASE_URL = "http://api.weatherapi.com/v1";
    private static final String CURRENT_PATH = "/current.json";
    private static final String FORCAST_PATH = "/forecast.json";

    private final String apiKey;

    public WeatherFetcher(String apiKey) {
        this.apiKey = apiKey;
    }

    public String fetchWeather(String city) throws IOException {
        String weatherData = callAPI(CURRENT_PATH, city, Map.of());
        return parseAndDisplayWeather(weatherData);
    }

    public String fetchForcast(String city) throws IOException {
        String weatherData = callAPI(FORCAST_PATH, city, Map.of("days", "2"));
        // return weatherData;
        return parseAndDisplayForcast(weatherData);
    }

    private String callAPI(String path, String city, Map<String, String> parameters) throws IOException {
        OkHttpClient client = new OkHttpClient();
        
        String extraParameters = parameters.entrySet().stream()
            .map(entry -> entry.getKey() + "=" + entry.getValue())
            .collect(Collectors.joining("&"));
        if (extraParameters.length() > 0) {
            extraParameters = "&" + extraParameters;
        }
        String url = "%s%s?key=%s&q=%s%s".formatted(BASE_URL, path, apiKey, city, extraParameters);
        
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
        sb.append("Temperature: " + temperature + "°C").append("\n");
        sb.append("Condition: " + condition);

        return sb.toString();
    }

    public String parseAndDisplayForcast(String jsonResponse) {
        JSONObject json = new JSONObject(jsonResponse);
        String location = json.getJSONObject("location").getString("name");
        String country = json.getJSONObject("location").getString("country");
        JSONObject forcast = json.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(1);
        double temperature = forcast.getJSONObject("day").getDouble("avgtemp_c");
        String condition = forcast.getJSONObject("day").getJSONObject("condition").getString("text");

        StringBuilder sb = new StringBuilder();
        sb.append("Tomorrow's weather at " + location + ", " + country + ":").append("\n");
        sb.append("Average temperature : " + temperature + "°C").append("\n");
        sb.append("Condition : " + condition);

        return sb.toString();
    }
}
