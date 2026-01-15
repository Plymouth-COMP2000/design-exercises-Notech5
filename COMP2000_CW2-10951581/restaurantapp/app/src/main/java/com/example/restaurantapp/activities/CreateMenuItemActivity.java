package com.example.restaurantapp.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import com.example.restaurantapp.R;
import com.example.restaurantapp.data.AppDatabase;
import com.example.restaurantapp.model.CustomerMenuItem;

public class CreateMenuItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_menu_item);

        //views
        EditText name = findViewById(R.id.name);
        EditText description = findViewById(R.id.description);
        EditText price = findViewById(R.id.price);
        Button save = findViewById(R.id.save);

        //retrieve user inputs
        save.setOnClickListener(v -> {
            CustomerMenuItem item = new CustomerMenuItem();
            item.name = name.getText().toString();
            item.description = description.getText().toString();
            item.price = Double.parseDouble(price.getText().toString());

            //run in background
            new Thread(() -> {

                //retrieve appdatabase instance
                AppDatabase.getInstance(this)
                        .menuDao()

                        //insert into database
                        .insert(item);

                //go back to menuactivity
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