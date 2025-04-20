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

  private ActivityViewMonstersBinding binding;
  private AppRepository repository;
  private MonsterViewModel monsterViewModel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityViewMonstersBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    monsterViewModel = new ViewModelProvider(this).get(MonsterViewModel.class);

    RecyclerView recyclerView = binding.viewMonstersRecyclerView;
    final MonsterAdapter adapter = new MonsterAdapter(new UserMonsterDiff());
    recyclerView.setAdapter(adapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));

    repository = AppRepository.getRepository(getApplication());

    //todo: get data for specific userId
    //todo: separate each entry by UserMonster
    monsterViewModel.getMonsterTypesWithUserMonstersLiveData().observe(this, adapter::submitList);
  }

  public static Intent intentFactory(Context context) {
    Intent intent = new Intent(context, ViewMonstersActivity.class);
    return intent;
  }
}