package com.lutra.legallydistinctpocketmonsterarea.viewHolders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.lutra.legallydistinctpocketmonsterarea.R;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.MonsterType;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.User;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.UserMonster;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import kotlin.Triple;

public class AdminUserMonsterViewHolder extends RecyclerView.ViewHolder {

  private final ImageView monsterRecyclerItemImageView;
  private final TextView monsterRecyclerItemNameTextView;
  private final TextView monsterRecyclerItemStatsTextView;
  private final TextView monsterRecyclerItemLabelsTextView;

  private AdminUserMonsterViewHolder(View monsterView) {
    super(monsterView);
    monsterRecyclerItemImageView = monsterView.findViewById(R.id.monsterRecyclerItemImageView);
    monsterRecyclerItemNameTextView = monsterView.findViewById(R.id.monsterRecyclerItemNameTextView);
    monsterRecyclerItemStatsTextView = monsterView.findViewById(R.id.monsterRecyclerItemStatsTextView);
    monsterRecyclerItemLabelsTextView = monsterView.findViewById(R.id.monsterRecyclerItemLabelsTextView);
  }

  public void bind(Triple<UserMonster, MonsterType, User> monsterTriple) {
    //set text as {nickname} - {monsterTypeName} if both are unique
    //else set text as {monsterTypeName}
    monsterRecyclerItemNameTextView.setText(
        Stream.of(monsterTriple.getFirst().getNickname(),
                monsterTriple.getSecond().getMonsterTypeName())
            .filter(n -> !n.isEmpty())
            .distinct()
            .collect(Collectors.joining(" - "))
    );

    monsterRecyclerItemStatsTextView.setText(
        String.join("\n",
            monsterTriple.getSecond().getElementalType().toString(),
            String.valueOf(monsterTriple.getFirst().getMaxHealth()),
            String.valueOf(monsterTriple.getFirst().getAttack()),
            String.valueOf(monsterTriple.getFirst().getDefense()),
            monsterTriple.getThird() == null
                ? String.valueOf(monsterTriple.getFirst().getUserId())
                : monsterTriple.getThird().getUsername()
        )
    );

    monsterRecyclerItemImageView.setImageResource(
        monsterTriple.getSecond().getImageID()
    );

    monsterRecyclerItemLabelsTextView.setText(
        R.string.view_label_type_health_attack_defense_owner
    );
  }

  static AdminUserMonsterViewHolder create(ViewGroup parent) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.recycler_item_monster, parent, false);
    return new AdminUserMonsterViewHolder(view);
  }
}
