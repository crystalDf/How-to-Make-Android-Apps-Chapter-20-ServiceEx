package com.star.serviceex;


import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadFileService extends IntentService{

    public static final String TRANSACTION_DONE = "com.star.TRANSACTION_DONE";

    public static final String FILE_NAME = "myFile.txt";

    public DownloadFileService() {
        super(DownloadFileService.class.getName());
    }

    public DownloadFileService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String passedURL = intent.getStringExtra("url");

        downloadFile(passedURL);

        Intent i = new Intent(TRANSACTION_DONE);

        sendBroadcast(i);

    }

    private void downloadFile(String url) {

        try {
            FileOutputStream fileOutputStream = openFileOutput(FILE_NAME, Context.MODE_PRIVATE);

            URL fileURL = new URL(url);

            HttpURLConnection httpURLConnection = (HttpURLConnection) fileURL.openConnection();

            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setDoOutput(true);

            httpURLConnection.connect();

            InputStream inputStream = httpURLConnection.getInputStream();

            byte[] buffer = new byte[1024];

            int bufferLength = 0;

            while ((bufferLength = inputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, bufferLength);
            }

            fileOutputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
