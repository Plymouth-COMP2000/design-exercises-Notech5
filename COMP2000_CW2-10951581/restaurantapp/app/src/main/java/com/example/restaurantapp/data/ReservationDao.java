package com.example.restaurantapp.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.restaurantapp.model.Reservation;

import java.util.List;

@Dao
public interface ReservationDao {

    //get all reservations (staff)
    @Query("SELECT * FROM reservations")
    List<Reservation> getAllReservations();

    //get reservations for a specific user (customers)
    @Query("SELECT * FROM reservations WHERE userId = :userId")
    List<Reservation> getUserReservations(String userId);

    @Query("SELECT * FROM reservations WHERE id = :id")
    Reservation getById(int id);

    //CRUD operations
    @Insert
    void insert(Reservation reservation);

    @Update
    void update(Reservation reservation);

    @Delete
    void delete(Reservation reservation);
}