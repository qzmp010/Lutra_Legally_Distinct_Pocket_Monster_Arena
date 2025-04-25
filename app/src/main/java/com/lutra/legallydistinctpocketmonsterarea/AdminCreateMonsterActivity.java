package com.lutra.legallydistinctpocketmonsterarea;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.lutra.legallydistinctpocketmonsterarea.database.AppRepository;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.MonsterType;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.User;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.UserMonster;
import com.lutra.legallydistinctpocketmonsterarea.databinding.ActivityAdminEditMonsterBinding;
import java.util.List;

public class AdminCreateMonsterActivity extends AppCompatActivity {

  public static final String MONSTER_TYPE_ID_KEY = "com.lutra.ldpm.create_monster_type_id_key";
  private static final String USER_MONSTER_ID_KEY = "com.lutra.ldpm.edit_user_monster_id_key";
  private static final int ID_DEFAULT = -1;
  private ActivityAdminEditMonsterBinding binding;
  private AppRepository repository;
  private int monsterTypeId;
  private boolean isEditing;
  private MonsterType monsterType;
  private int userMonsterId;
  private UserMonster userMonster;
  private List<User> userList;
  private String monsterNickname;
  private int monsterHealth;
  private int monsterAttack;
  private int monsterDefense;
  private int monsterUserId;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityAdminEditMonsterBinding.inflate(getLayoutInflater());
    repository = AppRepository.getRepository(getApplication());
    View view = binding.getRoot();
    setContentView(view);

    monsterTypeId = getIntent().getIntExtra(MONSTER_TYPE_ID_KEY, ID_DEFAULT);
    userMonsterId = getIntent().getIntExtra(USER_MONSTER_ID_KEY, ID_DEFAULT);
    monsterType = repository.getMonsterTypeById(monsterTypeId);
    isEditing = userMonsterId != ID_DEFAULT;

    if (isEditing) {
      userMonster = repository.getUserMonsterById(userMonsterId);
      binding.adminEditMonsterEditTextView.setText(R.string.Edit_monster);
      binding.adminEditMonsterNicknameEditText.setText(userMonster.getNickname());
      binding.adminEditMonsterHealthEditText.setText(String.valueOf(userMonster.getMaxHealth()));
      binding.adminEditMonsterAttackEditText.setText(String.valueOf(userMonster.getAttack()));
      binding.adminEditMonsterDefenseEditText.setText(String.valueOf(userMonster.getDefense()));
    } else {
      binding.adminEditMonsterEditTextView.setText(R.string.Create_monster);
      binding.adminEditMonsterNicknameEditText.setText(monsterType.getMonsterTypeName());
    }

    repository.getAllUsers().observe(this,uL -> {
      userList = uL;
      List<String>userNames = userList.stream().map(User::getUsername).toList();
      ArrayAdapter<String> userDataAdapter = new ArrayAdapter<>(
          this, android.R.layout.simple_spinner_item, userNames);
      userDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
      binding.adminEditMonsterOwnerSpinner.setAdapter(userDataAdapter);

      if (isEditing) {
        String userName = userList.stream()
            .filter(u -> u.getId() == userMonster.getUserId())
            .map(u -> u.getUsername())
            .findFirst().orElse("");
        binding.adminEditMonsterOwnerSpinner.setSelection(userDataAdapter.getPosition(userName));
      }
    });

    binding.adminEditMonsterTypeNameTextView.setText(monsterType.getMonsterTypeName());

    binding.adminEditMonsterHealthTextView.setText(getString(
        R.string.label_admin_health_range,
        monsterType.getHealthMin(),
        monsterType.getHealthMax())
    );

    binding.adminEditMonsterAttackTextView.setText(getString(
        R.string.label_admin_attack_range,
        monsterType.getAttackMin(),
        monsterType.getAttackMax())
    );

    binding.adminEditMonsterDefenseTextView.setText(getString(
        R.string.label_admin_defense_range,
        monsterType.getDefenseMin(),
        monsterType.getDefenseMax())
    );

    binding.adminEditMonsterReturnButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        backToRecycler();
      }
    });

    binding.adminEditMonsterSaveButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (validateInput()) {
          insertMonster();
          toastMaker(String.format("%s was %s successfully!",
              monsterNickname,
              isEditing ? "updated" : "created"));
          backToRecycler();
        }
      }
    });
  }

  /**
   * Validates input fields and shows a Toast if values are outside of valid ranges defined in
   * MonsterType.
   * Populates private variable values
   * @return true if valid, false if not
   */
  private boolean validateInput() {
    try {
      monsterHealth = Integer.parseInt(binding.adminEditMonsterHealthEditText.getText().toString());
      if (monsterHealth < monsterType.getHealthMin() || monsterHealth > monsterType.getHealthMax()) {
        throw new NumberFormatException();
      }
    } catch (NumberFormatException e) {
      errorToastMaker("Health", monsterType.getHealthMin(), monsterType.getHealthMax());
      return false;
    }
    try {
      monsterAttack = Integer.parseInt(binding.adminEditMonsterAttackEditText.getText().toString());
      if (monsterAttack < monsterType.getAttackMin() || monsterAttack > monsterType.getAttackMax()) {
        throw new NumberFormatException();
      }
    } catch (NumberFormatException e) {
      errorToastMaker("Attack", monsterType.getAttackMin(), monsterType.getAttackMax());
      return false;
    }
    try {
      monsterDefense = Integer.parseInt(binding.adminEditMonsterDefenseEditText.getText().toString());
      if (monsterDefense < monsterType.getDefenseMin() || monsterDefense > monsterType.getDefenseMax()) {
        throw new NumberFormatException();
      }
    } catch (NumberFormatException e) {
      errorToastMaker("Defense", monsterType.getDefenseMin(), monsterType.getDefenseMax());
      return false;
    }

    String userName = binding.adminEditMonsterOwnerSpinner.getSelectedItem().toString();
    Integer userId = userList.stream()
        .filter(u -> u.getUsername().equals(userName))
        .map(User::getId)
        .findFirst()
        .orElse(null);
    if (userId == null) {
      toastMaker("Select a valid Owner");
      return false;
    }

    monsterUserId = userId;
    monsterNickname = binding.adminEditMonsterNicknameEditText.getText().toString().trim();
    if (monsterNickname.isEmpty()) {
      monsterNickname = monsterType.getMonsterTypeName();
    }
    return true;
  }

  private void toastMaker(String message) {
    Toast.makeText(getApplicationContext(),
        message,
        Toast.LENGTH_LONG).show();
  }

  @SuppressLint("DefaultLocale")
  private void errorToastMaker(String fieldName, int min, int max) {
    toastMaker(String.format("%s must be between %d and %d", fieldName, min, max));
  }

  private void insertMonster() {
    if (isEditing) {
      userMonster.setNickname(monsterNickname);
      userMonster.setMaxHealth(monsterHealth);
      userMonster.setAttack(monsterAttack);
      userMonster.setDefense(monsterDefense);
      userMonster.setUserId(monsterUserId);
      repository.insertUserMonster(userMonster);
    } else {
      MonsterFactory.createNewMonster(repository,
          monsterTypeId,
          monsterNickname,
          "",
          monsterAttack,
          monsterDefense,
          monsterHealth,
          monsterUserId
      );
    }
  }

  public void backToRecycler() {
    Intent intent = isEditing
        ? AdminSelectUserMonsterActivity.intentFactory(getApplicationContext())
        : AdminSelectMonsterTypeActivity.intentFactory(getApplicationContext());
    startActivity(intent);
  }

  public static Intent intentFactory(Context context, int monsterTypeId) {
    return intentFactory(context, monsterTypeId, ID_DEFAULT);
  }

  public static Intent intentFactory(Context context, int monsterTypeId, int userMonsterId) {
    Intent intent = new Intent(context, AdminCreateMonsterActivity.class);
    intent.putExtra(MONSTER_TYPE_ID_KEY, monsterTypeId);
    intent.putExtra(USER_MONSTER_ID_KEY, userMonsterId);
    return intent;
  }
}