package com.lutra.legallydistinctpocketmonsterarea;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.lutra.legallydistinctpocketmonsterarea.database.AppRepository;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.MonsterType;
import com.lutra.legallydistinctpocketmonsterarea.databinding.ActivityAdminSelectMonsterTypeBinding;
import com.lutra.legallydistinctpocketmonsterarea.viewHolders.AdminMonsterTypeAdapter;
import com.lutra.legallydistinctpocketmonsterarea.viewHolders.AdminMonsterTypeAdapter.MonsterTypeDiff;
import com.lutra.legallydistinctpocketmonsterarea.viewHolders.AdminMonsterTypeAdapter.OnClickListener;
import com.lutra.legallydistinctpocketmonsterarea.viewHolders.AdminMonsterTypeViewModel;

public class AdminSelectMonsterTypeActivity extends AppCompatActivity {

  private int loggedInUserId;
  private ActivityAdminSelectMonsterTypeBinding binding;
  private AppRepository repository;
  private AdminMonsterTypeViewModel adminMonsterTypeViewModel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityAdminSelectMonsterTypeBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    loggedInUserId = getIntent().getIntExtra(LobbyActivity.LOBBY_USER_ID, -1);

    adminMonsterTypeViewModel = new ViewModelProvider(this).get(AdminMonsterTypeViewModel.class);

    RecyclerView recyclerView = binding.selectMonsterTypeRecyclerView;
    final AdminMonsterTypeAdapter adapter = new AdminMonsterTypeAdapter(new MonsterTypeDiff());

    adapter.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(MonsterType monsterType) {
        Intent intent = AdminCreateMonsterActivity.intentFactory(getApplicationContext(),
            loggedInUserId,
            monsterType.getMonsterTypeId());
        startActivity(intent);
      }
    });

    recyclerView.setAdapter(adapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));

    repository = AppRepository.getRepository(getApplication());

    adminMonsterTypeViewModel.getMonsterTypesLiveData().observe(this, adapter::submitList);

    binding.adminSelectMonsterTypeReturnButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = AdminLobbyActivity.intentFactory(getApplicationContext(), loggedInUserId);
        startActivity(intent);
      }
    });
  }

  public static Intent intentFactory(Context context, int loggedInUserId) {
    Intent intent = new Intent(context, AdminSelectMonsterTypeActivity.class);
    intent.putExtra(LobbyActivity.LOBBY_USER_ID, loggedInUserId);
    return intent;
  }
}