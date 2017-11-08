package com.example.cgallegu.apprise;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class KmlInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kml_info);

        Intent intent = getIntent();
        setTitle(intent.getStringExtra(MapFragment.EXTRA_MESSAGE));
    }
}
