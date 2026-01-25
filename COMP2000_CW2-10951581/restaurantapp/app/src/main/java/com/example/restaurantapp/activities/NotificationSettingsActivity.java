package com.example.restaurantapp.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import com.example.restaurantapp.R;
import com.example.restaurantapp.data.AppDatabase;
import com.example.restaurantapp.model.UserSession;

public class NotificationSettingsActivity extends AppCompatActivity {

    private SwitchCompat enabledSwitch, editSwitch, cancelSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_settings);

        //switch listeners
        enabledSwitch = findViewById(R.id.switchEnabled);
        editSwitch = findViewById(R.id.switchEdit);
        cancelSwitch = findViewById(R.id.switchCancel);



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Notification settings");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }





    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}