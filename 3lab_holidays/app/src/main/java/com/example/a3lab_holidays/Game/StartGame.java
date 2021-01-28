package com.example.a3lab_holidays.Game;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.a3lab_holidays.R;
import com.example.a3lab_holidays.Room.Game;
import com.example.a3lab_holidays.Room.UserEnum;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StartGame extends AppCompatActivity implements View.OnClickListener {

    private Button[][] buttons = new Button[8][8];
    Button buttonGo, buttonHuge, buttonMedium, buttonSmall;
    ShipEnum selectedShip;
    int smallShips = 3, mediumShips = 2, hugeShips = 1;
    FirebaseDatabase rootNode = FirebaseDatabase.getInstance("https://lab-holidays-default-rtdb.firebaseio.com/");;

    DatabaseReference reference = rootNode.getReference("games");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepare);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                String buttonID = "button_" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(this);
                buttons[i][j].setBackgroundColor(Color.BLUE);
                buttons[i][j].setText(buttonID);
                buttons[i][j].setTextColor(Color.BLUE);
                Game.fieldMy[i][j] = false;
            }
        }

        buttonSmall = findViewById(R.id.small_ship);
        buttonSmall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedShip = ShipEnum.SMALL;
                buttonMedium.setEnabled(false);
                buttonHuge.setEnabled(false);
            }
        });

        buttonMedium = findViewById(R.id.medium_ship);
        buttonMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedShip = ShipEnum.MEDIUM;
                buttonHuge.setEnabled(false);
                buttonSmall.setEnabled(false);

            }
        });

        buttonHuge = findViewById(R.id.huge_ship);
        buttonHuge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedShip = ShipEnum.HUGE;
                buttonMedium.setEnabled(false);
                buttonSmall.setEnabled(false);

            }
        });

        buttonGo = findViewById(R.id.btn_go);
        buttonGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Game.isReadyMe = !Game.isReadyMe;
                String ready;
                if(Game.isReadyMe){
                    ready = "Wait your enemy";
                    buttonGo.setText(ready);
                    ready = "true";
                }else {
                    ready = "Go!";
                    buttonGo.setText(ready);
                    ready = "false new";
                }

                reference.child(Game.Id).child(Game.myUserPath).child("ready").setValue(ready);
                if(Game.isReadyMe && Game.isReadySecond){
                    Game.isReadyMe = false;
                    Game.isReadySecond = false;
                    reference.child(Game.Id).child(Game.myUserPath).child("ready").setValue("false");
                    Intent intent = new Intent(getApplicationContext(), BattleActivity.class);
                    startActivity(intent);
                }
            }
        });
        checkShips();
        ReadyEventListener();
    }

    @Override
    public void onClick(View v) {
        if (((Button) v).getText().toString().equals("")) {
            return;
        }

        if (selectedShip == ShipEnum.NOTHING) {
            return;
        } else if(selectedShip == ShipEnum.SMALL) {
            placeShip(((Button) v).getText().toString(), 2);

        } else if(selectedShip == ShipEnum.MEDIUM) {
            placeShip(((Button) v).getText().toString(), 3);
        } else {
            placeShip(((Button) v).getText().toString(), 4);
        }
        checkShips();
    }

    public void checkShips(){
        buttonHuge.setEnabled(hugeShips != 0);
        buttonMedium.setEnabled(mediumShips != 0);
        buttonSmall.setEnabled(smallShips != 0);
        buttonGo.setEnabled(smallShips == 0 && mediumShips == 0 && hugeShips == 0);
        selectedShip = ShipEnum.NOTHING;
        String btnText = "Small: " + smallShips;
        buttonSmall.setText(btnText);
        btnText = "Medium: " + mediumShips;
        buttonMedium.setText(btnText);
        btnText = "Huge: " + hugeShips;
        buttonHuge.setText(btnText);
    }

    public void checkField(){
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if(Game.fieldMy[i][j]) {
                    buttons[i][j].setBackgroundColor(Color.BLUE);
                }
                else {
                    buttons[i][j].setBackgroundColor(Color.BLUE);
                }
            }
        }
    }

    public void placeShip(String buttonName, int size){
        String subStr = buttonName.substring(7, 9);
        char c = subStr.charAt(0);
        int Y = Character.getNumericValue(c);
        c = subStr.charAt(1);
        int X = Character.getNumericValue(c);
        for (int i=0; i<size; i++){
            if (Game.fieldMy[Y][X] != Game.fieldMy[Y][X + i]  || X+size>8) {
                return;
            }
        }
        if(selectedShip == ShipEnum.MEDIUM)
        {
            mediumShips--;
        }else if (selectedShip == ShipEnum.HUGE){
            hugeShips--;
        }else {
            smallShips--;
        }
        for (int i=0; i<size; i++){
            Game.fieldMy[Y][X+i] = true;
            buttons[Y][X+i].setBackgroundColor(Color.GRAY);
            buttons[Y][X+i].setTextColor(Color.GRAY);
            String yx = String.valueOf(Y) + String.valueOf(X+i);
            reference.child(Game.Id).child(Game.myUserPath).child("field").child(yx).setValue("true");
        }
    }

    public void ReadyEventListener() {
        DatabaseReference userRef = rootNode.getReference("games/" + Game.Id + "/" + Game.userPath + "/ready");
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue(String.class).equals("true")){
                    Game.isReadySecond = true;
                }else if(snapshot.getValue(String.class).equals("false new")){
                    Game.isReadySecond = false;
                }else {
                    Game.isReadyMe = false;
                    Game.isReadySecond = false;
                    reference.child(Game.Id).child(Game.myUserPath).child("ready").setValue("false");
                    Intent intent = new Intent(getApplicationContext(), BattleActivity.class);
                    startActivity(intent);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
