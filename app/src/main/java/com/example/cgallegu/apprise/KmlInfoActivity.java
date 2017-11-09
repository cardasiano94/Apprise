package com.example.cgallegu.apprise;

import android.content.Intent;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class KmlInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kml_info);

        Intent intent = getIntent();
        Bundle characteristics = intent.getExtras();

        String name = characteristics.getString("EXTRA_MESSAGE");
        String description = characteristics.getString("DESCRIPTION");
        setTitle(name);
        TextView info = (TextView)findViewById(R.id.info);
        info.setText(description);
    }
}
