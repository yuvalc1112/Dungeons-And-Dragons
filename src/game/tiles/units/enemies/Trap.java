package game.tiles.units.enemies;

import game.tiles.units.player.Player;
import game.utils.Position;

public class Trap extends Enemy {
    private Integer visibilityTime;
    private Integer notvisibilityTime;
    private Integer tickCount;
    private boolean visible;
    private char OrgChar;

    public Trap(char tile, String name, int healthCapacity, int attack, int defense, Integer experience, Integer visibilityTime, Integer notvisibilityTime) {
        super(tile, name, healthCapacity, attack, defense, experience);
        this.visibilityTime = visibilityTime;
        this.notvisibilityTime = notvisibilityTime;
        this.tickCount = 0;
        this.OrgChar = tile;
        this.visible = true;
    }

    public Position enemyTurn(Player p) {
        this.tickCount++;
        boolean isCurrentlyVisible = (this.tickCount < this.visibilityTime);

        if (isCurrentlyVisible) {
            this.tileChar = this.OrgChar;
        } else {
            this.tileChar = '.';
        }
        if (this.tickCount >= this.visibilityTime + this.notvisibilityTime) {
            this.tickCount = 0;
            this.tileChar = this.OrgChar;
        }

        if (this.position.distance(p.getPosition()) < 2) {
            this.visit(p);
        }

        return this.position;
    }


}

