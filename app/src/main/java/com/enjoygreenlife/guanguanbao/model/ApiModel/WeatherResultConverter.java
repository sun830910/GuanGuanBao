package com.enjoygreenlife.guanguanbao.model.ApiModel;

import android.widget.ImageView;

import com.enjoygreenlife.guanguanbao.R;

/**
 * Created by luthertsai on 2017/12/7.
 */

public class WeatherResultConverter {
    private WeatherStatus convertWeatherToCode(String weather) {
        if (weather.equals("晴")) {
            return WeatherStatus.SUNNY;
        } else if (weather.equals("多云")) {
            return WeatherStatus.PARTLY_CLOUDLY;
        } else if (weather.equals("阴")) {
            return WeatherStatus.CLOUDY;
        } else if (weather.contains("雷")) {
            return WeatherStatus.THUNDERSTORM;
        } else if (weather.contains("雨")) {
            return WeatherStatus.RAIN;
        } else if (weather.contains("雪")) {
            return WeatherStatus.SNOW;
        } else if (weather.contains("尘") || weather.contains("沙") || weather.contains("霾")) {
            return WeatherStatus.DUSTY;
        } else if (weather.contains("雾")) {
            return WeatherStatus.FOGGY;
        } else if (weather.equals("龙卷风")) {
            return WeatherStatus.TORNATO;
        } else {
            return WeatherStatus.SUNNY;
        }
    }

    public void SetWeatherIcon(ImageView imgView, String weatherInfo) {
        if (this.convertWeatherToCode(weatherInfo) == WeatherStatus.SUNNY) {
            imgView.setImageResource(R.drawable.ic_weather_sun);
        } else if (this.convertWeatherToCode(weatherInfo) == WeatherStatus.PARTLY_CLOUDLY) {
            imgView.setImageResource(R.drawable.ic_weather_cloudy);
        } else if (this.convertWeatherToCode(weatherInfo) == WeatherStatus.CLOUDY) {
            imgView.setImageResource(R.drawable.ic_weather_cloud);
        } else if (this.convertWeatherToCode(weatherInfo) == WeatherStatus.THUNDERSTORM) {
            imgView.setImageResource(R.drawable.ic_weather_storm);
        } else if (this.convertWeatherToCode(weatherInfo) == WeatherStatus.RAIN) {
            imgView.setImageResource(R.drawable.ic_weather_storm);
        } else if (this.convertWeatherToCode(weatherInfo) == WeatherStatus.SNOW) {
            imgView.setImageResource(R.drawable.ic_weather_storm);
        } else if (this.convertWeatherToCode(weatherInfo) == WeatherStatus.DUSTY) {
            imgView.setImageResource(R.drawable.ic_weather_storm);
        } else if (this.convertWeatherToCode(weatherInfo) == WeatherStatus.FOGGY) {
            imgView.setImageResource(R.drawable.ic_weather_storm);
        } else if (this.convertWeatherToCode(weatherInfo) == WeatherStatus.TORNATO) {
            imgView.setImageResource(R.drawable.ic_weather_storm);
        } else {
            imgView.setImageResource(R.drawable.ic_weather_sun);
        }
    }

    enum WeatherStatus {
        SUNNY, RAIN, PARTLY_CLOUDLY, CLOUDY, SNOW, THUNDERSTORM, FOGGY, DUSTY, TORNATO
    }
}
