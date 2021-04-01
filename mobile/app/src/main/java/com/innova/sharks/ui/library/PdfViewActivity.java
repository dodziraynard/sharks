package com.innova.sharks.ui.library;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.innova.sharks.R;
import com.innova.sharks.databinding.ActivityPdfViewBinding;
import com.innova.sharks.models.Book;
import com.innova.sharks.services.DownloadService;

import java.io.File;

import static com.innova.sharks.utils.Constants.BOOK_TO_PDF_VIEW;

public class PdfViewActivity extends AppCompatActivity {
    private static final String TAG = "PdfViewActivity";

    private static final String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_NETWORK_STATE,
    };
    private final int REQUEST_PERMISSIONS_CODE = 10;
    private ActivityPdfViewBinding mBinding;
    private Book mBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityPdfViewBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        setSupportActionBar(mBinding.toolbar);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mBook = getIntent().getParcelableExtra(BOOK_TO_PDF_VIEW);
        setTitle(mBook.getTitle());

        mBinding.webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mBinding.progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(PdfViewActivity.this, description + " Code: " + errorCode, Toast.LENGTH_SHORT).show();
                mBinding.webView.loadUrl("file:///android_asset/error.html ");
                mBinding.progressBar.setVisibility(View.GONE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                mBinding.progressBar.setVisibility(View.VISIBLE);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mBinding.progressBar.setVisibility(View.GONE);
            }
        });

        mBinding.webView.getSettings().setLoadsImagesAutomatically(true);
        mBinding.webView.getSettings().setJavaScriptEnabled(true);
        mBinding.webView.getSettings().setAppCacheEnabled(true);
        mBinding.webView.getSettings().setBuiltInZoomControls(true);
        mBinding.webView.getSettings().setDisplayZoomControls(false);
        mBinding.webView.getSettings().setSupportZoom(true);
        mBinding.webView.getSettings().setLoadWithOverviewMode(true);
        mBinding.webView.getSettings().setUseWideViewPort(true);
        mBinding.webView.getSettings().setAllowContentAccess(true);
        mBinding.webView.getSettings().setAllowFileAccess(true);
        mBinding.webView.loadUrl("https://docs.google.com/gview?embedded=true&url=" + mBook.getFile());
    }

    private void reloadPdf() {
        mBinding.progressBar.setVisibility(View.VISIBLE);
        mBinding.webView.reload();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_download) {
            String extension = mBook.getFile().split("\\.")[mBook.getFile().split("\\.").length - 1];
            String filename = mBook.getTitle() + "." + extension;
            downloadPdfInBackground(mBook.getFile(), filename);
        } else if (item.getItemId() == R.id.action_refresh) {
            reloadPdf();
        } else if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pdf_menu, menu);
        return true;
    }

    private void downloadPdfInBackground(String fileUrl, String filename) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && permissionsDenied()) {
            requestPermissions(PERMISSIONS, REQUEST_PERMISSIONS_CODE);
            return;
        }
        String path = "/" + getString(R.string.app_name);
        final File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdir();
        }
        final String fullPath = Environment.getExternalStorageDirectory() + path + "/" + filename;
        if (!new File(fullPath).exists()) {
            Toast.makeText(this, "Downloading...", Toast.LENGTH_SHORT).show();
            startService(DownloadService.getDownloadService(this, fileUrl, folder.getPath(), filename));
        } else {
            View parentLayout = findViewById(android.R.id.content);
            Snackbar.make(parentLayout, "File already exists. Download again?", Snackbar.LENGTH_LONG)
                    .setAction("Yes", view -> {
                        Toast.makeText(this, "Downloading...", Toast.LENGTH_SHORT).show();
                        startService(DownloadService.getDownloadService(this, fileUrl, folder.getPath(), filename));
                    }).show();
        }
    }

    private boolean permissionsDenied() {
        for (String permission : PERMISSIONS) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissionsDenied() && REQUEST_PERMISSIONS_CODE == requestCode) {
            Toast.makeText(this, "Perm Denied", Toast.LENGTH_LONG).show();
            // ((ActivityManager) (this.getSystemService(ACTIVITY_SERVICE))).clearApplicationUserData();
        } else {
            String extension = mBook.getFile().split("\\.")[mBook.getFile().split("\\.").length - 1];
            String filename = mBook.getTitle() + "." + extension;
            downloadPdfInBackground(mBook.getFile(), filename);
        }
    }
}