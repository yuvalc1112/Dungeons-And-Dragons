package game.gameManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import game.tiles.Tile;
import game.tiles.tilesType.Empty;
import game.tiles.units.enemies.Enemy;
import game.tiles.units.player.Player;
import game.utils.Position;
import game.callBack.MessageCallback;


public class GameManager {
        private Level gameLevel;
        private MessageCallback messege;
        private List<Enemy> enemies;
        private Player player;
        private boolean deadPlayer;
        private boolean allEnemiesDead;

        public GameManager(Level level, Player player, List<Enemy> enemies, MessageCallback m) {
            this.messege = m;
            this.enemies = enemies;
            this.player = player;
            this.deadPlayer = false;
            this.allEnemiesDead = false;
            this.gameLevel = level;
        }

        public void setEnemies(List<Enemy> newEnemies) {
            this.enemies = newEnemies;
        }
        public void printBoard() {
            this.messege.send(this.gameLevel.getBoard().PrintBoard());
            this.messege.send(this.player.describe());
        }

        public void tick(char c) {

            Position positionPlayer = this.player.getPosition();
            Position targetPosition = null;
            if (c == 'w') {
                targetPosition = new Position(positionPlayer.x, positionPlayer.y - 1);
            } else if (c == 's') {
                targetPosition = new Position(positionPlayer.x, positionPlayer.y + 1);
            } else if (c == 'a') {
                targetPosition = new Position(positionPlayer.x - 1, positionPlayer.y);
            } else if (c == 'd') {
                targetPosition = new Position(positionPlayer.x + 1, positionPlayer.y);
            } else if (c == 'e') {
                this.player.castAbility(this.gameLevel.GetEnemies());
            } else if (c != 'q') {
                this.messege.send("Invalid Input");
            }

            if (targetPosition != null) {
                Tile targetTile = this.gameLevel.getBoard().GetTile(targetPosition.x, targetPosition.y);

                if (targetTile != null) {
                    this.player.interact(targetTile);
                }

                Tile updatedTargetTile = this.gameLevel.getBoard().GetTile(targetPosition.x, targetPosition.y);
                if (updatedTargetTile instanceof Empty) {
                    player.swap(player, updatedTargetTile);
                }
            }

            this.enemies = this.gameLevel.GetEnemies();
            this.OnGameTick();
            if (this.gameLevel.GetEnemies().isEmpty()) {
                this.allEnemiesDead = true;
            }

            if (this.player.isDead()) {
                this.deadPlayer = true;
            }
        }

        private void OnGameTick() {
            this.player.OnGameTick();

            for (Enemy e : new ArrayList<>(this.enemies)) {
                if (e.isAlive()) {

                    Position targetPos = e.enemyTurn(this.player);
                    Tile targetTile = this.gameLevel.getBoard().GetTile(targetPos.getX(), targetPos.getY());

                    if (targetTile != null) {
                        e.interact(targetTile);
                    }
                }
            }


        }

        public boolean DeadPlayer() {
            return this.deadPlayer;
        }

        public boolean DeadEnemies() {
            return this.allEnemiesDead;
        }
    }


