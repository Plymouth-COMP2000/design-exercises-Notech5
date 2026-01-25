package com.example.restaurantapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.restaurantapp.R;
import com.example.restaurantapp.model.UserSession;
import com.example.restaurantapp.network.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private EditText UnameEditText, passwordEditText;
    private Button loginBtn;

    private Button createBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //register relevant screen elements
        UnameEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);
        createBtn = findViewById(R.id.createUserBtn);

        //call login function when clicked
        loginBtn.setOnClickListener(v -> attemptLogin());
        createBtn.setOnClickListener(v -> createUser());
    }

    private void createUser() {

        startActivity(new Intent(this, CreateUserActivity.class));

    }

    private void attemptLogin() {
        final String usernameInput = UnameEditText.getText().toString().trim();
        final String passwordInput = passwordEditText.getText().toString().trim();

        if (usernameInput.isEmpty() || passwordInput.isEmpty()) {
            Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        //api URL
        String url = "http://10.240.72.69/comp2000/coursework/read_user/"
                + "10951581" + "/" + usernameInput;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {

                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();;

                        //invalid user
                        if (!response.has("user")) {
                            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        JSONObject user = response.getJSONObject("user");
                        String passwordFromApi = user.getString("password");

                        if (passwordFromApi.equals(passwordInput)) {
                            //save user info for session
                            UserSession.userId = usernameInput;
                            UserSession.role = user.getString("usertype");

                            //check role
                            if ("staff".equalsIgnoreCase(UserSession.role)) {

                                //if role is staff
                                startActivity(new Intent(LoginActivity.this, StaffMainActivity.class));
                            } else {

                                //if role is customer
                                startActivity(new Intent(LoginActivity.this, CustomerMainActivity.class));
                            }
                            //close login screen
                            finish();
                        } else {
                            Toast.makeText(this, "Incorrect login information", Toast.LENGTH_SHORT).show();
                        }

                    //debug only
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Login failed: Invalid response", Toast.LENGTH_LONG).show();
                    }

                //volley error
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Login failed: " + error.toString(), Toast.LENGTH_LONG).show();
                }
        );

        //add request to volley queue
        VolleySingleton.getInstance(this).add(request);
    }
}