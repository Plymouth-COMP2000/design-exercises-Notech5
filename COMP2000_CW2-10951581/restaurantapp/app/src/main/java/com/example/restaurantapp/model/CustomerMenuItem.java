package com.example.restaurantapp.model;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "menu")
public class CustomerMenuItem {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public String description;
    public double price;
}