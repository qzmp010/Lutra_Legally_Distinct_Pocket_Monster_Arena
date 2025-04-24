package com.lutra.legallydistinctpocketmonsterarea.viewHolders;

import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.MonsterType;

public class AdminMonsterTypeAdapter extends ListAdapter<MonsterType, AdminMonsterTypeViewHolder> {
  private AdminMonsterTypeAdapter.OnClickListener onClickListener;

  public interface OnClickListener{
    void onClick(MonsterType monsterType);
  }

  public void setOnClickListener(AdminMonsterTypeAdapter.OnClickListener onClickListener) {
    this.onClickListener = onClickListener;
  }

  public AdminMonsterTypeAdapter(@NonNull DiffUtil.ItemCallback<MonsterType> diffCallback) {
    super(diffCallback);
  }

  @NonNull
  @Override
  public AdminMonsterTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return AdminMonsterTypeViewHolder.create(parent);
  }

  @Override
  public void onBindViewHolder(@NonNull AdminMonsterTypeViewHolder holder, int position) {
    MonsterType current = getItem(position);
    holder.bind(current);
    holder.itemView.setOnClickListener(view -> {
      if (onClickListener != null) {
        onClickListener.onClick(current);
      }
    });

  }
  public static class MonsterTypeDiff extends DiffUtil.ItemCallback<MonsterType> {

    @Override
    public boolean areItemsTheSame(@NonNull MonsterType oldItem, @NonNull MonsterType newItem) {
      return oldItem == newItem;
    }

    @Override
    public boolean areContentsTheSame(@NonNull MonsterType oldItem, @NonNull MonsterType newItem) {
      return oldItem.equals(newItem);
    }
  }
}
