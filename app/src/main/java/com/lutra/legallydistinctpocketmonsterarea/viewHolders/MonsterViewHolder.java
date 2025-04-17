package com.lutra.legallydistinctpocketmonsterarea.viewHolders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.lutra.legallydistinctpocketmonsterarea.R;

public class MonsterViewHolder extends RecyclerView.ViewHolder {

  private final TextView monsterViewItem;

  private MonsterViewHolder(View monsterView) {
    super(monsterView);
    monsterViewItem = monsterView.findViewById(R.id.monsterRecyclerItemTextView);
  }

  //todo: add binding for MonsterType data
  //todo: add monster image
  //todo: show default text if no monsters
  public void bind(String text) {
    monsterViewItem.setText(text);
  }

  static MonsterViewHolder create(ViewGroup parent) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.recycler_item_monster, parent, false);
    return new MonsterViewHolder(view);
  }
}
