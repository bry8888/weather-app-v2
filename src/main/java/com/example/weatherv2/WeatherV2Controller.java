package com.example.weatherv2;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;

@Controller
public class WeatherV2Controller {

    private final String API_KEY = "72b4f038d8f8c4246228d5c92caaaa1e";

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/")
    public String weather(@RequestParam String city, Model model) {
        if (!city.isEmpty()) {
            String apiUrl = "http://api.openweathermap.org/geo/1.0/direct?q=" + city + "&limit=1&appid=" + API_KEY;
            RestTemplate restTemplate = new RestTemplate();
            LongLat[] locations = restTemplate.getForObject(apiUrl, LongLat[].class);

            if (locations != null && locations.length > 0) {
                double lat = locations[0].getLat();
                double lon = locations[0].getLon();
                model.addAttribute("latitude", lat);
                model.addAttribute("longitude", lon);

                String weatherApiUrl = "http://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&appid=" + API_KEY;
                RestTemplate weatherRestTemplate = new RestTemplate();
                WeatherResult weatherResults = weatherRestTemplate.getForObject(weatherApiUrl, WeatherResult.class);

                if (weatherResults != null) {
                    // Get the main object
                    Map<String, Object> main = weatherResults.getMain();
                    Double temp = (Double) main.get("temp") - 273.15;
                    temp = Math.ceil(temp);
                    String tempFormatted = String.format("%.0f", temp);
                    int humidity = (int) main.get("humidity");

                    // Get the weather list
                    List<Map<String, Object>> weatherList = weatherResults.getWeather();
                    // Get the first weather object from the list
                    Map<String, Object> firstWeather = weatherList.get(0);
                    // Get the description property from the first weather object
                    Object description = firstWeather.get("description");
                    String strDescription = String.valueOf(description);
                    // icon
                    Object icon = firstWeather.get("icon");

                    model.addAttribute("city", capitalizeFirstLetter(city));
                    model.addAttribute("icon", icon);
                    model.addAttribute("temp", tempFormatted + "°C");
                    model.addAttribute("humidity", humidity);
                    model.addAttribute("description", capitalizeFirstLetter(strDescription));
                } else {
                    model.addAttribute("error", "Weather record not found");
                }
            } else {
                model.addAttribute("error", "City not found");
            }
        } else {
            model.addAttribute("error", "Please enter a city");
        }
        return "index";
    }

    public static String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }
}

class WeatherResult {
    private List<Map<String, Object>> weather;
    private Map<String, Object> main;

    // Getters and setters
    public List<Map<String, Object>> getWeather() {
        return weather;
    }

    public void setWeather(List<Map<String, Object>> weather) {
        this.weather = weather;
    }
    
    public Map<String, Object> getMain() {
        return main;
    }

    public void setMain(Map<String, Object> main) {
        this.main = main;
    }
}

class LongLat {
    private double lat;
    private double lon;

    // Getters and setters
    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
