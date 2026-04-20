package game.tiles.units.enemies;
import game.tiles.units.Unit;
import game.tiles.units.player.Player;
import game.utils.Position;


import java.util.Random;

public class Monster extends Enemy {
    protected int visionRange;

    public Monster(char tile, String name, int hitPoints, int attack, int defense, int experienceValue, int visionRange) {
        super(tile, name, hitPoints, attack, defense, experienceValue);
        this.visionRange = visionRange;
    }
    public Position chase(Player p) {
        int dx = this.getPosition().getX() - p.getPosition().getX();
        int dy = this.getPosition().getY() - p.getPosition().getY();
        if (Math.abs(dx) > Math.abs(dy)) {
            return dx > 0 ? new Position(this.position.getX() - 1, this.position.getY()) : new Position(this.position.getX() + 1, this.position.getY());
        } else {
            return dy > 0 ? new Position(this.position.getX(), this.position.getY() - 1) : new Position(this.position.getX(), this.position.getY() + 1);
        }
    }

    @Override
    public Position enemyTurn(Player player) {
        if (this.getPosition().distance(player.getPosition()) < 2) {
            return player.getPosition();
        }
        if (this.getPosition().distance(player.getPosition()) <= this.visionRange) {
            return this.chase(player);
        }
        else {
            Random rand = new Random();
            int dir = rand.nextInt(5);
            if (dir == 0) return new Position(this.position.getX() + 1, this.position.getY());
            if (dir == 1) return new Position(this.position.getX() - 1, this.position.getY());
            if (dir == 2) return new Position(this.position.getX(), this.position.getY() - 1);
            if (dir == 3) return new Position(this.position.getX(), this.position.getY() + 1);
            return this.position;
        }
    }

    @Override
    public String describe() {
        return super.describe();
    }

}
