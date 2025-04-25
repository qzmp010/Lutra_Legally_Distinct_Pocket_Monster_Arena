package com.lutra.legallydistinctpocketmonsterarea;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lutra.legallydistinctpocketmonsterarea.database.AppRepository;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.UserMonster;

import com.lutra.legallydistinctpocketmonsterarea.viewHolders.MonsterAdapter;
import com.lutra.legallydistinctpocketmonsterarea.viewHolders.MonsterViewModel;

import com.lutra.legallydistinctpocketmonsterarea.databinding.ActivityEditMonstersBinding;


public class EditMonstersActivity extends AppCompatActivity {

    int loggedInUserId;
    private ActivityEditMonstersBinding binding;
    private AppRepository repository;
    private MonsterViewModel monsterViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditMonstersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loggedInUserId = getIntent().getIntExtra(LobbyActivity.LOBBY_USER_ID, -1);

        monsterViewModel = new ViewModelProvider(this).get(MonsterViewModel.class);
        RecyclerView recyclerView = binding.EditMonstersRecyclerView;

        final MonsterAdapter adapter = new MonsterAdapter(new MonsterAdapter.UserMonsterDiff());

        adapter.setOnClickListener(new MonsterAdapter.OnClickListener() {
            @Override
            public void onClick(UserMonster monster) {
                editNickname(monster);
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        repository = AppRepository.getRepository(getApplication());

        monsterViewModel.getUserMonstersWithTypeListByUserIdLiveData(loggedInUserId)
            .observe(this, adapter::submitList);
    }

    public void editNickname(UserMonster monster){
        EditText editText = new EditText(this);

        editText.setHint("Nickname: ");
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("Edit monster nickname");
        alertBuilder.setView(editText);

        alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String input = editText.getText().toString();
                if(input.isEmpty()){
                    Toast.makeText(EditMonstersActivity.this, "Nickname cannot be blank", Toast.LENGTH_SHORT).show();
                    return;
                }
                monster.setNickname(input);
                Toast.makeText(EditMonstersActivity.this, "Nickname updated to "+ input + "!", Toast.LENGTH_SHORT).show();
                repository.insertUserMonster(monster);

            }
        });

        alertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //dismiss
            }
        });

        alertBuilder.create().show();
    }


    public static Intent intentFactory(Context context, int loggedInUserId) {
        Intent intent = new Intent(context, EditMonstersActivity.class);
        intent.putExtra(LobbyActivity.LOBBY_USER_ID, loggedInUserId);
        return intent;
    }
}
