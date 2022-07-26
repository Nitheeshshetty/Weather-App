package com.app.weatherapp;

import android.content.Intent;

import org.json.JSONException;
import org.json.JSONObject;

public class weatherData {

    private String mtemprature,micon,mcity,mweathertype;
    private int mcondition;

    public static weatherData fromJson(JSONObject jsonObject) {

        try {
            weatherData weatherD = new weatherData();
            weatherD.mcity = jsonObject.getString("name");
            weatherD.mcondition = jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
            weatherD.mweathertype = jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");
            weatherD.micon = updateWeatherIcon(weatherD.mcondition);
            double tempResult = jsonObject.getJSONObject("main").getDouble("temp") - 273.15;
            int roundedValue = (int) Math.rint(tempResult);
            weatherD.mtemprature = Integer.toString(roundedValue);
            return weatherD;

        }
        catch (JSONException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    private  static String updateWeatherIcon(int condition)
    {
        if(condition>=0 && condition<=300)
        {
            return "thunderstorm1";
        }
        else if(condition>=300 && condition<=500)
        {
            return "lightrain";
        }
        else if(condition>=500 && condition<=600)
        {
            return "shower";
        }
        else if(condition>=600 && condition<=700)
        {
            return "snow2";
        }
        else if(condition>=701 && condition<=771)
        {
            return "fog";
        }
        else if(condition>=772 && condition<=800)
        {
            return "overcast";
        }
        else if(condition==800)
        {
            return "sunny";
        }
        else if(condition>=801 && condition<=804)
        {
            return "cloudy";
        }
        else if(condition>=900 && condition<=902)
        {
            return "thunderstorm1";
        }
        else if(condition==903)
        {
            return "snow1";
        }
        else if(condition==904)
        {
            return "sunny";
        }
        else if (condition>=905 && condition<=1000)
        {
            return "thunderstorm2";
        }
        return "Cant Fetch The Details For Now!";
    }

    public String getMtemprature() {
        return mtemprature+"°C";
    }

    public String getMicon() {
        return micon;
    }

    public String getMcity() {
        return mcity;
    }

    public String getMweathertype() {
        return mweathertype;
    }
}
