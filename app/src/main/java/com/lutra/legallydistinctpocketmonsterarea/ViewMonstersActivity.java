package com.lutra.legallydistinctpocketmonsterarea;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.lutra.legallydistinctpocketmonsterarea.database.AppRepository;
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
    recyclerView.setAdapter(adapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));

    monsterViewModel.getUserMonstersWithTypeListByUserIdLiveData(loggedInUserId)
        .observe(this, adapter::submitList);
  }

  public static Intent intentFactory(Context context, int loggedInUserId) {
    Intent intent = new Intent(context, ViewMonstersActivity.class);
    intent.putExtra(LobbyActivity.LOBBY_USER_ID, loggedInUserId);
    return intent;
  }
}