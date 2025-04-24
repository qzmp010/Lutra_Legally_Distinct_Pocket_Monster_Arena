package com.lutra.legallydistinctpocketmonsterarea.viewHolders;

import static android.widget.Toast.LENGTH_SHORT;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.lutra.legallydistinctpocketmonsterarea.BattleActivity;
import com.lutra.legallydistinctpocketmonsterarea.LobbyActivity;
import com.lutra.legallydistinctpocketmonsterarea.R;
import com.lutra.legallydistinctpocketmonsterarea.SwitchMonsterActivity;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.UserMonster;

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
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(itemView.getContext());
                AlertDialog confirmDialog = alertBuilder.create();
                alertBuilder.setTitle(String.format("Battle with %s?", userMonsterEntry.getNickname()));

                alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(userMonsterEntry.getCurrentHealth() > 0) {
                            confirmDialog.dismiss();
                            Intent intent = BattleActivity.intentFactory(itemView.getContext());
                            intent.putExtra(LobbyActivity.LOBBY_USER_ID, userMonsterEntry.getUserId());
                            intent.putExtra(SwitchMonsterActivity.MONSTER_ID, userMonsterEntry.getUserMonsterId());
                            itemView.getContext().startActivity(intent);
                        } else {
                            confirmDialog.dismiss();
                            Toast.makeText(itemView.getContext(),
                                            String.format("%s has fainted and can't fight!", userMonsterEntry.getNickname()),
                                            LENGTH_SHORT).show();
                        }
                    }
                });

                alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        confirmDialog.dismiss();
                    }
                });

                alertBuilder.create().show();
            }
        });
    }

    static SwitchMonsterViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_monster, parent, false);
        return new SwitchMonsterViewHolder(view);
    }
}
