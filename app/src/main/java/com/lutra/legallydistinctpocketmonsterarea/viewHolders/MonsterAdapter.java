package com.lutra.legallydistinctpocketmonsterarea.viewHolders;

import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.MonsterTypeWithUserMonsters;

public class MonsterAdapter extends ListAdapter<MonsterTypeWithUserMonsters, MonsterViewHolder> {

  public MonsterAdapter(@NonNull DiffUtil.ItemCallback<MonsterTypeWithUserMonsters> diffCallback) {
    super(diffCallback);
  }

  @NonNull
  @Override
  public MonsterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return MonsterViewHolder.create(parent);
  }

  @Override
  public void onBindViewHolder(@NonNull MonsterViewHolder holder, int position) {
    MonsterTypeWithUserMonsters current = getItem(position);
    holder.bind(current.toString());
  }

  public static class UserMonsterDiff extends DiffUtil.ItemCallback<MonsterTypeWithUserMonsters> {

    @Override
    public boolean areItemsTheSame(@NonNull MonsterTypeWithUserMonsters oldItem, @NonNull MonsterTypeWithUserMonsters newItem) {
      return oldItem == newItem;
    }

    @Override
    public boolean areContentsTheSame(@NonNull MonsterTypeWithUserMonsters oldItem, @NonNull MonsterTypeWithUserMonsters newItem) {
      return oldItem.equals(newItem);
    }
  }
}
