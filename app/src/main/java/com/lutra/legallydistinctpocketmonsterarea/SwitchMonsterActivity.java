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
import com.lutra.legallydistinctpocketmonsterarea.databinding.ActivitySwitchMonsterBinding;
import com.lutra.legallydistinctpocketmonsterarea.viewHolders.MonsterAdapter;
import com.lutra.legallydistinctpocketmonsterarea.viewHolders.MonsterViewModel;

public class SwitchMonsterActivity extends AppCompatActivity {

    private ActivitySwitchMonsterBinding binding;
    private AppRepository repository;
    private MonsterViewModel monsterViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySwitchMonsterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        monsterViewModel = new ViewModelProvider(this).get(MonsterViewModel.class);

        RecyclerView recyclerView = binding.switchMonsterRecyclerView;
        final MonsterAdapter adapter = new MonsterAdapter(new MonsterAdapter.UserMonsterDiff());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        repository = AppRepository.getRepository(getApplication());

        //todo: get data for specific userId
        //todo: separate each entry by UserMonster
        monsterViewModel.getUserMonstersWithTypeListLiveData().observe(this, adapter::submitList);

        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SwitchMonsterActivity.this, "Man, you sure clicked it!", LENGTH_SHORT).show();
            }
        });
    }

    public static Intent intentFactory(Context context) {
        Intent intent = new Intent(context, SwitchMonsterActivity.class);
        return intent;
    }
}