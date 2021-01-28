package com.example.a3lab_holidays;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.a3lab_holidays.Room.Game;
import com.example.a3lab_holidays.Room.RoomActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MenuActivity extends AppCompatActivity {
    FirebaseDatabase rootNode = FirebaseDatabase.getInstance("https://lab-holidays-default-rtdb.firebaseio.com/");;
    DatabaseReference reference;
    DatabaseReference referenceS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Button btn;

        btn = findViewById(R.id.btn_create);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference = rootNode.getReference("games");
                User user = new User(UserHelper.username, UserHelper.imageUrl);
                Game game = new Game(user);
                reference.child(Game.Id).child("id").setValue(Game.Id);
                reference.child(Game.Id).child("userFirst").setValue(user);
                reference.child(Game.Id).child("userSecond").child("username").setValue("");
                reference.child(Game.Id).child("userSecond").child("imageUrl").setValue("");
                reference.child(Game.Id).child("userSecond").child("ready").setValue("false");
                Intent intent = new Intent(getApplicationContext(), RoomActivity.class);
                startActivity(intent);
            }
        });

        btn = findViewById(R.id.btn_join);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText inputKey = findViewById(R.id.txt_key);
                final String enteredKey = inputKey.getText().toString().trim();
                DatabaseReference reference = FirebaseDatabase.getInstance("https://lab-holidays-default-rtdb.firebaseio.com/").getReference("games");
                Query checkUser = reference.orderByChild("id").equalTo(enteredKey);
                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            inputKey.setError(null);

                            String usernameDB = dataSnapshot.child(enteredKey).child("userFirst").child("username").getValue(String.class);
                            String imageDB = dataSnapshot.child(enteredKey).child("userFirst").child("imageUrl").getValue(String.class);
                            String Id = dataSnapshot.child(enteredKey).child("id").getValue(String.class);
                            User userS = new User(UserHelper.username, UserHelper.imageUrl);
                            User userF = new User(usernameDB, imageDB);
                            referenceS = rootNode.getReference("games");
                            reference.child(enteredKey).child("userSecond").setValue(userS);
                            Game game = new Game(Id, userF, userS);


                            Intent intent = new Intent(getApplicationContext(), RoomActivity.class);
                            startActivity(intent);
                        } else {
                            inputKey.setError("No such room exist");
                            inputKey.requestFocus();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        btn = findViewById(R.id.btn_profile);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });

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
