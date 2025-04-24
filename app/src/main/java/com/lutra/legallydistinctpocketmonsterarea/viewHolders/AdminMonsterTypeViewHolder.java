package com.lutra.legallydistinctpocketmonsterarea.viewHolders;

import android.annotation.SuppressLint;
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

public class AdminMonsterTypeViewHolder extends RecyclerView.ViewHolder {
  private final ImageView monsterRecyclerItemImageView;
  private final TextView monsterRecyclerItemNameTextView;
  private final TextView monsterRecyclerItemStatsTextView;

  private AdminMonsterTypeViewHolder(View monsterView) {
    super(monsterView);
    monsterRecyclerItemImageView = monsterView.findViewById(R.id.monsterRecyclerItemImageView);
    monsterRecyclerItemNameTextView = monsterView.findViewById(R.id.monsterRecyclerItemNameTextView);
    monsterRecyclerItemStatsTextView = monsterView.findViewById(R.id.monsterRecyclerItemStatsTextView);
  }

  @SuppressLint("DefaultLocale")
  public void bind(MonsterType monsterType) {
    monsterRecyclerItemNameTextView.setText(
        monsterType.getMonsterTypeName()
    );

    monsterRecyclerItemStatsTextView.setText(
        String.join("\n",
            monsterType.getElementalType().toString(),
            String.format("%d - %d", monsterType.getHealthMin(), monsterType.getHealthMax()),
            String.format("%d - %d", monsterType.getAttackMin(), monsterType.getAttackMax()),
            String.format("%d - %d", monsterType.getDefenseMin(), monsterType.getDefenseMax())
        )
    );

    monsterRecyclerItemImageView.setImageResource(
        monsterType.getImageID()
    );
  }

  static AdminMonsterTypeViewHolder create(ViewGroup parent) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.recycler_item_monster, parent, false);
    return new AdminMonsterTypeViewHolder(view);
  }
}
