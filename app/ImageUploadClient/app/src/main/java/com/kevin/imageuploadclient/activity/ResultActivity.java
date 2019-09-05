package com.kevin.imageuploadclient.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kevin.imageuploadclient.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class ResultActivity extends ActionBarActivity {

    private ImageView resultImage;
    private TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Button sendRequest = (Button) findViewById(R.id.locate);
        resultText = (TextView) findViewById(R.id.result_text);
        resultImage = (ImageView) findViewById(R.id.result_image);

        sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequestWithHttpURLConnection();
            }
        });
    }

    private void sendRequestWithHttpURLConnection() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL("http://172.28.243.29:8080/ImageUploadServer/");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    showResponse(response.toString(), resultImage);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    private void showResponse(String firstW, ImageView firstImg) {
        if (firstW.contains("Borrow")) {  //自助借还区
            resultText.setText("自助借还区");
            firstImg.setImageDrawable(ContextCompat.getDrawable(ResultActivity.this, R.drawable.borrow));
        } else if (firstW.contains("Area")) {  //检索区
            resultText.setText("检索区");
            firstImg.setImageDrawable(ContextCompat.getDrawable(ResultActivity.this, R.drawable.getbook));
        } else if (firstW.contains("hall")) {  //大厅
            resultText.setText("大厅");
            firstImg.setImageDrawable(ContextCompat.getDrawable(ResultActivity.this, R.drawable.hall));
        } else if (firstW.contains("tech")) {    //新技术体验区
            resultText.setText("新技术体验区");
            firstImg.setImageDrawable(ContextCompat.getDrawable(ResultActivity.this, R.drawable.technical));
        } else {
            resultText.setText("请重新拍摄照片");
        }
    }
}
