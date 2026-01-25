package com.example.restaurantapp.activities;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.os.Build;
import android.Manifest;
import androidx.core.content.ContextCompat;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;

import android.app.NotificationManager;
import androidx.core.app.NotificationCompat;


import com.example.restaurantapp.R;
import com.example.restaurantapp.model.UserSession;

import com.example.restaurantapp.util.NotificationStore;
import com.example.restaurantapp.RestaurantApp;

public class CustomerMainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_main);

        requestNotificationPermission();

        //create toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Home");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Button viewMenu = findViewById(R.id.viewMenuBtn);
        Button viewReservations = findViewById(R.id.viewReservationsBtn);
        Button notificationSettings = findViewById(R.id.notificationSettingsBtn);

        viewMenu.setOnClickListener(v-> startActivity(new Intent(this,
                MenuActivity.class)));
        viewReservations.setOnClickListener(v-> startActivity(new
                Intent(this, ReservationActivity.class)));
        notificationSettings.setOnClickListener(v-> startActivity(new
                Intent(this, NotificationSettingsActivity.class)));
    }

    //toolbar functionality
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            UserSession.userId = null;
            UserSession.role = null;

            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showPendingNotifications();
    }

    private void showPendingNotifications() {

        var messages = NotificationStore.consume(this, UserSession.userId);
        if (messages.isEmpty()) return;

        NotificationManager manager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        for (String msg : messages) {
            manager.notify(
                    (int) System.currentTimeMillis(),
                    new NotificationCompat.Builder(this, RestaurantApp.CHANNEL_ID)
                            .setSmallIcon(R.drawable.ic_notification)
                            .setContentTitle("Reservation Update")
                            .setContentText(msg)
                            .setAutoCancel(true)
                            .build()
            );
        }
    }

    private static final int NOTIFICATION_PERMISSION_CODE = 1001;
    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        NOTIFICATION_PERMISSION_CODE
                );
            }
        }
    }

}