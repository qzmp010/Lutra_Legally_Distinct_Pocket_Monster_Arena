package com.lutra.legallydistinctpocketmonsterarea;

import static android.widget.Toast.LENGTH_SHORT;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lutra.legallydistinctpocketmonsterarea.database.AppRepository;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.UserMonster;
import com.lutra.legallydistinctpocketmonsterarea.databinding.ActivitySwitchMonsterBinding;
import com.lutra.legallydistinctpocketmonsterarea.viewHolders.MonsterAdapter;
import com.lutra.legallydistinctpocketmonsterarea.viewHolders.MonsterViewModel;
import com.lutra.legallydistinctpocketmonsterarea.viewHolders.SwitchMonsterAdapter;
import com.lutra.legallydistinctpocketmonsterarea.viewHolders.SwitchMonsterViewHolder;
import com.lutra.legallydistinctpocketmonsterarea.viewHolders.SwitchMonsterViewModel;

import java.util.ArrayList;
import java.util.List;

public class SwitchMonsterActivity extends AppCompatActivity {

    private ActivitySwitchMonsterBinding binding;
    private AppRepository repository;
    private SwitchMonsterViewModel monsterViewModel;

    public static final String MONSTER_ID = "SwitchMonsterActivity.MONSTER_ID";
    public static final String USER_ID = "SwitchMonsterActivity.USER_ID";

    private int loggedInUser = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySwitchMonsterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginUser();

        monsterViewModel = new ViewModelProvider(this).get(SwitchMonsterViewModel.class);

        RecyclerView recyclerView = binding.switchMonsterRecyclerView;
        final SwitchMonsterAdapter adapter = new SwitchMonsterAdapter(new SwitchMonsterAdapter.UserMonsterDiff());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        repository = AppRepository.getRepository(getApplication());

        //todo: get data for specific userId
        //todo: separate each entry by UserMonster
        monsterViewModel.getUserMonstersByUserIdLiveData(loggedInUser).observe(
                this,monsters -> {adapter.submitList(monsters);});

        binding.returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = LobbyActivity.intentFactory(getApplicationContext());
                intent.putExtra(USER_ID, loggedInUser);
                startActivity(intent);
            }
        });

    }

    private void loginUser() {
        if(loggedInUser == -1) {
            loggedInUser = getIntent().getIntExtra(BattleActivity.USER_ID, -1);
        }

        if(loggedInUser == -1) {
            loggedInUser = getIntent().getIntExtra(LobbyActivity.LOBBY_USER_ID, -1);
        }

        if(loggedInUser == -1) {
            loggedInUser = getIntent().getIntExtra(CaptureActivity.USER_ID, -1);
        }

        if(loggedInUser == -1) {
            Intent intent = LoginActivity.loginIntentFactory(getApplicationContext());
            startActivity(intent);
        }
    }

    public static Intent intentFactory(Context context) {
        Intent intent = new Intent(context, SwitchMonsterActivity.class);
        return intent;
    }
}