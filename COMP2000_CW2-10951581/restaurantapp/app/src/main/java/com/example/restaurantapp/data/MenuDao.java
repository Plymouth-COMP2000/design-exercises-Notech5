package com.example.restaurantapp.data;

//room database
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import com.example.restaurantapp.model.CustomerMenuItem;
import java.util.List;

@Dao
public interface MenuDao {
    @Query("SELECT * FROM menu")
    List<CustomerMenuItem> getAll();

    @Insert
    void insert(CustomerMenuItem item);

    @Update
    void update(CustomerMenuItem item);

    @Delete
    void delete(CustomerMenuItem item);

    //allows passing specific menu item to other intents
    @Query("SELECT * FROM menu WHERE id = :id LIMIT 1")
    CustomerMenuItem getById(int id);

}