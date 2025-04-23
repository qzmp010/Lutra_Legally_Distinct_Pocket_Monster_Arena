package com.lutra.legallydistinctpocketmonsterarea.viewHolders;

import static android.widget.Toast.LENGTH_SHORT;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import com.lutra.legallydistinctpocketmonsterarea.R;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.MonsterType;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.UserMonster;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SwitchMonsterViewHolder extends RecyclerView.ViewHolder {

    private final ImageView monsterRecyclerItemImageView;
    private final TextView monsterRecyclerItemNameTextView;
    private final TextView monsterRecyclerItemStatsTextView;

    private SwitchMonsterViewHolder(View monsterView) {
        super(monsterView);
        monsterRecyclerItemImageView = monsterView.findViewById(R.id.monsterRecyclerItemImageView);
        monsterRecyclerItemNameTextView = monsterView.findViewById(R.id.monsterRecyclerItemNameTextView);
        monsterRecyclerItemStatsTextView = monsterView.findViewById(R.id.monsterRecyclerItemStatsTextView);
    }

    //todo: filter monsters by userId
    //todo: show default text if no monsters
    public void bind(UserMonster userMonsterEntry) {
        monsterRecyclerItemNameTextView.setText(userMonsterEntry.getNickname());

        monsterRecyclerItemStatsTextView.setText(
                String.join("\n",
                        userMonsterEntry.getType().toString(),
                        String.format("%d/%d", userMonsterEntry.getCurrentHealth(), userMonsterEntry.getMaxHealth()),
                        String.valueOf(userMonsterEntry.getAttack()),
                        String.valueOf(userMonsterEntry.getDefense())
                )
        );

        monsterRecyclerItemImageView.setImageResource(userMonsterEntry.getImageID());

        //show monster phrase when clicked
        itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(
                        itemView.getContext(),
                        String.format("My ID is %d", userMonsterEntry.getUserMonsterId()),
                        LENGTH_SHORT).show();
            }
        });
    }

    static SwitchMonsterViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_monster, parent, false);
        return new SwitchMonsterViewHolder(view);
    }
}
