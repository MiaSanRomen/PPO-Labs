package com.example.a3lab_holidays;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Button btn;

        /*mainViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        if (mainViewModel.isUser()){
            Intent intent = new Intent(getApplicationContext(), SignInPage.class);
            startActivity(intent);
        }*/

        btn = findViewById(R.id.btn_profile);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        /*btn = findViewById(R.id.btn_create);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                startActivity(intent);
            }
        });

        btn = findViewById(R.id.btn_join);
        btn.setOnClickListener(v -> showDialog());

        mainViewModel.getIdRoom().observe(this, new Observer<String>() {
            public void onChanged(String s) {
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                intent.putExtra("id_room", s);
                startActivity(intent);
            }
        }); */

        btn = findViewById(R.id.btn_sign_out);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
