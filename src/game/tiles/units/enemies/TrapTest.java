package game.tiles.units.enemies;
import game.tiles.units.player.Player;
import game.tiles.units.player.Warrior;
import game.utils.Position;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TrapTest {
    @Test
    public void trapShouldNotMoveFromItsPosition() {
        Trap trap = new Trap('Q', "Trap", 250, 50, 10, 100, 3, 4);
        Player player = new Warrior("Player", 500, 10, 10, 5);

        trap.setPosition(new Position(5, 5));
        player.setPosition(new Position(20, 20)); // הוספת מיקום לשחקן
        Position initialPosition = trap.getPosition();
        Position nextPosition = trap.enemyTurn(player);
        assertEquals("Trap's position should remain unchanged.", initialPosition, nextPosition);
    }

    @Test
    public void trapShouldAttackWhenPlayerIsAdjacent() {
        Trap trap = new Trap('Q', "Trap", 250, 50, 10, 100, 3, 4);
        Player player = new Warrior("Player", 500, 10, 10, 5);
        trap.SetMessageCallBack(msg -> {});
        player.SetMessageCallBack(msg -> {});

        trap.setPosition(new Position(5, 5));
        player.setPosition(new Position(5, 6));
        int initialPlayerHealth = player.getHealth().getCurrent();

        trap.enemyTurn(player);
        assertTrue("Player should take damage.", player.getHealth().getCurrent() < initialPlayerHealth);
    }

    @Test
    public void trapShouldNotAttackWhenPlayerIsOutOfRange() {
        // Arrange
        Trap trap = new Trap('Q', "Trap", 250, 50, 10, 100, 3, 4);
        Player player = new Warrior("Player", 500, 10, 10, 5);

        trap.setPosition(new Position(5, 5));
        player.setPosition(new Position(10, 10)); // מיקום רחוק

        int initialPlayerHealth = player.getHealth().getCurrent();

        // Act
        trap.enemyTurn(player);

        // Assert
        assertEquals("Player should not take damage when out of range.", initialPlayerHealth, player.getHealth().getCurrent());
    }

    @Test
    public void testTrapVisibilityCycleIsCorrectAndLogical() {
        Trap trap = new Trap('Q', "Queen's Trap", 250, 50, 10, 100, 3, 4);
        Player player = new Warrior("Test Player", 500, 10, 10, 5);

        trap.setPosition(new Position(5, 5));
        player.setPosition(new Position(10, 10));

        char originalChar = trap.getTileChar();
        final char INVISIBLE_CHAR = '.';

        assertEquals("Tick 0", originalChar, trap.getTileChar());
        trap.enemyTurn(player);
        assertEquals("Tick 1", originalChar, trap.getTileChar());
        trap.enemyTurn(player);
        assertEquals("Tick 2", originalChar, trap.getTileChar());
        trap.enemyTurn(player);
        assertEquals("Tick 3: Should become invisible", INVISIBLE_CHAR, trap.getTileChar());
        trap.enemyTurn(player);
        assertEquals("Tick 4: Should stay invisible", INVISIBLE_CHAR, trap.getTileChar());
        trap.enemyTurn(player);
        assertEquals("Tick 5: Should stay invisible", INVISIBLE_CHAR, trap.getTileChar());
        trap.enemyTurn(player);
        assertEquals("Tick 6: Should stay invisible", INVISIBLE_CHAR, trap.getTileChar());
        trap.enemyTurn(player);
        assertEquals("Tick 7: Should reset and become visible", originalChar, trap.getTileChar());
    }
}