package com.lutra.legallydistinctpocketmonsterarea.viewHolders;

import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.MonsterType;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.UserMonster;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class SwitchMonsterAdapter extends ListAdapter<UserMonster, SwitchMonsterViewHolder> {

    public SwitchMonsterAdapter(@NonNull DiffUtil.ItemCallback<UserMonster> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public SwitchMonsterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return SwitchMonsterViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull SwitchMonsterViewHolder holder, int position) {
        UserMonster current = getItem(position);
        holder.bind(current);
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
