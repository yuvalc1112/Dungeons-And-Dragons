package game.gameManager;

import game.callBack.MessageCallback;
import game.tiles.Tile;
import game.tiles.tilesType.Empty;
import game.tiles.tilesType.Wall;
import game.tiles.units.enemies.Enemy;
import game.tiles.units.enemies.Monster;
import game.tiles.units.player.Warrior;
import game.utils.Position;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class GameManagerIntegrationTest {
    private GameManager gameManager;
    private Level level;
    private Warrior player;
    private Board board;
    private List<Enemy> enemies;

    @Before
    public void setUp() {
        board = new Board();
        player = new Warrior("Test Player", 200, 50, 20, 5);
        level = new Level(board, player);
        enemies = new ArrayList<>();

        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                board.add(new Empty(new Position(x, y)));
            }
        }


        MessageCallback cb = (msg) -> {};
        player.SetMessageCallBack(cb);
        gameManager = new GameManager(level, player, enemies, cb);
    }
    private void addUnitToBoard(Tile unit, Position position) {
        unit.setPosition(position);


        Tile oldTile = board.GetTile(position.x, position.y);
        if (oldTile != null) {
            board.Remove(oldTile);
        }

        board.add(unit);

        if (unit instanceof Enemy) {
            Enemy enemy = (Enemy) unit;
            level.AddEnemies(enemy);
            enemies.add(enemy);
            enemy.setDeathCallBack(() -> level.onEnemyDeath(enemy));
            enemy.SetMessageCallBack(msg -> {});
        }
    }

    @Test
    public void testPlayerMoveAndKillScenario() {
        addUnitToBoard(player, new Position(1, 2));
        Enemy weakEnemy = new Monster('s', "Goblin", 10, 5, 0, 40, 5);
        addUnitToBoard(weakEnemy, new Position(2, 2)); // שימוש במתודת העזר

        int playerInitialExp = player.getExperience();

        gameManager.tick('d');

        int maxAttempts = 50;
        int attempts = 0;
        while (weakEnemy.isAlive() && attempts < maxAttempts) {
            player.visit(weakEnemy);
            attempts++;
        }

        assertTrue("Enemy should be dead after repeated attacks.", weakEnemy.isDead());
        assertEquals("Player should gain experience.", playerInitialExp + 40, player.getExperience());

        gameManager.tick('q');
        assertTrue("GameManager should detect all enemies are dead.", gameManager.DeadEnemies());
    }


    @Test
    public void testPlayerDeathScenario() {
        addUnitToBoard(player, new Position(2, 1));
        player.getHealth().setCurrent(1);

        Enemy strongEnemy = new Monster('K', "Night King", 5000, 300, 0, 0, 8);
        addUnitToBoard(strongEnemy, new Position(2, 2));

        int maxAttempts = 50;
        int attempts = 0;
        while (player.isAlive() && attempts < maxAttempts) {
            gameManager.tick('q');
            attempts++;
        }

        assertTrue("Player should be dead.", player.isDead());
        assertTrue("GameManager should know the player is dead.", gameManager.DeadPlayer());
    }
    @Test
    public void testPlayerMovesIntoWall() {
        addUnitToBoard(player, new Position(1, 1));
        addUnitToBoard(new Wall(), new Position(0, 1));

        Position initialPosition = new Position(player.getPosition().x, player.getPosition().y);
        gameManager.tick('a');

        assertEquals("Player should not move when walking into a wall.", initialPosition, player.getPosition());
    }
}