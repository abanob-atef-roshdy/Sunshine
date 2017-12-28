
package com.example.android.sunshine.utilities;

import android.content.ContentValues;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;


public  final      class OpenWeatherJsonUtils  {






    public static String[] getSimpleWeatherStringsFromJson(Context context, String forecastJsonStr)
            throws JSONException {


        final String OWM_LIST = "list";


        final String OWM_TEMPERATURE = "main";


        final String OWM_MAX = "temp_max";
        final String OWM_MIN = "temp_min";

        final String OWM_WEATHER = "weather";
        final String OWM_DESCRIPTION = "main";

        final String OWM_MESSAGE_CODE = "cod";


        String[] parsedWeatherData = null;

        JSONObject forecastJson = new JSONObject(forecastJsonStr);


        if (forecastJson.has(OWM_MESSAGE_CODE)) {
            int errorCode = forecastJson.getInt(OWM_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:

                    return null;
                default:

                    return null;
            }
        }

        JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);

        parsedWeatherData = new String[weatherArray.length()];



        for (int i = 0; i < weatherArray.length(); i++) {




            double high;
            double low;
            String description;


            JSONObject dayForecast = weatherArray.getJSONObject(i);


            String dateandhour = dayForecast.getString("dt_txt");



            JSONObject weatherObject =
                    dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
            description = weatherObject.getString(OWM_DESCRIPTION);


            JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);
            high = temperatureObject.getDouble(OWM_MAX);

            low = temperatureObject.getDouble(OWM_MIN);

            parsedWeatherData[i] = dateandhour +  "\n"  + description + " \n " + high + (char) 0x00B0 +"/" + low + (char) 0x00B0;
        }

        return parsedWeatherData;

    }


}