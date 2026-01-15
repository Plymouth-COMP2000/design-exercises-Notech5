package com.example.restaurantapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.restaurantapp.R;
import com.example.restaurantapp.model.UserSession;

import org.json.JSONException;
import org.json.JSONObject;

public class CreateUserActivity extends AppCompatActivity {
    private static final String BASE_URL = "http://10.240.72.69/comp2000/coursework/create_user/10951581";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Create a user");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        EditText username = findViewById(R.id.username);
        EditText password = findViewById(R.id.password);
        EditText firstname = findViewById(R.id.firstname);
        EditText lastname = findViewById(R.id.lastname);
        EditText email = findViewById(R.id.email);
        EditText contact = findViewById(R.id.contact);
        Button createBtn = findViewById(R.id.createUserBtn);

        createBtn.setOnClickListener(v -> {
            try {
                JSONObject body = new JSONObject();
                body.put("username", username.getText().toString().trim());
                body.put("password", password.getText().toString().trim());
                body.put("firstname", firstname.getText().toString().trim());
                body.put("lastname", lastname.getText().toString().trim());
                body.put("email", email.getText().toString().trim());
                body.put("contact", contact.getText().toString().trim());
                body.put("usertype", "customer"); //only customer profiles can be created

                JsonObjectRequest request = new JsonObjectRequest(
                        Request.Method.POST,
                        BASE_URL,
                        body,
                        response -> {
                            Toast.makeText(this,
                                    "User created successfully",
                                    Toast.LENGTH_LONG).show();
                            finish();
                        },
                        error -> {
                            error.printStackTrace();
                            Toast.makeText(this,
                                    "Failed to create user",
                                    Toast.LENGTH_LONG).show();
                        }
                );

                //add request to queue
                Volley.newRequestQueue(this).add(request);

            //couldn't reach API
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error creating request", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //clear user session
            UserSession.userId = null;
            UserSession.role = null;

            //return to login screen
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}