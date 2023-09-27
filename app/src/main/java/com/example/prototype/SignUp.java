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

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {
    //create variables
    private TextInputEditText textInputEditTextFullname, textInputEditTextEmail, textInputEditTextUsername, textInputEditTextPassword;
    private String fullname, email, username, password, role;
    private static final String sign_up_url = "http://192.168.220.66/PrototypeApi/signup.php";
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //initialize view variables
        textInputEditTextFullname = findViewById(R.id.etFullname);
        textInputEditTextEmail = findViewById(R.id.etEmail);
        textInputEditTextUsername = findViewById(R.id.etUsername);
        textInputEditTextPassword = findViewById(R.id.etPassword);
        Button buttonSignUp = findViewById(R.id.btnSignUp);
        TextView textViewLogin = findViewById(R.id.tvlogintext);
        radioGroup = findViewById(R.id.radioGroup);

        textViewLogin.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Login.class);
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

        buttonSignUp.setOnClickListener(v -> {
            fullname = String.valueOf(textInputEditTextFullname.getText());
            email = String.valueOf(textInputEditTextEmail.getText());
            username = String.valueOf(textInputEditTextUsername.getText());
            password = String.valueOf(textInputEditTextPassword.getText());

            if (validateInputs()) {
                signup();
            }
        });
    }

    private void signup() {
        //Create new RequestQueue for managing network requests
        RequestQueue queue = Volley.newRequestQueue(this);
        //Create new StringRequest with a request method and response listener
        StringRequest request = new StringRequest(Request.Method.POST, sign_up_url, response -> {
            // Handle the response from the server
            if (response.equals("Sign Up Success")) {
                // Signup success
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                Log.i("Message", response);
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
            else if(response.equals("Username already exists")){
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                textInputEditTextUsername.setError("Try creating another username");
                textInputEditTextUsername.requestFocus();
                Log.i("Message", response);
            }
            else {
                // Signup failed
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                Log.i("Message", response);
            }
        },
                //Error listener
                error -> {
                    // Handle error
                    Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.i("Error", error.getMessage());
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Set the parameters for the POST request
                Map<String, String> params = new HashMap<>();
                params.put("fullname", fullname);
                params.put("email", email);
                params.put("username", username);
                params.put("password", password);
                params.put("role", role);
                return params;
            }
        };

        //Add StringRequest to Request Queue to execute network request
        queue.add(request);
    }

    private boolean validateInputs() {
        if(fullname.isEmpty()) {
            textInputEditTextFullname.setError("Fullname cannot be empty");
            textInputEditTextFullname.requestFocus();
            return false;
        }

        if(email.isEmpty()) {
            textInputEditTextEmail.setError("Email cannot be empty");
            textInputEditTextEmail.requestFocus();
            return false;
        }

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