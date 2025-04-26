package com.lutra.legallydistinctpocketmonsterarea;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.lutra.legallydistinctpocketmonsterarea.database.AppRepository;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.UserMonster;
import com.lutra.legallydistinctpocketmonsterarea.databinding.ActivitySwitchMonsterBinding;
import com.lutra.legallydistinctpocketmonsterarea.viewHolders.SwitchMonsterAdapter;
import com.lutra.legallydistinctpocketmonsterarea.viewHolders.SwitchMonsterViewModel;

import java.util.ArrayList;

/**
 * SwitchMonsterActivity - relies heavily on the template set in ViewMonsterActivity (all credit to
 * Sunny for the format of the adapter, view holder, and view model) but with a couple modifications.
 * Displays all monsters that belong to the current user in a recycler view that contains their name
 * and stats. The goal of this activity is to choose a monster to battle with. On item click (code
 * set in SwitchMonsterViewHolder), user confirms choice and returns to battle with the selected monster.
 * Monsters can not be selected for battle if they have an HP of zero or less. Users have the option
 * to return to the lobby directly from this activity. Future improvements could include health restoration
 * on exit or storing of enemy data so the enemy can remain on screen if monster is switched.
 * @author David Rosenfeld
 */
public class SwitchMonsterActivity extends AppCompatActivity {

    private ActivitySwitchMonsterBinding binding;
    private SwitchMonsterViewModel monsterViewModel;

    public static final String MONSTER_ID = "SwitchMonsterActivity.MONSTER_ID";
    public static final String USER_ID = "SwitchMonsterActivity.USER_ID";
    public static final String TAG = "SwitchMonsterActivity.java";

    private int loggedInUser = -1;
    private boolean isAdmin;
    private AppRepository repository;

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

        monsterViewModel.getUserMonstersByUserIdLiveData(loggedInUser).observe(
                this,monsters -> {adapter.submitList(monsters);});

        binding.returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<UserMonster> userMonsters = new ArrayList<>();
                while(userMonsters.isEmpty()) {
                    try {
                        userMonsters = repository.getAllUserMonsters();
                        for(UserMonster monster : userMonsters) {
                            monster.setCurrentHealth(monster.getMaxHealth());
                            repository.insertUserMonster(monster);
                        }
                    } catch (RuntimeException e) {
                        Log.e(TAG, "Couldn't restore monster health.");
                    }
                }
                backToLobby();
            }
        });

        repository = AppRepository.getRepository(getApplication());
        if (repository != null) {
            repository.getUserByUserId(loggedInUser).observe(this, user -> {
                isAdmin = user.isAdmin();
            });
        }
    }

    /**
     * Checks extras tagged with any of the activities that interact with SwitchMonsterActivity to see
     * if they contain the ID of the logged-in user. If not, redirects back to LoginActivity.
     */
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

    private void backToLobby() {
        Intent intent = isAdmin
            ? AdminLobbyActivity.intentFactory(getApplicationContext(), loggedInUser)
            : LobbyActivity.intentFactory(getApplicationContext(), loggedInUser);
        intent.putExtra(USER_ID, loggedInUser);
        startActivity(intent);
    }

    public static Intent intentFactory(Context context) {
        Intent intent = new Intent(context, SwitchMonsterActivity.class);
        return intent;
    }
}