package io.dagger.sample.module;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import io.dagger.client.DaggerQueryException;
import io.dagger.client.Secret;
import io.dagger.module.Base;
import io.dagger.module.annotation.Function;
import io.dagger.module.annotation.Object;

/** Weather Fetcher */
@Object
public class WeatherApi extends Base {

  public WeatherApi() {
    super();
  }

  /** 
   * Returns the current weather in the given city
   * 
   * @param city the city name
  */
  @Function
  public String current(Secret apiKey, String city) throws InterruptedException, ExecutionException, DaggerQueryException {
    WeatherFetcher weatherFetcher = new WeatherFetcher(apiKey.plaintext());
    try {
      return weatherFetcher.fetchWeather(city);        
    } catch (IOException e) {
      return "Could not fetch weather data for " + city;
    }
  }
}
