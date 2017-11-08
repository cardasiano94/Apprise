package com.example.cgallegu.apprise;

import android.os.Bundle;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.app.ListActivity;
import android.widget.Toast;
import android.content.Context;


public class EventListActivity extends ListActivity {

    private TextView text;
    private List<String> listValues;
    private static Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        context = getApplicationContext();
        updateKmls();

        text = (TextView) findViewById(R.id.mainText);

        listValues = new ArrayList<String>();
        listValues.add("Android");
        listValues.add("iOS");
        listValues.add("Symbian");
        listValues.add("Blackberry");
        listValues.add("Windows Phone");

        // initiate the listadapter
        ArrayAdapter<String> myAdapter = new ArrayAdapter <String>(this,
                R.layout.event_layout, R.id.listText, listValues);

        // assign the list adapter
        setListAdapter(myAdapter);

    }

    // when an item of the list is clicked
    @Override
    protected void onListItemClick(ListView list, View view, int position, long id) {
        super.onListItemClick(list, view, position, id);

        String selectedItem = (String) getListView().getItemAtPosition(position);
        //String selectedItem = (String) getListAdapter().getItem(position);

        text.setText("You clicked " + selectedItem + " at position " + position);
    }

    ///funcion updatea kmls en data/data/com.example.cgallegu.apprise/files/ExtractLoc
    public void updateKmls() {
        String serverFilePath = "http://labcomp.cl/~oduarte/kmls.zip";
        //String serverFilePath = "https://drive.google.com/uc?export=download&id=0B2mbV4LAZpcLbHdTWmwtWXlHenc";

        String path = FileUtils.getDataDir(context).getAbsolutePath();

        String fileName = "kml";
        File file = new File(path, fileName);

        String localPath = file.getAbsolutePath();
        FileUtils.deleteRecursive(FileUtils.getDataDir(context, "ExtractLoc"));
        String unzipPath = FileUtils.getDataDir(context, "ExtractLoc").getAbsolutePath();

        Log.e("EventListActivity",path);

        FileDownloadService.DownloadRequest downloadRequest = new FileDownloadService.DownloadRequest(serverFilePath, localPath);
        downloadRequest.setRequiresUnzip(true);
        downloadRequest.setDeleteZipAfterExtract(true);
        downloadRequest.setUnzipAtFilePath(unzipPath);

        FileDownloadService.OnDownloadStatusListener listener = new FileDownloadService.OnDownloadStatusListener() {

            @Override
            public void onDownloadStarted() {
                //subscriber.onNext(String.valueOf(0));
                Toast.makeText(EventListActivity.this,
                        "started",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDownloadCompleted() {
                //subscriber.onNext(String.valueOf(100));
                Toast.makeText(EventListActivity.this,
                        "complete",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDownloadFailed() {
                Toast.makeText(EventListActivity.this,
                        "failed",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDownloadProgress(int progress) {
                Toast.makeText(EventListActivity.this,
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
        listValues = new ArrayList<String>();
        for (int i = 0; i < files.length; i++)
        {

            listValues.add(files[i].getName());
            Log.e("Files", "FileName:" + files[i].getName());
            // files[i].getName() para poner en listActivity
        }
        ////////////////////


    }

}