package com.lutra.legallydistinctpocketmonsterarea.viewHolders;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.lutra.legallydistinctpocketmonsterarea.database.AppRepository;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.UserMonster;
import java.util.List;

public class MonsterViewModel extends AndroidViewModel {

  private final AppRepository repository;

  public MonsterViewModel(Application application) {
    super(application);
    repository = AppRepository.getRepository(application);
  }

  public LiveData<List<UserMonster>> getByUserIdLiveData(int userId) {
    return repository.getByUserIdLiveData(userId);
  }
}
