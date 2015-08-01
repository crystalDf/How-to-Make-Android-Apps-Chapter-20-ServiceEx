package com.star.serviceex;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;


public class MainActivity extends AppCompatActivity {

    private Button mStartServiceButton;
    private EditText mDownloadedEditText;

    private BroadcastReceiver mDownloadFileReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            showFileContents();
        }
    };

    private IntentFilter mIntentFilter = new IntentFilter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStartServiceButton = (Button) findViewById(R.id.start_service_button);

        mStartServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DownloadFileService.class);

                intent.putExtra("url", "http://www.newthinktank.com/wordpress/lotr.txt");

                startService(intent);
            }
        });

        mDownloadedEditText = (EditText) findViewById(R.id.downloaded_edit_text);

        mIntentFilter.addAction(DownloadFileService.TRANSACTION_DONE);

        registerReceiver(mDownloadFileReceiver, mIntentFilter);

    }

    @Override
    protected void onDestroy() {

        unregisterReceiver(mDownloadFileReceiver);

        super.onDestroy();
    }

    private void showFileContents() {

        try {
            FileInputStream fileInputStream = openFileInput(DownloadFileService.FILE_NAME);

            StringBuilder stringBuilder = new StringBuilder();

            String line = null;

            BufferedReader bufferedReader =
                    new BufferedReader(new InputStreamReader(fileInputStream));

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }

            mDownloadedEditText.setText(stringBuilder.toString());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
