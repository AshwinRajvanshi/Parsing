package com.example.android.xml;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static com.example.android.xml.R.id.btn_update;

public class MainActivity extends Activity {
    private TextView txt;
    public Button btn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from activity_main.xml
        setContentView(R.layout.activity_main);
        btn = (Button) findViewById(btn_update);
        txt = (TextView) findViewById(R.id.Text);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new JSONTask().execute();
            }
        });
    }

    public class JSONTask extends AsyncTask<Void, String, String> {

        @Override
        protected String doInBackground(Void... params) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;
            String line = "";
            StringBuffer finalBufferData = new StringBuffer();
            try {
                //URL url = new URL("https://jsonparsingdemo-cec5b.firebaseapp.com/jsonData/moviesDemoItem.txt");
                URL url = new URL("https://jsonparsingdemo-cec5b.firebaseapp.com/jsonData/moviesDemoList.txt");
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();


                while ((line = reader.readLine()) != null) {
                    buffer.append(line);

                }

                String finalJson = buffer.toString();
                Log.e("saf",finalJson);
                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray parentArray = parentObject.getJSONArray("movies");

                int i;
                for (i = 0; i < parentArray.length(); i++) {
                    JSONObject finalObject = parentArray.getJSONObject(i);
                    String movieName = finalObject.getString("movie");
                    int years = finalObject.getInt("year");
                    finalBufferData.append(movieName + "-"+years+"\n");
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return finalBufferData.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            txt.setText(result);
        }
    }
}
