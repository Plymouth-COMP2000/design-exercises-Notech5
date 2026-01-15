package com.example.restaurantapp.data;
import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.restaurantapp.model.CustomerMenuItem;
import com.example.restaurantapp.model.Reservation;


//set up app database
@Database(entities = {CustomerMenuItem.class, Reservation.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    //get DAOs
    public abstract MenuDao menuDao();
    public abstract ReservationDao reservationDao();

    //gets database
    public static synchronized AppDatabase getInstance(Context context) {

        //checks if there is an existing database instance
        if (instance == null) {

            //instantiates database
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "restaurant_db")
                    .allowMainThreadQueries()
                    .build();
        }
        //return database instance
        return instance;
    }
}