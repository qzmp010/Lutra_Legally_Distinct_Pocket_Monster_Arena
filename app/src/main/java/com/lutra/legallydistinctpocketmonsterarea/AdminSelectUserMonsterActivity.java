package com.lutra.legallydistinctpocketmonsterarea;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.lutra.legallydistinctpocketmonsterarea.database.AppRepository;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.MonsterType;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.User;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.UserMonster;
import com.lutra.legallydistinctpocketmonsterarea.databinding.ActivityAdminSelectMonsterTypeBinding;
import com.lutra.legallydistinctpocketmonsterarea.databinding.ActivityAdminSelectUserMonsterBinding;
import com.lutra.legallydistinctpocketmonsterarea.viewHolders.AdminMonsterTypeViewModel;
import com.lutra.legallydistinctpocketmonsterarea.viewHolders.AdminUserMonsterAdapter;
import com.lutra.legallydistinctpocketmonsterarea.viewHolders.AdminUserMonsterAdapter.OnClickListener;
import com.lutra.legallydistinctpocketmonsterarea.viewHolders.AdminUserMonsterAdapter.UserMonsterDiff;
import com.lutra.legallydistinctpocketmonsterarea.viewHolders.AdminUserMonsterViewModel;
import kotlin.Triple;

public class AdminSelectUserMonsterActivity extends AppCompatActivity {

  private int loggedInUserId;
  private ActivityAdminSelectUserMonsterBinding binding;
  private AdminUserMonsterViewModel adminUserMonsterViewModel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityAdminSelectUserMonsterBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    loggedInUserId = getIntent().getIntExtra(LobbyActivity.LOBBY_USER_ID, -1);

    adminUserMonsterViewModel = new ViewModelProvider(this).get(AdminUserMonsterViewModel.class);

    RecyclerView recyclerView = binding.selectUserMonsterRecyclerView;
    final AdminUserMonsterAdapter adapter = new AdminUserMonsterAdapter(new UserMonsterDiff());

    adapter.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(UserMonster userMonster) {
        Intent intent = AdminCreateMonsterActivity.intentFactory(getApplicationContext(),
            loggedInUserId,
            userMonster.getMonsterTypeId(),
            userMonster.getUserMonsterId()
        );
        startActivity(intent);
      }
    });

    recyclerView.setAdapter(adapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));

    adminUserMonsterViewModel.getUserMonstersWithTypeAndUserLiveData().observe(this, adapter::submitList);

    binding.adminSelectUserMonsterReturnButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = AdminLobbyActivity.intentFactory(getApplicationContext(), loggedInUserId);
        startActivity(intent);
      }
    });
  }

  public static Intent intentFactory(Context context, int loggedInUserId) {
    Intent intent = new Intent(context, AdminSelectUserMonsterActivity.class);
    intent.putExtra(LobbyActivity.LOBBY_USER_ID, loggedInUserId);
    return intent;
  }
}