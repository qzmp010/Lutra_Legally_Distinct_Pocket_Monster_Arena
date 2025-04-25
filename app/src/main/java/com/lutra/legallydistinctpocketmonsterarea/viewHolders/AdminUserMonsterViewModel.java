package com.lutra.legallydistinctpocketmonsterarea.viewHolders;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.lutra.legallydistinctpocketmonsterarea.database.AppRepository;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.MonsterType;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.User;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.UserMonster;
import java.util.List;
import kotlin.Triple;

public class AdminUserMonsterViewModel extends AndroidViewModel {

  private final AppRepository repository;

  public AdminUserMonsterViewModel(@NonNull Application application) {
    super(application);
    repository = AppRepository.getRepository(application);
  }

  public LiveData<List<Triple<UserMonster, MonsterType, User>>> getUserMonstersWithTypeAndUserLiveData() {
    return repository.getUserMonstersWithTypeAndUserLiveData();
  }
}
