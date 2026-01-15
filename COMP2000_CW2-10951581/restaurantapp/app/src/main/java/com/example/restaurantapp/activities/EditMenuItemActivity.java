package com.example.restaurantapp.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.restaurantapp.R;
import com.example.restaurantapp.data.AppDatabase;
import com.example.restaurantapp.model.CustomerMenuItem;

public class EditMenuItemActivity extends AppCompatActivity {

    private EditText nameInput, priceInput, descriptionInput;
    private Button saveButton;

    private int menuId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_menu_item);

        //current intent menu item
        menuId = getIntent().getIntExtra("menuId", -1);
        if (menuId == -1) {
            Toast.makeText(this, "Invalid menu item", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        //input fields
        nameInput = findViewById(R.id.editName);
        priceInput = findViewById(R.id.editPrice);
        descriptionInput = findViewById(R.id.editDescription);
        saveButton = findViewById(R.id.saveButton);

        //run on background thread to avoid freezing
        new Thread(() -> {

            //load database item
            CustomerMenuItem item = AppDatabase.getInstance(this).menuDao().getById(menuId);

            //main thread
            runOnUiThread(() -> {
                if (item != null) {
                    nameInput.setText(item.name);
                    priceInput.setText(String.valueOf(item.price));
                    descriptionInput.setText(item.description);
                }
            });

            //savebutton listener
            saveButton.setOnClickListener(v -> {

                //retrieve user inputs
                String newName = nameInput.getText().toString().trim();
                String newPriceStr = priceInput.getText().toString().trim();
                String newDescription = descriptionInput.getText().toString().trim();

                if (newName.isEmpty() || newPriceStr.isEmpty() || newDescription.isEmpty()) {
                    Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                //convert user input from string to double (allow for non-decimal inputs)
                double newPrice;
                try {
                    newPrice = Double.parseDouble(newPriceStr);
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Invalid price", Toast.LENGTH_SHORT).show();
                    return;
                }

                //replace item values
                item.name = newName;
                item.price = newPrice;
                item.description = newDescription;

                //run in background
                new Thread(() -> {

                    //get menu item and update it
                    AppDatabase.getInstance(this).menuDao().update(item);

                    //show toast
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Menu item updated", Toast.LENGTH_SHORT).show();

                        //go back to menuactivity
                        finish();
                    });
                }).start();
            });
        }).start();
    }
}