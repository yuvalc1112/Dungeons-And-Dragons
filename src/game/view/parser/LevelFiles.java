package game.view.parser;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import game.callBack.MessageCallback;
import game.gameManager.*;
import game.tiles.tilesType.Empty;
import game.tiles.tilesType.Wall;
import game.tiles.units.player.Player;
import game.tiles.units.enemies.Enemy;
import game.utils.*;




public class LevelFiles {
    private MessageCallback messageCallback;

    public LevelFiles(MessageCallback messageCallback) {
        this.messageCallback = messageCallback;
    }

    public Level ExtractLevel(String levelFile, Player player, TileFactory tiles) {
        try {
            List<String> l = Files.readAllLines(Paths.get(levelFile));
            Board board = new Board();
            Level gameLevel = new Level(board, player);

            for(int i = 0; i < l.size(); ++i) {
                String row = l.get(i);

                for(int j = 0; j < row.length(); ++j) {
                    char c = row.charAt(j);
                    Position p = new Position(j, i);
                    //if it is a wall
                    if (c == '#') {
                        board.add(new Wall(p));
                        //if it is a free apace
                    } else if (c == '.') {
                        board.add(new Empty(p));
                        //if it is a player
                    } else if (c == '@') {
                        gameLevel.SetPlayerStartPosition(p);
                        player.setPosition(p);
                        player.SetMessageCallBack(this.messageCallback);
                        player.setDeathCallBack(() -> {
                            gameLevel.whilePlayerDeath();
                        });
                        board.add(player);
                        //if it is an Enemy
                    } else {
                        Enemy enemy = tiles.produceEnemy(c);
                        enemy.setPosition(p);
                        gameLevel.AddEnemies(enemy);
                        enemy.setDeathCallBack(() -> {
                            gameLevel.onEnemyDeath(enemy);
                        });
                        enemy.SetMessageCallBack(this.messageCallback);
                        board.add(enemy);
                    }
                }
            }

            return gameLevel;

        } catch (IOException ioException) {
            PrintStream errVar = System.err;
            String exceptionMessage = ioException.getMessage();
            errVar.println(exceptionMessage + "\n" + ioException.getStackTrace());
            return null;
        }
    }
}
