package com.example.restaurantapp.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.restaurantapp.R;
import com.example.restaurantapp.data.AppDatabase;
import com.example.restaurantapp.model.CustomerMenuItem;

public class ViewMenuItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_menu_item);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Menu");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        int menuId = getIntent().getIntExtra("menuId", -1);
        CustomerMenuItem item = AppDatabase.getInstance(this).menuDao().getById(menuId);

        TextView name = findViewById(R.id.menuName);
        TextView price = findViewById(R.id.menuPrice);
        TextView description = findViewById(R.id.menuDescription);

        if (item != null) {
            name.setText(item.name);
            price.setText(String.format("$%.2f", item.price));
            description.setText(item.description);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}