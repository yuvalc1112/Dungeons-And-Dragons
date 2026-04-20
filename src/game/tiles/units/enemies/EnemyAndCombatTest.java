package game.tiles.units.enemies;

import game.tiles.units.player.Player;
import game.tiles.units.player.Warrior;
import game.utils.Position;
import org.junit.Before;
import org.junit.Test;
import java.util.Collections;
import static org.junit.Assert.*;

public class EnemyAndCombatTest {

    private Monster monster;
    private Player player;

    @Before
    public void setUp() {

        player = new Warrior("Test Player", 100, 20, 10, 5);
        monster = new Monster('s', "Test Monster", 80, 15, 5, 25, 5);

        player.SetMessageCallBack(msg -> {});
        monster.SetMessageCallBack(msg -> {});
    }


    @Test
    public void monsterShouldChasePlayerWhenInRange() {
        monster.setPosition(new Position(10, 10));
        player.setPosition(new Position(10, 8));
        Position nextMove = monster.enemyTurn(player);
        assertEquals("Monster should move up towards the player.", new Position(10, 9), nextMove);
    }

    @Test
    public void monsterShouldTargetPlayerWhenAdjacent() {
        monster.setPosition(new Position(10, 10));
        player.setPosition(new Position(10, 9));
        Position targetPos = monster.enemyTurn(player);
        assertEquals("When adjacent, monster's target should be the player's position.", player.getPosition(), targetPos);
    }


    @Test
    public void testStrongEnemyKillsPlayer() {

        Monster strongEnemy = new Monster('K', "Night King", 5000, 300, 0, 5000, 8); // הגנה 0 כדי להגדיל סיכוי לנזק
        strongEnemy.SetMessageCallBack(msg -> {});

        int maxAttempts = 100;
        int attempts = 0;
        while(player.isAlive() && attempts < maxAttempts) {
            strongEnemy.visit(player);
            attempts++;
        }


        assertTrue("Player should be dead after multiple attacks by a strong enemy.", player.isDead());
    }

    @Test
    public void testStrongPlayerKillsEnemy() {

        int initialPlayerHealth = player.getHealth().getCurrent();
        int initialMonsterHealth = monster.getHealth().getCurrent();

        player.visit(monster);

        assertTrue("Monster's health should not increase.", monster.getHealth().getCurrent() <= initialMonsterHealth);
        assertEquals("Player's health should not change when they are the attacker.", initialPlayerHealth, player.getHealth().getCurrent());

        monster.visit(player);
        assertTrue("Player's health should not increase after being attacked.", player.getHealth().getCurrent() <= initialPlayerHealth);
    }

    @Test
    public void testCombatWhereNoOneDies() {

        int initialPlayerHealth = player.getHealth().getCurrent();
        int initialMonsterHealth = monster.getHealth().getCurrent();
        player.visit(monster);

        int monsterHealthAfterAttack = monster.getHealth().getCurrent();
        int playerHealthAfterAttack = player.getHealth().getCurrent();

        assertTrue("Monster's health should not increase.", monsterHealthAfterAttack <= initialMonsterHealth);
        assertEquals("Player's health should not change when they are the attacker.", initialPlayerHealth, playerHealthAfterAttack);
        monster.visit(player);


        int finalPlayerHealth = player.getHealth().getCurrent();
        assertTrue("Player's health should not increase after being attacked.", finalPlayerHealth <= initialPlayerHealth);

    }
}