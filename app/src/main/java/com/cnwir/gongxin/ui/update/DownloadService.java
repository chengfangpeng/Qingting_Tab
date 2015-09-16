package com.cnwir.gongxin.ui.update;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by heaven on 2015/9/7.
 */
public class DownloadService extends IntentService{

    private static final String TAG = "DownloadService";

    public static final int UPDATE_PROGRESS = 8344;

    public static final int DOWNLOADSTOP = 8345;

    public static boolean isStopService;
    public DownloadService() {
        super("DownloadService");
        isStopService = false;
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        String urlToDownload = intent.getStringExtra("url");
        String fileDestination = intent.getStringExtra("dest");
        ResultReceiver receiver = (ResultReceiver) intent.getParcelableExtra("receiver");
        try {
            URL url = new URL(urlToDownload);
            URLConnection connection = url.openConnection();
            connection.connect();
            // this will be useful so that you can show a typical 0-100% progress bar
            int fileLength = connection.getContentLength();
            // download the file
            InputStream input = new BufferedInputStream(connection.getInputStream());
            OutputStream output = new FileOutputStream(fileDestination);
            byte data[] = new byte[100];
            long total = 0;
            int count;
            int temp = 0;
            while ((count = input.read(data)) != -1) {
                if(isStopService){
                    receiver.send(DOWNLOADSTOP, new Bundle());
                    return;
                }
                total += count;
                // publishing the progress....
                int sendData = (int) (total * 100 / fileLength);

                if(sendData != temp){

                    Bundle resultData = new Bundle();
                    resultData.putInt("progress" ,sendData);

                    receiver.send(UPDATE_PROGRESS, resultData);
                    temp = sendData;
                    Log.d(TAG, "download service progress = " + sendData);
                }

                output.write(data, 0, count);
            }
            output.flush();
            output.close();
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        Bundle resultData = new Bundle();
//        resultData.putInt("progress" ,100);
//        receiver.send(UPDATE_PROGRESS, resultData);
    }
}
