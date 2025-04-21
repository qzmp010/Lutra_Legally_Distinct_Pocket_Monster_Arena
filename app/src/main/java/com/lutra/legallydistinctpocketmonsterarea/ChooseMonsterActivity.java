package com.lutra.legallydistinctpocketmonsterarea;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.lutra.legallydistinctpocketmonsterarea.database.AppRepository;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.MonsterType;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.User;
import com.lutra.legallydistinctpocketmonsterarea.databinding.ActivityChooseMonsterBinding;

import java.util.ArrayList;

public class ChooseMonsterActivity extends AppCompatActivity {
    private ActivityChooseMonsterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityChooseMonsterBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);

        setContentView(binding.getRoot());
        binding.NORMAL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmMonster("NORMAL");
            }
        });
        binding.ELECTRIC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmMonster("ELECTRIC");
            }
        });
        binding.FIRE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmMonster("FIRE");
            }
        });
        binding.GRASS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmMonster("GRASS");
            }
        });
        binding.WATER.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmMonster("WATER");
            }
        });

    }

    private void confirmMonster(String type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want " + type + "?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                createNickname(type);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Dismiss dialog, so user can make another selection
            }
        });
        builder.show();
    }
    private void createNickname(String type){
        EditText editText = new EditText(this);
        editText.setHint("Nickname:");
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setMessage("Would you like to add a nickname for your " + type + "?");

        alertBuilder.setView(editText);

        alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String input = editText.getText().toString();
                if(input.isEmpty()){
                    Toast.makeText(ChooseMonsterActivity.this, "Nickname cannot be blank", Toast.LENGTH_SHORT).show();
                    createNickname(type);
                    return;
                }
                createNewMonster(input);
            }
        });

        alertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                createNewMonster(type);
            }
        });

        alertBuilder.create().show();
    }
    private void createNewMonster(String input){
      String nickname = input;
      if(nickname.isEmpty()) {
         nickname = "NORMAL";
       }
        Toast.makeText(this, "Monster's nickname is " + input, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(ChooseMonsterActivity.this, LobbyActivity.class);
        startActivity(intent);
    }
    public static Intent intentFactory(Context context) {
        Intent intent = new Intent(context, ChooseMonsterActivity.class);
        return intent;
    }



}
