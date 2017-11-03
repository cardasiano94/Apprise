package com.example.cgallegu.apprise;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import android.content.Context;
import android.widget.Toast;


import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class EventsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ProgressDialog pDialog;
    public static final int progress_bar_type = 0;
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        context = getApplicationContext();
        updateKmls();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.events, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_map) {
            MapFragment mapFragment = new MapFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.mainLayout, mapFragment).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    ///funcion updatea kmls en data/data/com.example.cgallegu.apprise/files/ExtractLoc
    public void updateKmls() {
        String serverFilePath = "https://drive.google.com/uc?export=download&id=0B2mbV4LAZpcLZ2t6bGlwalNWU3M";
        //String serverFilePath = "https://drive.google.com/uc?export=download&id=0B2mbV4LAZpcLbHdTWmwtWXlHenc";

        String path = FileUtils.getDataDir(context).getAbsolutePath();

        String fileName = "kml";
        File file = new File(path, fileName);

        String localPath = file.getAbsolutePath();
        FileUtils.deleteRecursive(FileUtils.getDataDir(context, "ExtractLoc"));
        String unzipPath = FileUtils.getDataDir(context, "ExtractLoc").getAbsolutePath();

        Log.e("EventsActivity",path);

        FileDownloadService.DownloadRequest downloadRequest = new FileDownloadService.DownloadRequest(serverFilePath, localPath);
        downloadRequest.setRequiresUnzip(true);
        downloadRequest.setDeleteZipAfterExtract(true);
        downloadRequest.setUnzipAtFilePath(unzipPath);

        FileDownloadService.OnDownloadStatusListener listener = new FileDownloadService.OnDownloadStatusListener() {

            @Override
            public void onDownloadStarted() {
                //subscriber.onNext(String.valueOf(0));
                Toast.makeText(EventsActivity.this,
                        "started",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDownloadCompleted() {
                //subscriber.onNext(String.valueOf(100));
                Toast.makeText(EventsActivity.this,
                        "complete",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDownloadFailed() {
                Toast.makeText(EventsActivity.this,
                        "failed",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDownloadProgress(int progress) {
                Toast.makeText(EventsActivity.this,
                        "progress="+String.valueOf(progress),
                        Toast.LENGTH_SHORT).show();
            }
        };

        FileDownloadService.FileDownloader downloader = FileDownloadService.FileDownloader.getInstance(downloadRequest, listener);
        downloader.download(context);

        //logcat para ver q archivos estaban en el zip
        String path2 = "data/data/com.example.cgallegu.apprise/files/ExtractLoc";
        Log.e("Files", "Path: " + path2);
        File directory = new File(path2);
        File[] files = directory.listFiles();
        Log.e("Files", "Size: "+ files.length);
        for (int i = 0; i < files.length; i++)
        {
            Log.e("Files", "FileName:" + files[i].getName());
            ///files[i].getName() para poner en listActivity
        }
        ////////////////////


    }


}

