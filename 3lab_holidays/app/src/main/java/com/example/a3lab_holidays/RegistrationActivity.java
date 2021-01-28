package com.example.a3lab_holidays;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegistrationActivity extends AppCompatActivity {
    EditText inputUsername, inputEmail, inputPassword;
    Button regBtn, regToLoginBtn;
    FirebaseDatabase rootNode = FirebaseDatabase.getInstance("https://lab-holidays-default-rtdb.firebaseio.com/");;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        inputUsername = findViewById(R.id.username);
        inputEmail = findViewById(R.id.email);
        inputPassword = findViewById(R.id.password);
        regBtn = findViewById(R.id.register);
        regToLoginBtn = findViewById(R.id.log_in);

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("users");

                String username = inputUsername.getText().toString();
                String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();
                UserHelper helperClass = new UserHelper(username, email, password);
                reference.child(username).setValue(helperClass);

                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(intent);
            }
        });

        regToLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
