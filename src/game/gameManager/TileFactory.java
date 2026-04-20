package game.gameManager;

import game.tiles.units.enemies.Enemy;
import game.tiles.units.enemies.Monster;
import game.tiles.units.player.Mage;
import game.tiles.units.player.Player;
import game.tiles.units.player.Rogue;
import game.tiles.units.player.Warrior;
import game.tiles.units.enemies.Trap;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class TileFactory {
    private Player p;
    private final Map<Character, Supplier<Enemy>> enemiesMap ;
    private final List<Supplier<Player>> playerTypes = Arrays.asList(
            () -> new Warrior("Jon Snow", 300, 30, 4, 3),
            () -> new Warrior("The Hound", 400, 20, 6, 5),
            () -> new Mage("Melisandre", 100, 5, 1, 300, 30, 15, 5, 6),
            () -> new Mage("Thoros of Myr", 250, 25, 4, 150, 20, 20, 3, 4),
            () -> new Rogue("Arya Stark", 150, 40, 2, 20),
            () -> new Rogue("Bronn", 250, 35, 3, 50)


    );
    private final List<Supplier<Enemy>>  enemiesTypes = Arrays.asList(
            () -> new Monster('s', "Lannister Solider", 80, 8, 3,25, 3),
            () -> new Monster('k', "Lannister Knight", 200, 14, 8, 50,   4),
            () -> new Monster('q', "Queen's Guard", 400, 20, 15, 100,  5),
            () -> new Monster('z', "Wright", 600, 30, 15,100, 3),
            () -> new Monster('b', "Bear-Wright", 1000, 75, 30, 250,  4),
            () -> new Monster('g', "Giant-Wright",1500, 100, 40,500,   5),
            () -> new Monster('w', "White Walker", 2000, 150, 50, 1000, 6),
            () -> new Monster('M', "The Mountain", 1000, 60, 25,  500, 6),
            () -> new Monster('C', "Queen Cersei", 100, 10, 10,1000, 1),
            () -> new Monster('K', "Night's King", 5000, 300, 150, 5000, 8),
            () -> new Trap('B', "Bonus Trap", 1, 1, 1, 250,  1, 5),
            () -> new Trap('Q', "Queen's Trap", 250, 50, 10, 100, 3, 7),
            () -> new Trap('D', "Death Trap", 500, 100, 20, 250, 1, 10)
    );

    public TileFactory() {
        this.enemiesMap = enemiesTypes.stream().collect(Collectors.toMap(s -> s.get().getTileChar(), Function.identity()));
    }


    public List<Player> listPlayers() {

        return playerTypes.stream().map(Supplier::get).collect(Collectors.toList());
    }

    public Player producePlayer(int k) {
        return (Player) ((Supplier) this.playerTypes.get(k - 1)).get();
    }
    public Enemy produceEnemy(char c) {
        return (Enemy)((Supplier)this.enemiesMap.get(c)).get();
    }
}
