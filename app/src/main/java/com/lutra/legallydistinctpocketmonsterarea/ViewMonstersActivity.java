package com.lutra.legallydistinctpocketmonsterarea;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.UserMonster;
import com.lutra.legallydistinctpocketmonsterarea.databinding.ActivityViewMonstersBinding;
import com.lutra.legallydistinctpocketmonsterarea.viewHolders.MonsterAdapter;
import com.lutra.legallydistinctpocketmonsterarea.viewHolders.MonsterAdapter.UserMonsterDiff;
import com.lutra.legallydistinctpocketmonsterarea.viewHolders.MonsterViewModel;

public class ViewMonstersActivity extends AppCompatActivity {

  private int loggedInUserId;
  private ActivityViewMonstersBinding binding;
  private MonsterViewModel monsterViewModel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityViewMonstersBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    loggedInUserId = getIntent().getIntExtra(LobbyActivity.LOBBY_USER_ID, -1);

    monsterViewModel = new ViewModelProvider(this).get(MonsterViewModel.class);

    RecyclerView recyclerView = binding.viewMonstersRecyclerView;
    final MonsterAdapter adapter = new MonsterAdapter(new UserMonsterDiff());

    adapter.setOnClickListener(new MonsterAdapter.OnClickListener() {
      @Override
      public void onClick(UserMonster monster) {
        Toast.makeText(getApplicationContext(), monster.getPhrase(), Toast.LENGTH_SHORT).show();
      }
    });

    recyclerView.setAdapter(adapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));

    monsterViewModel.getUserMonstersWithTypeListByUserIdLiveData(loggedInUserId)
        .observe(this, adapter::submitList);

    binding.returnButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = LobbyActivity.intentFactory(getApplicationContext(), loggedInUserId);
        startActivity(intent);
      }
    });
  }

  public static Intent intentFactory(Context context, int loggedInUserId) {
    Intent intent = new Intent(context, ViewMonstersActivity.class);
    intent.putExtra(LobbyActivity.LOBBY_USER_ID, loggedInUserId);
    return intent;
  }
}