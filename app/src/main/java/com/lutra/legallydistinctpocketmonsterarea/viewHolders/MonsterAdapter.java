package com.lutra.legallydistinctpocketmonsterarea.viewHolders;

import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.MonsterType;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.UserMonster;
import java.util.Map.Entry;

public class MonsterAdapter extends ListAdapter<Entry<UserMonster, MonsterType>, MonsterViewHolder> {
 private OnClickListener onClickListener;
  public interface OnClickListener{
    void onClick(UserMonster monster);
  }
  public void setOnClickListener(OnClickListener onClickListener) {
    this.onClickListener = onClickListener;
  }


  public MonsterAdapter(@NonNull DiffUtil.ItemCallback<Entry<UserMonster, MonsterType>> diffCallback) {
    super(diffCallback);
  }

  @NonNull
  @Override
  public MonsterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return MonsterViewHolder.create(parent);
  }

  @Override
  public void onBindViewHolder(@NonNull MonsterViewHolder holder, int position) {
    Entry<UserMonster, MonsterType> current = getItem(position);
    holder.bind(current);
    holder.itemView.setOnClickListener(view -> {
      if (onClickListener != null) {
        onClickListener.onClick(current.getKey());
      }
    });

  }
  public static class UserMonsterDiff extends DiffUtil.ItemCallback<Entry<UserMonster, MonsterType>> {

    @Override
    public boolean areItemsTheSame(@NonNull Entry<UserMonster, MonsterType> oldItem, @NonNull Entry<UserMonster, MonsterType> newItem) {
      return oldItem == newItem;
    }

    @Override
    public boolean areContentsTheSame(@NonNull Entry<UserMonster, MonsterType> oldItem, @NonNull Entry<UserMonster, MonsterType> newItem) {
      return oldItem.equals(newItem);
    }
  }
}