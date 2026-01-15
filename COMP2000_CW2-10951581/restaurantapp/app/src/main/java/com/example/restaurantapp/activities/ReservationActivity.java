package com.example.restaurantapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantapp.R;
import com.example.restaurantapp.adapter.ReservationAdapter;
import com.example.restaurantapp.data.AppDatabase;
import com.example.restaurantapp.model.Reservation;
import com.example.restaurantapp.model.UserSession;

import java.util.List;

public class ReservationActivity extends AppCompatActivity {

    private RecyclerView recycler;
    private Button createBtn;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservations);



        //toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //retrieve database instance
        db = AppDatabase.getInstance(this);

        //retrieve recycler object
        recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        //add reservation button
        createBtn = findViewById(R.id.createReservationBtn);

        //limited to customers
        if (!"staff".equals(UserSession.role)) {
            createBtn.setVisibility(View.VISIBLE);
            createBtn.setOnClickListener(v ->
                    startActivity(new Intent(this, CreateReservationActivity.class)));
        } else {

            //staff don't create reservations
            createBtn.setVisibility(View.GONE);
        }

        //load reservations into recyclerview
        loadReservations();
    }

    //function to load reservations
    private void loadReservations() {

        //retrieve reservations
        List<Reservation> reservations;
        if ("staff".equals(UserSession.role)) {
            reservations = db.reservationDao().getAllReservations();
        } else {
            reservations = db.reservationDao().getUserReservations(UserSession.userId);
        }

        //add reservations to recyclerview
        ReservationAdapter adapter = new ReservationAdapter(reservations);
        recycler.setAdapter(adapter);
    }

    //automatically refresh reservations list when user returns to reservation screen
    @Override
    protected void onResume() {
        super.onResume();
        loadReservations();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}