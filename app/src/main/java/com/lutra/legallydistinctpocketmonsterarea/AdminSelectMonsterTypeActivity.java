package com.lutra.legallydistinctpocketmonsterarea;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.lutra.legallydistinctpocketmonsterarea.database.AppRepository;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.MonsterType;
import com.lutra.legallydistinctpocketmonsterarea.databinding.ActivityAdminSelectMonsterTypeBinding;
import com.lutra.legallydistinctpocketmonsterarea.databinding.ActivityViewMonstersBinding;
import com.lutra.legallydistinctpocketmonsterarea.viewHolders.AdminMonsterTypeAdapter;
import com.lutra.legallydistinctpocketmonsterarea.viewHolders.AdminMonsterTypeAdapter.MonsterTypeDiff;
import com.lutra.legallydistinctpocketmonsterarea.viewHolders.AdminMonsterTypeAdapter.OnClickListener;
import com.lutra.legallydistinctpocketmonsterarea.viewHolders.AdminMonsterTypeViewModel;
import com.lutra.legallydistinctpocketmonsterarea.viewHolders.MonsterAdapter;
import com.lutra.legallydistinctpocketmonsterarea.viewHolders.MonsterAdapter.UserMonsterDiff;
import com.lutra.legallydistinctpocketmonsterarea.viewHolders.MonsterViewModel;

public class AdminSelectMonsterTypeActivity extends AppCompatActivity {

  private ActivityAdminSelectMonsterTypeBinding binding;
  private AppRepository repository;
  private AdminMonsterTypeViewModel adminMonsterTypeViewModel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityAdminSelectMonsterTypeBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    adminMonsterTypeViewModel = new ViewModelProvider(this).get(AdminMonsterTypeViewModel.class);

    RecyclerView recyclerView = binding.selectMonsterTypeRecyclerView;
    final AdminMonsterTypeAdapter adapter = new AdminMonsterTypeAdapter(new MonsterTypeDiff());

    adapter.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(MonsterType monsterType) {
        Intent intent = AdminCreateMonsterActivity.intentFactory(getApplicationContext(),
            monsterType.getMonsterTypeId());
        startActivity(intent);
      }
    });

    recyclerView.setAdapter(adapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));

    repository = AppRepository.getRepository(getApplication());

    adminMonsterTypeViewModel.getMonsterTypesLiveData().observe(this, adapter::submitList);
  }

  public static Intent intentFactory(Context context) {
    Intent intent = new Intent(context, AdminSelectMonsterTypeActivity.class);
    return intent;
  }
}