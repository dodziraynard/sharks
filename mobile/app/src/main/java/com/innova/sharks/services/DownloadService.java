package com.innova.sharks.services;

import android.app.DownloadManager;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DownloadService extends IntentService {
    private static final String TAG = "DownloadService";
    private static final String DOWNLOAD_PATH = "com.innova.sharks.services.path";
    private static final String DESTINATION_PATH = "com.innova.sharks.services.destination";
    private static final String TITLE = "title";

    public DownloadService() {
        super("DownloadService");
    }

    public static Intent getDownloadService(final @NonNull Context callingClassContext,
                                            final @NonNull String downloadPath,
                                            final @NonNull String destinationPath,
                                            final String title) {
        return new Intent(callingClassContext, DownloadService.class)
                .putExtra(DOWNLOAD_PATH, downloadPath)
                .putExtra(DESTINATION_PATH, destinationPath)
                .putExtra(TITLE, title);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String downloadPath = intent.getStringExtra(DOWNLOAD_PATH);
        String destinationPath = intent.getStringExtra(DESTINATION_PATH);
        String title = intent.getStringExtra(TITLE);
        startDownload(downloadPath, destinationPath, title);
    }

    private void startDownload(String downloadPath, String destinationPath, String title) {
        try {
            Uri uri = Uri.parse(downloadPath); // Path where you want to download file.
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);  // Tell on which network you want to download file.
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);  // This will show notification on top when downloading the file.
            request.setTitle(title); // Title for notification.
            request.setVisibleInDownloadsUi(true);
            request.setDestinationInExternalPublicDir(destinationPath, title);  // Storage directory path
            ((DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE)).enqueue(request); // This will start downloading
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "NOPE", Toast.LENGTH_LONG).show();
            Log.d(TAG, "startDownload: ERROR");
            e.printStackTrace();
        }
    }
}
