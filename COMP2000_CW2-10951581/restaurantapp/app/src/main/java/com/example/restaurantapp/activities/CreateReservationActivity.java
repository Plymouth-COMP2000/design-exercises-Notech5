package com.example.restaurantapp.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.restaurantapp.R;
import com.example.restaurantapp.data.AppDatabase;
import com.example.restaurantapp.model.Reservation;
import com.example.restaurantapp.model.UserSession;

import java.util.Calendar;
import java.util.Locale;

public class CreateReservationActivity extends AppCompatActivity {

    private EditText surname, date, time, guests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_reservation);

        //toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //input field views
        surname = findViewById(R.id.surname);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        guests = findViewById(R.id.guests);
        Button save = findViewById(R.id.saveReservationBtn);

        //listeners
        date.setOnClickListener(v -> showDatePicker());
        time.setOnClickListener(v -> showTimePicker());

        save.setOnClickListener(v -> saveReservation());
    }

    //date
    private void showDatePicker() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(this,
                (v, y, m, d) ->
                        date.setText(String.format(Locale.UK,
                                "%04d-%02d-%02d", y, m + 1, d)),
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    //time
    private void showTimePicker() {
        Calendar c = Calendar.getInstance();
        new TimePickerDialog(this,
                (v, h, m) ->
                        time.setText(String.format(Locale.UK,
                                "%02d:%02d", h, m)),
                c.get(Calendar.HOUR_OF_DAY),
                c.get(Calendar.MINUTE),
                true
        ).show();
    }

    private void saveReservation() {

        //don't allow empty fields
        if (surname.getText().toString().isEmpty()
                || date.getText().toString().isEmpty()
                || time.getText().toString().isEmpty()
                || guests.getText().toString().isEmpty()) {
            return;
        }

        //create new reservation
        Reservation r = new Reservation(
                UserSession.userId,
                surname.getText().toString(),
                date.getText().toString(),
                time.getText().toString(),
                Integer.parseInt(guests.getText().toString())
        );

        //run on separate thread to avoid crashes
        new Thread(() -> {
            AppDatabase.getInstance(this)
                    .reservationDao()
                    .insert(r);
            runOnUiThread(this::finish);
        }).start();
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}