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
import com.example.restaurantapp.util.NotificationStore;

import java.util.Calendar;

public class EditReservationActivity extends AppCompatActivity {

    private Reservation reservation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_reservation);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Edit reservation");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        int id = getIntent().getIntExtra("id", -1);

        EditText surname = findViewById(R.id.surname);
        EditText date = findViewById(R.id.date);
        EditText time = findViewById(R.id.time);
        EditText guests = findViewById(R.id.guests);
        Button save = findViewById(R.id.saveReservationBtn);

        // Disable manual typing
        date.setFocusable(false);
        time.setFocusable(false);

        //run in background
        new Thread(() -> {

            //retrieve reservations
            reservation = AppDatabase.getInstance(this)
                    .reservationDao()
                    .getById(id);

            //display current values
            runOnUiThread(() -> {
                surname.setText(reservation.surname);
                date.setText(reservation.date);
                time.setText(reservation.time);
                guests.setText(String.valueOf(reservation.guests));
            });
        }).start();

        //enforce valid inputs

        //date
        date.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new DatePickerDialog(this,
                    (view, y, m, d) ->
                            date.setText(String.format("%04d-%02d-%02d", y, m + 1, d)),
                    c.get(Calendar.YEAR),
                    c.get(Calendar.MONTH),
                    c.get(Calendar.DAY_OF_MONTH)
            ).show();
        });

        //time
        time.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new TimePickerDialog(this,
                    (view, h, m) ->
                            time.setText(String.format("%02d:%02d", h, m)),
                    c.get(Calendar.HOUR_OF_DAY),
                    c.get(Calendar.MINUTE),
                    true
            ).show();
        });

        //save changes
        save.setOnClickListener(v -> {
            reservation.surname = surname.getText().toString();
            reservation.date = date.getText().toString();
            reservation.time = time.getText().toString();
            reservation.guests = Integer.parseInt(guests.getText().toString());

            new Thread(() -> {
                AppDatabase db = AppDatabase.getInstance(this);

                db.reservationDao().update(reservation);

                if ("staff".equals(UserSession.role)) {

                    String customerUserId = reservation.userId;

                    NotificationStore.add(
                            this,
                            customerUserId,
                            "Your reservation was updated by staff."
                    );
                }


                //return to reservations screen
                runOnUiThread(this::finish);
            }).start();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}