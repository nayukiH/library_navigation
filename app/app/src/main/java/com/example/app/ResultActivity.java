package com.example.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ResultActivity extends AppCompatActivity {

    private String url = "";
    private TextView resultText;
    private ImageView resultImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        resultText = (TextView) findViewById(R.id.result_text);
        resultImage = (ImageView) findViewById(R.id.result_image);
        GetResult getResult = new GetResult();
        getResult.execute(url);

    }

    @SuppressLint("StaticFieldLeak")
    private class GetResult extends AsyncTask<String, String, String> {
        private String openConnection(String address) {
            String result = "";
            try {
                URL url = new URL(address );
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                InputStream in = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    result = result + line;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Log.i("info", result);
            return result;
        }

        @Override
        protected String doInBackground(String... params) {
            return openConnection(params[0]);
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String result){
            try {
                JSONObject object = new JSONObject(result);
                JSONObject place = (JSONObject) object.get("result");
               // JSONArray located = (JSONArray) place.get("place");
                //resultText.setText(place.getString("place"));
                resultImage = (ImageView) findViewById(R.id.result_image);
                String location =place.getString("place");
                changeResultImg(location,resultImage);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void changeResultImg(String firstW, ImageView firstImg) {
        if (firstW.contains("")) {  //自助借还区
            resultText.setText("自助借还区");
            firstImg.setImageDrawable(ContextCompat.getDrawable(ResultActivity.this, R.drawable.borrow));
        } else if (firstW.contains("")) {  //检索区
            resultText.setText("检索区");
            firstImg.setImageDrawable(ContextCompat.getDrawable(ResultActivity.this, R.drawable.getbook));
        } else if (firstW.contains("")) {  //大厅
            resultText.setText("大厅");
            firstImg.setImageDrawable(ContextCompat.getDrawable(ResultActivity.this, R.drawable.hall));
        }else if (firstW.contains("")) {    //文化展区
            resultText.setText("文化展区");
            firstImg.setImageDrawable(ContextCompat.getDrawable(ResultActivity.this, R.drawable.show));
        }else if (firstW.contains("")) {    //新技术体验区
            resultText.setText("新技术体验区");
            firstImg.setImageDrawable(ContextCompat.getDrawable(ResultActivity.this, R.drawable.technical));
        }
    }
}