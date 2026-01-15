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
import com.example.restaurantapp.adapter.MenuAdapter;
import com.example.restaurantapp.data.AppDatabase;
import com.example.restaurantapp.model.CustomerMenuItem;
import com.example.restaurantapp.model.UserSession;

import java.util.List;

public class MenuActivity extends AppCompatActivity {

    private MenuAdapter menuAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Menu");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //when clicked sends user to create menu item activity
        Button addMenuItem = findViewById(R.id.addMenuItem);

        //user is staff
        if ("staff".equals(UserSession.role)) {

            //allow staff to add menu items
            addMenuItem.setVisibility(View.VISIBLE);

            //send staff to create menu item activity when clicked
            addMenuItem.setOnClickListener(v ->
                    startActivity(new Intent(this, CreateMenuItemActivity.class))
            );

        //user is customer
        } else {

            //hide add menu button
            addMenuItem.setVisibility(View.GONE);
        }

        //recyclerview
        RecyclerView recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        List<CustomerMenuItem> items = AppDatabase.getInstance(this).menuDao().getAll();
        menuAdapter = new MenuAdapter(this, items, "staff".equals(UserSession.role));
        recycler.setAdapter(menuAdapter);
    }

    //call function to display items when activity is returned to
    @Override
    protected void onResume() {
        super.onResume();
        loadMenuItems();
    }

    //display menu items on screen
    private void loadMenuItems() {
        List<CustomerMenuItem> items = AppDatabase.getInstance(this).menuDao().getAll();
        menuAdapter.updateMenu(items);
    }

    //toolbar back button functionality
    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}