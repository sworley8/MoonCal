package com.example.sophi.myapplication;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.LogPrinter;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button json_button = (Button) findViewById(R.id.json_button);

        json_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RetrieveFeedTask().execute();

            }
        });

    }

    /* New class: Phases of the Moon

     */

    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

        private Exception exception;
        private String APIurl =
                "http://api.usno.navy.mil/moon/phase?date=5/3/2009&nump=48";


        protected String doInBackground(Void... urls) {
            // Do some validation here

            try {
                java.net.URL url = new URL(APIurl /*"date"  "&nump=" + "&apiKey=" + API_KEY*/);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }

                /* Now that the url is read, need to find "phase": and "date" and "time"*/ finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }

        }


        protected void onPostExecute(String response) {
            if (response == null) {
                response = "THERE WAS AN ERROR";
            }

            Log.i("INFO", response);
            try {
                JSONObject jObject = new JSONObject(response);
                JSONArray jArray = jObject.getJSONArray("phasedata");
                for (int i = 0; i<jArray.length();i++){
                    JSONObject oneObject = jArray.getJSONObject(i);
                    String phase = oneObject.getString("phase");
                    String date = oneObject.getString("date");
                    String time = oneObject.getString("time");

                    Log.i("phase",phase);
                        /*String jdate = jArray.get(i).getString(1);
                         String jtime = jArray.get(i).getString(2);*/
                    }

                Log.i("json", jArray.toString());


            } catch (JSONException e) {
                Toast.makeText(MainActivity.this, "error", Toast.LENGTH_LONG);
            }


        }
    }
}





