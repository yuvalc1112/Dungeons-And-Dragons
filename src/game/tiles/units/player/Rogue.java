package game.tiles.units.player;
import game.callBack.MessageCallback;

import game.tiles.units.Unit;
import game.tiles.units.enemies.Enemy;

import java.util.Iterator;
import java.util.List;

public class Rogue extends Player  {
    private int cost;
    private int currentEnergy;
    private Integer maxEnergy;
    private int attackRange;

    public Rogue(String name, int healthPool, int attack, int defense,  int cost ) {
        super(name, healthPool, attack, defense);
        this.cost = cost;
        this.currentEnergy = 100;
        this.maxEnergy = 100;
        this.attackRange = 2;
    }

    @Override
    public void castAbility(List<Enemy> enemies) {
        if (this.currentEnergy < this.cost) {
            this.messageCallback.send(String.format("%s tried to cast Fan of Knives but there was not enough energy: %d/%d",
                    this.getName(), this.currentEnergy, this.cost));
        } else {

            this.messageCallback.send(String.format("%s cast Fan of Knives.", this.getName()));
            this.currentEnergy -= this.cost;

            List<Enemy> enemiesInRange = this.enemiesAtAbilityRange(enemies, this.attackRange);

            if (enemiesInRange.isEmpty()) {
                this.messageCallback.send(this.getName() + "'s Fan of Knives hit nothing.");
            } else {
                for (Enemy enemyToHit : enemiesInRange) {
                    this.attackingWithAbility(this.attack, enemyToHit, enemyToHit.rollDefense());
                }
            }
        }

    }

    @Override
    public void OnGameTick() {
        this.currentEnergy = Math.min(this.currentEnergy + 10, this.maxEnergy);
    }

    public void levelUp() {
        super.levelUp();
        attack += (plevel * 3);
        currentEnergy = 100;
        MessageCallback MCB = this.messageCallback;
        String name = this.getName();
        MCB.send(name + " reached level " + (this.getPLevel() + 1) + " +" + this.plevel * 10 + " =Health" + this.currentAttack() + " =Attack" + this.currentDefense() + " =Defense.");
    }



    public String describe(){
        return String.format("%s\t\tEnergy: %s/%s ", super.describe(), this.currentEnergy, this.maxEnergy);
    }

}



