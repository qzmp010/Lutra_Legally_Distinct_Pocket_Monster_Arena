package com.lutra.legallydistinctpocketmonsterarea.viewHolders;

import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.UserMonster;

public class MonsterAdapter extends ListAdapter<UserMonster, MonsterViewHolder> {

  public MonsterAdapter(@NonNull DiffUtil.ItemCallback<UserMonster> diffCallback) {
    super(diffCallback);
  }

  @NonNull
  @Override
  public MonsterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return MonsterViewHolder.create(parent);
  }

  @Override
  public void onBindViewHolder(@NonNull MonsterViewHolder holder, int position) {
    UserMonster current = getItem(position);
    holder.bind(current.toString());
  }

  public static class UserMonsterDiff extends DiffUtil.ItemCallback<UserMonster> {

    @Override
    public boolean areItemsTheSame(@NonNull UserMonster oldItem, @NonNull UserMonster newItem) {
      return oldItem == newItem;
    }

    @Override
    public boolean areContentsTheSame(@NonNull UserMonster oldItem, @NonNull UserMonster newItem) {
      return oldItem.equals(newItem);
    }
  }
}
