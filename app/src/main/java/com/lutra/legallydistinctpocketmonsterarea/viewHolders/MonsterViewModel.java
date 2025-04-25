package com.lutra.legallydistinctpocketmonsterarea.viewHolders;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.lutra.legallydistinctpocketmonsterarea.database.AppRepository;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.MonsterType;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.MonsterTypeWithUserMonsters;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.UserMonster;
import java.util.List;
import java.util.Map.Entry;

public class MonsterViewModel extends AndroidViewModel {

  private final AppRepository repository;

  public MonsterViewModel(Application application) {
    super(application);
    repository = AppRepository.getRepository(application);
  }

  public LiveData<List<Entry<UserMonster, MonsterType>>> getUserMonstersWithTypeListLiveData() {
    return repository.getUserMonstersWithTypeListLiveData();
  }

  public LiveData<List<Entry<UserMonster, MonsterType>>> getUserMonstersWithTypeListByUserIdLiveData(int userId) {
    return repository.getUserMonsterWithTypeListByUserIdLiveData(userId);
  }
}
