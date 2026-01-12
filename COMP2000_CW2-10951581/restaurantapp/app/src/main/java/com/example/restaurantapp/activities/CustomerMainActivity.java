package com.example.restaurantapp.activities;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.restaurantapp.R;
import com.example.restaurantapp.model.UserSession;

public class CustomerMainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_main);

        Button viewMenu = findViewById(R.id.viewMenuBtn);
        Button viewReservations = findViewById(R.id.viewReservationsBtn);
        viewMenu.setOnClickListener(v-> startActivity(new Intent(this,
                MenuActivity.class)));
        viewReservations.setOnClickListener(v-> startActivity(new
                Intent(this, ReservationActivity.class)));
    }

}