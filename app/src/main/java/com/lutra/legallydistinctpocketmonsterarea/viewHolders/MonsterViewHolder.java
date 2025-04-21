package com.lutra.legallydistinctpocketmonsterarea.viewHolders;

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
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MonsterViewHolder extends RecyclerView.ViewHolder {

  private final ImageView monsterRecyclerItemImageView;
  private final TextView monsterRecyclerItemNameTextView;
  private final TextView monsterRecyclerItemStatsTextView;

  private MonsterViewHolder(View monsterView) {
    super(monsterView);
    monsterRecyclerItemImageView = monsterView.findViewById(R.id.monsterRecyclerItemImageView);
    monsterRecyclerItemNameTextView = monsterView.findViewById(R.id.monsterRecyclerItemNameTextView);
    monsterRecyclerItemStatsTextView = monsterView.findViewById(R.id.monsterRecyclerItemStatsTextView);
  }

  //todo: filter monsters by userId
  //todo: show default text if no monsters
  public void bind(Entry<UserMonster, MonsterType> userMonsterMonsterTypeEntry) {
    //set text as {nickname} - {monsterTypeName} if both are unique
    //else set text as {monsterTypeName}
    monsterRecyclerItemNameTextView.setText(
        Stream.of(userMonsterMonsterTypeEntry.getKey().getNickname(),
                userMonsterMonsterTypeEntry.getValue().getMonsterTypeName())
            .filter(n -> !n.isEmpty())
            .distinct()
            .collect(Collectors.joining(" - "))
    );

    monsterRecyclerItemStatsTextView.setText(
        String.join("\n",
            userMonsterMonsterTypeEntry.getValue().getElementalType().toString(),
            String.valueOf(userMonsterMonsterTypeEntry.getKey().getMaxHealth()),
            String.valueOf(userMonsterMonsterTypeEntry.getKey().getAttack()),
            String.valueOf(userMonsterMonsterTypeEntry.getKey().getDefense())
        )
    );

    monsterRecyclerItemImageView.setImageResource(
        userMonsterMonsterTypeEntry.getValue().getImageID()
    );

    //show monster phrase when clicked
    itemView.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Toast.makeText(itemView.getContext(),
            userMonsterMonsterTypeEntry.getValue().getPhrase(),
            Toast.LENGTH_SHORT
        ).show();
      }
    });
  }

  static MonsterViewHolder create(ViewGroup parent) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.recycler_item_monster, parent, false);
    return new MonsterViewHolder(view);
  }
}
