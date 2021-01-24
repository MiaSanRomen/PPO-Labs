package com.example.a3lab_holidays;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText inputUsername, inputPassword;
    Button regBtn, LoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputUsername = findViewById(R.id.username_login);
        inputPassword = findViewById(R.id.password_login);
        regBtn = findViewById(R.id.register_login);
        LoginBtn = findViewById(R.id.log_in_login);

        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser(view);
            }
        });

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }

    public void loginUser(View view) {
        if (!checkUsername() || !checkPassword()) {
            return;
        }
        else {
            isUser();
        }
    }

    private Boolean checkUsername() {
        String val = inputUsername.getText().toString();
        if (val.isEmpty()) {
            inputUsername.setError("Field cannot be empty");
            return false;
        } else {
            inputUsername.setError(null);
            return true;
        }
    }

    private Boolean checkPassword() {
        String val = inputPassword.getText().toString();
        if (val.isEmpty()) {
            inputPassword.setError("Field cannot be empty");
            return false;
        } else {
            inputPassword.setError(null);
            return true;
        }
    }

    private void isUser() {
        final String userEnteredUsername = inputUsername.getText().toString().trim();
        final String userEnteredPassword = inputPassword.getText().toString().trim();
        DatabaseReference reference = FirebaseDatabase.getInstance("https://lab-holidays-default-rtdb.firebaseio.com/").getReference("users");
        Query checkUser = reference.orderByChild("username").equalTo(userEnteredUsername);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    inputUsername.setError(null);
                    String passwordDB = dataSnapshot.child(userEnteredUsername).child("password").getValue(String.class);
                    if (passwordDB.equals(userEnteredPassword)) {
                        inputUsername.setError(null);
                        String usernameDB = dataSnapshot.child(userEnteredUsername).child("username").getValue(String.class);
                        String emailDB = dataSnapshot.child(userEnteredUsername).child("email").getValue(String.class);
                        String victoriesDB = dataSnapshot.child(userEnteredUsername).child("victories").getValue(String.class);
                        String imageDB = dataSnapshot.child(userEnteredUsername).child("imageUrl").getValue(String.class);
                        UserHelper user = new UserHelper(usernameDB, emailDB, passwordDB, victoriesDB, imageDB);
                        Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                        startActivity(intent);
                    } else {
                        inputPassword.setError("Wrong Password");
                        inputPassword.requestFocus();
                    }
                } else {
                    inputUsername.setError("No such User exist");
                    inputUsername.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}