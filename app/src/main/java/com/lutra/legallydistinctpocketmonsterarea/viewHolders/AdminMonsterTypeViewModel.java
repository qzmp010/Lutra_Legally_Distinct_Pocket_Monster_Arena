package com.lutra.legallydistinctpocketmonsterarea.viewHolders;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.lutra.legallydistinctpocketmonsterarea.database.AppRepository;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.MonsterType;
import java.util.List;

public class AdminMonsterTypeViewModel extends AndroidViewModel{

    private final AppRepository repository;

    public AdminMonsterTypeViewModel(Application application) {
      super(application);
      repository = AppRepository.getRepository(application);
    }

    public LiveData<List<MonsterType>> getMonsterTypesLiveData() {
      return repository.getAllMonsterTypesLiveData();
    }
  }

