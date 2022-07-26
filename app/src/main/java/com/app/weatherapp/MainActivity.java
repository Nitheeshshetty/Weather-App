package com.app.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    final String App_ID = "80d995d45f349ad22ac4a2c9b1433eea";
    final String Weather_URL = "https://api.openweathermap.org/data/2.5/weather";
    final long Min_Time = 5000;
    final float Min_Distance = 1000;
    int Request_Code = 101;
    String Location_Provider = LocationManager.GPS_PROVIDER;
    TextView nameofCity, weatherState, temperature;
    ImageView mweathericon;
    RelativeLayout mCityFinder;
    LocationManager mlocationManager;
    LocationListener mlocationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherState = findViewById(R.id.weathercondition);
        temperature = findViewById(R.id.temprature);
        mweathericon = findViewById(R.id.weathericon);
        mCityFinder = findViewById(R.id.cityfinder);
        nameofCity = findViewById(R.id.cityname);

        mCityFinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, cityFinder.class);
                startActivity(intent);
            }
        });
    }

  /*  @Override
    protected void onResume() {
        super.onResume();
        getWeatherForCurrentLocation();
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        Intent mintent=getIntent();
        String city=mintent.getStringExtra("City");
        if(city!=null)
        {
            getWeatherForNewCity(city);
        }
        else
        {
            getWeatherForCurrentLocation();
        }
    }

    private void getWeatherForNewCity(String city){
        RequestParams params=new RequestParams();
        params.put("q",city);
        params.put("appid",App_ID);
        letsdosomenetworking(params);
    }
    private void getWeatherForCurrentLocation() {
        mlocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mlocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {

                String Latitude = String.valueOf(location.getLatitude());
                String Longitude = String.valueOf(location.getLongitude());

                RequestParams params= new RequestParams();
                params.put("lat",Latitude);
                params.put("lon",Longitude);
                params.put("appid",App_ID);
                letsdosomenetworking(params);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {

            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {


            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            },Request_Code);
            return;
        }
        mlocationManager.requestLocationUpdates(Location_Provider, Min_Time, Min_Distance, mlocationListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==Request_Code)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(MainActivity.this, "location fetched!", Toast.LENGTH_SHORT).show();
                getWeatherForCurrentLocation();
            }
            else
                {
                //user denied permission
            }
        }
    }
    private void letsdosomenetworking(RequestParams params){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Weather_URL,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            //    super.onSuccess(statusCode, headers, response);
                Toast.makeText(MainActivity.this, "Weather Details Fetched Successfully", Toast.LENGTH_SHORT).show();
                weatherData weatherD=weatherData.fromJson(response);
                updateUI(weatherD);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
              //  super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }
    private void updateUI(weatherData weather){
        temperature.setText(weather.getMtemprature());
        nameofCity.setText(weather.getMcity());
        weatherState.setText(weather.getMweathertype());
        int resourceID=getResources().getIdentifier(weather.getMicon(),"drawable",getPackageName());
        mweathericon.setImageResource(resourceID);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mlocationManager!=null)
        {
            mlocationManager.removeUpdates(mlocationListener);
        }
    }
}