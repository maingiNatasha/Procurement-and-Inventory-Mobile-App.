package com.example.prototype;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    //create view variables
    TextInputEditText textInputEditTextUsername, textInputEditTextPassword;
    Button buttonLogin;
    TextView textViewSignUp;
    RadioGroup radioGroup;

    private String username, password, role;
    private int supplierId, farmerId;
    private SessionManager sessionManager;

    //url
    public static final String login_url = "http://192.168.220.66/PrototypeApi/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //initialize view variables
        textInputEditTextUsername = findViewById(R.id.etUsername);
        textInputEditTextPassword = findViewById(R.id.etPassword);
        buttonLogin = findViewById(R.id.btnLogin);
        textViewSignUp = findViewById(R.id.tvsignuptext);
        radioGroup = findViewById(R.id.radioGroup);
        sessionManager = new SessionManager(getApplicationContext());

        textViewSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SignUp.class);
            startActivity(intent);
            finish();
        });

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton radioButton1 = findViewById(R.id.rBtn_1);
            RadioButton radioButton2 = findViewById(R.id.rBtn_2);

            if (checkedId == radioButton1.getId()) {
                role = radioButton1.getText().toString();
            }
            else if (checkedId == radioButton2.getId()) {
                role = radioButton2.getText().toString();
            }
        });

        buttonLogin.setOnClickListener(v -> {
            username = String.valueOf(textInputEditTextUsername.getText());
            password = String.valueOf(textInputEditTextPassword.getText());

            if (validateInputs()) {
                login();
            }

        });
    }

    private void login() {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, login_url, response -> {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                String status = jsonResponse.getString("status");

                if (status.equals("success")) {
                    // Login success
                    if (role.equals("Supplier")) {
                        supplierId = jsonResponse.getInt("supplierId");
                        sessionManager.saveSupplierId(supplierId);
                    } else if (role.equals("Farmer")) {
                        farmerId = jsonResponse.getInt("farmerId");
                        sessionManager.saveFarmerId(farmerId);
                    }

                    String username = jsonResponse.getString("username");
                    String fullname = jsonResponse.getString("fullname");
                    String email = jsonResponse.getString("email");

                    sessionManager.saveUsername(username);
                    sessionManager.saveFullname(fullname);
                    sessionManager.saveEmail(email);

                    Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show();
                    Log.i("Message", "Login Success");

                    Intent intent;
                    if (role.equals("Farmer")) {
                        intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (role.equals("Supplier")) {
                        intent = new Intent(getApplicationContext(), SupplierHomePage.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    // Login failed
                    String message = jsonResponse.getString("message");
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    Log.i("Message", message);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.i("Error", "Error parsing JSON response");
            }
        },
                error -> {
                    // Handle error
                    String errorMessage = error.getMessage();
                    Toast.makeText(getApplicationContext(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                    if (errorMessage != null) {
                        Log.i("Error", errorMessage);
                    } else {
                        Log.i("Error", "Unknown error occurred");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Set the parameters for the POST request
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                params.put("role", role);
                return params;
            }
        };

        queue.add(request);
    }

    private boolean validateInputs() {
        if(username.isEmpty()) {
            textInputEditTextUsername.setError("Username cannot be empty");
            textInputEditTextUsername.requestFocus();
            return false;
        }

        if(password.isEmpty()) {
            textInputEditTextPassword.setError("Password cannot be empty");
            textInputEditTextPassword.requestFocus();
            return false;
        }

        int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
        if (selectedRadioButtonId == -1) {
            // No radio button is selected
            Toast.makeText(getApplicationContext(), "Please select an option; Farmer or Supplier?", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}