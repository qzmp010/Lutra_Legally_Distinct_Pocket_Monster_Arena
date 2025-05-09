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

public class SwitchMonsterViewModel extends AndroidViewModel {

    private final AppRepository repository;

    public SwitchMonsterViewModel(Application application) {
        super(application);
        repository = AppRepository.getRepository(application);
    }

    public LiveData<List<MonsterTypeWithUserMonsters>> getMonsterTypesWithUserMonstersLiveData() {
        return repository.getMonsterTypesWithUserMonstersLiveData();
    }

    public LiveData<List<UserMonster>> getUserMonstersByUserIdLiveData(int userID) {
        return repository.getUserMonstersByUserIdLiveData(userID);
    }
}