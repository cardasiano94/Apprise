package com.example.cgallegu.apprise;

import android.content.Intent;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class KmlInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kml_info);

        Intent intent = getIntent();
        Bundle characteristics = intent.getExtras();

        String nameText = characteristics.getString("EXTRA_MESSAGE");
        String descriptionText = characteristics.getString("DESCRIPTION");
        //setTitle(name);
        TextView name = (TextView)findViewById(R.id.kml_info_name);
        TextView description = (TextView)findViewById(R.id.kml_info_description);

        name.setText(nameText);
        description.setText(descriptionText);
    }
}
