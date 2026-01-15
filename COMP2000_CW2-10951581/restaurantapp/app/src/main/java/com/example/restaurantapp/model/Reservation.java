package com.example.restaurantapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

//database table
@Entity(tableName = "reservations")
public class Reservation {

    //fields
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String userId;

    public String surname;
    public String date;
    public String time;
    public int guests;

    //no arguments constructor
    public Reservation() {}

    //constructor
    public Reservation(String userId, String surname, String date, String time, int guests) {
        this.userId = userId;
        this.surname = surname;
        this.date = date;
        this.time = time;
        this.guests = guests;
    }

    //getters
    public int getId() { return id; }
    public String getUserId() { return userId; }
    public String getSurname() { return surname; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public int getGuests() { return guests; }

    //setters
    public void setId(int id) { this.id = id; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setSurname(String surname) { this.surname = surname; }
    public void setDate(String date) { this.date = date; }
    public void setTime(String time) { this.time = time; }
    public void setGuests(int guests) { this.guests = guests; }
}