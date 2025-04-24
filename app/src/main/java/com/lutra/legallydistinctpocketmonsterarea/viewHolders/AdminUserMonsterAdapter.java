package com.lutra.legallydistinctpocketmonsterarea.viewHolders;

import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.DiffUtil.ItemCallback;
import androidx.recyclerview.widget.ListAdapter;
import kotlin.Triple;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.MonsterType;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.UserMonster;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.User;

public class AdminUserMonsterAdapter extends
    ListAdapter<Triple<UserMonster, MonsterType, User>, AdminUserMonsterViewHolder> {

  private AdminUserMonsterAdapter.OnClickListener onClickListener;

  public interface OnClickListener{
    void onClick(Triple<UserMonster, MonsterType, User> monsterTriple);
  }
  public void setOnClickListener(AdminUserMonsterAdapter.OnClickListener onClickListener) {
    this.onClickListener = onClickListener;
  }

  public AdminUserMonsterAdapter(
      @NonNull ItemCallback<Triple<UserMonster, MonsterType, User>> diffCallback) {
    super(diffCallback);
  }

  @NonNull
  @Override
  public AdminUserMonsterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return AdminUserMonsterViewHolder.create(parent);
  }

  @Override
  public void onBindViewHolder(@NonNull AdminUserMonsterViewHolder holder, int position) {
    Triple<UserMonster, MonsterType, User> current = getItem(position);
    holder.bind(current);
    holder.itemView.setOnClickListener(view -> {
      if (onClickListener != null) {
        onClickListener.onClick(current);
      }
    });
  }

  public static class UserMonsterDiff extends
      DiffUtil.ItemCallback<Triple<UserMonster, MonsterType, User>> {

    @Override
    public boolean areItemsTheSame(@NonNull Triple<UserMonster, MonsterType, User> oldItem,
        @NonNull Triple<UserMonster, MonsterType, User> newItem) {
      return oldItem == newItem;
    }

    @Override
    public boolean areContentsTheSame(@NonNull Triple<UserMonster, MonsterType, User> oldItem,
        @NonNull Triple<UserMonster, MonsterType, User> newItem) {
      return oldItem.equals(newItem);
    }
  }
}
