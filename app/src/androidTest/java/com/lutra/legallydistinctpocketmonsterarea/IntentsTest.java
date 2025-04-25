package com.lutra.legallydistinctpocketmonsterarea;

import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests each Activity's intentFactory() method to verify that it returns the same category of intent
 * that a new Intent() statement would make.
 * @author David Rosenfeld
 */
@RunWith(AndroidJUnit4.class)
public class IntentsTest {

  private static final int TEST_INT = 1;

  @Test
  public void testAdminCreateMonsterActivity() {
    Intents.init();

    Intent intent = AdminCreateMonsterActivity.intentFactory(ApplicationProvider.getApplicationContext(), TEST_INT, TEST_INT, TEST_INT);
    assert(intent).hasExtra(AdminCreateMonsterActivity.MONSTER_TYPE_ID_KEY);
    assert(intent).hasExtra(AdminCreateMonsterActivity.USER_MONSTER_ID_KEY);
    assert(intent).hasExtra(LobbyActivity.LOBBY_USER_ID);

    Intent intent2 = new Intent(ApplicationProvider.getApplicationContext(), AdminCreateMonsterActivity.class);
    assert(intent).filterEquals(intent2);

    Intents.release();
  }

  @Test
  public void testAdminLobbyActivity() {
    Intents.init();

    Intent intent = AdminLobbyActivity.intentFactory(ApplicationProvider.getApplicationContext(),TEST_INT);
    assert(intent).hasExtra(LobbyActivity.LOBBY_USER_ID);

    Intent intent2 = new Intent(ApplicationProvider.getApplicationContext(), AdminLobbyActivity.class);
    assert(intent).filterEquals(intent2);

    Intents.release();
  }

  @Test
  public void testAdminSelectMonsterTypeActivity() {
    Intents.init();

    Intent intent = AdminSelectMonsterTypeActivity.intentFactory(ApplicationProvider.getApplicationContext(), TEST_INT);
    assert(intent).hasExtra(LobbyActivity.LOBBY_USER_ID);

    Intent intent2 = new Intent(ApplicationProvider.getApplicationContext(), AdminSelectMonsterTypeActivity.class);
    assert(intent).filterEquals(intent2);

    Intents.release();
  }

  @Test
  public void testBattleActivity() {
    Intents.init();

    Intent intent = BattleActivity.intentFactory(ApplicationProvider.getApplicationContext());
    Intent intent2 = new Intent(ApplicationProvider.getApplicationContext(), BattleActivity.class);
    assert(intent).filterEquals(intent2);

    Intents.release();
  }

  @Test
  public void testCaptureActivity() {
    Intents.init();

    Intent intent = CaptureActivity.intentFactory(ApplicationProvider.getApplicationContext());
    Intent intent2 = new Intent(ApplicationProvider.getApplicationContext(), CaptureActivity.class);
    assert(intent).filterEquals(intent2);

    Intents.release();
  }

  @Test
  public void testChooseMonsterActivity() {
    Intents.init();

    Intent intent = ChooseMonsterActivity.intentFactory(ApplicationProvider.getApplicationContext());
    Intent intent2 = new Intent(ApplicationProvider.getApplicationContext(), ChooseMonsterActivity.class);
    assert(intent).filterEquals(intent2);

    Intents.release();
  }

  @Test
  public void testEditMonstersActivity() {
    Intents.init();

    Intent intent = EditMonstersActivity.intentFactory(ApplicationProvider.getApplicationContext(), TEST_INT);
    assert(intent).hasExtra(LobbyActivity.LOBBY_USER_ID);

    Intent intent2 = new Intent(ApplicationProvider.getApplicationContext(), EditMonstersActivity.class);
    assert(intent).filterEquals(intent2);

    Intents.release();
  }

  @Test
  public void testLobbyActivity() {
    Intents.init();

    Intent intent = LobbyActivity.intentFactory(ApplicationProvider.getApplicationContext());
    Intent intent2 = new Intent(ApplicationProvider.getApplicationContext(), LobbyActivity.class);
    assert(intent).filterEquals(intent2);

    Intents.release();
  }

  @Test
  public void testLoginActivity() {
    Intents.init();

    Intent intent = LoginActivity.loginIntentFactory(ApplicationProvider.getApplicationContext());
    Intent intent2 = new Intent(ApplicationProvider.getApplicationContext(), LoginActivity.class);
    assert(intent).filterEquals(intent2);

    Intents.release();
  }

  @Test
  public void testSignupActivity() {
    Intents.init();

    Intent intent = SignUpActivity.intentFactory(ApplicationProvider.getApplicationContext());
    Intent intent2 = new Intent(ApplicationProvider.getApplicationContext(), SignUpActivity.class);
    assert(intent).filterEquals(intent2);

    Intents.release();
  }

  @Test
  public void testSwitchMonsterActivity() {
    Intents.init();

    Intent intent = SwitchMonsterActivity.intentFactory(ApplicationProvider.getApplicationContext());
    Intent intent2 = new Intent(ApplicationProvider.getApplicationContext(), SwitchMonsterActivity.class);
    assert(intent).filterEquals(intent2);

    Intents.release();
  }

  @Test
  public void testViewMonstersActivity() {
    Intents.init();

    Intent intent = ViewMonstersActivity.intentFactory(ApplicationProvider.getApplicationContext(), TEST_INT);
    assert(intent).hasExtra(LobbyActivity.LOBBY_USER_ID);

    Intent intent2 = new Intent(ApplicationProvider.getApplicationContext(), ViewMonstersActivity.class);
    assert(intent).filterEquals(intent2);

    Intents.release();
  }
}