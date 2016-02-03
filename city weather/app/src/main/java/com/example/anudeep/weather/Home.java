package com.example.anudeep.weather;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Home extends AppCompatActivity {

    TextView displayText,displayWeather,displayMinmum,displayMaximum;
    String response,temp,  min,  max;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        displayMinmum = (TextView) findViewById(R.id.min);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_logout){
            Intent i = new Intent(Home.this, MainActivity.class);
            startActivity(i);
            finish();
        }else if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void weather(View v) {


        if (v.getId() == R.id.weather) {
            String city = ((EditText) findViewById(R.id.city)).getText().toString();
            final String urlText = "http://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=44db6a862fba0b067b1930da0d769e98";

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL url = new URL(urlText);
                        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                        urlConnection.setRequestMethod("GET");
                        urlConnection.setDoInput(true);
                        urlConnection.setDoOutput(true);
                        StringBuilder stringBuilder = new StringBuilder();
                        InputStream is;
                        is = urlConnection.getInputStream();
                        BufferedReader br = new BufferedReader(new InputStreamReader(is));
                        String line;
                        while ((line = br.readLine()) != null) {
                            stringBuilder.append(line);
                        }
                        response = stringBuilder.toString();
                        System.out.println(stringBuilder.toString());
                        is.close();
                        urlConnection.disconnect();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        JSONObject j = new JSONObject(response);
                        JSONObject info = j.getJSONObject("main");
                        min = "minimum temperature: "+info.getString("temp_min") +" K";
                        max = min + "\nmaximum temperature: " + info.getString("temp_max")+" K";

                        Log.d("min", min);
                        Log.d("max", max);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    {

                    }
                }
            });

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    displayMinmum.setText(max);
                }
            }, 1500);

        }
    }
}
