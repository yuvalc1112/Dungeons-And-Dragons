package game.gameManager;

import java.util.ArrayList;
import java.util.List;

import game.tiles.tilesType.Empty;
import game.tiles.units.enemies.Enemy;
import game.tiles.units.player.Player;
import game.utils.Position;


public class Level {

        private Board Board;
        private Player player;
        private List<Enemy> enemies;
        private Position playerStartPosition;

        public Level(Board gameBoard, Player player) {
            this.Board = gameBoard;
            this.player = player;
            this.enemies = new ArrayList();
        }

        public void AddEnemies(Enemy e) {
            this.enemies.add(e);
        }

        public void SetPlayerStartPosition(Position p) {
            this.playerStartPosition = p;
        }

        public Position GetPlayerStartPosition() {
            return this.playerStartPosition;
        }

        public Board getBoard() {
            return this.Board;
        }

        public void whilePlayerDeath() {
        }

        public void onEnemyDeath(Enemy e) {
            Position deadEnemyPosition = new Position(e.getPosition().x, e.getPosition().y);
            this.player.addExperience(e.getExperienceVal());
            this.enemies.remove(e);
            this.getBoard().Remove(e);
            this.getBoard().add(new Empty(deadEnemyPosition));
        }

        public String toString() {
            return String.format("%s\n%s", this.Board, this.player.describe());
        }

        public List<Enemy> GetEnemies() {
            return this.enemies;
        }

        public Player getPlayer() {
            return this.player;
        }

        public void SetPlayer(Player player) {
            this.player = player;
        }
    }

