package game.tiles.units.player;
import game.callBack.MessageCallback;
import game.tiles.tilesType.Empty;
import game.tiles.tilesType.Wall;
import game.tiles.units.Unit;
import game.tiles.units.enemies.Enemy;
import java.util.List;

public class Warrior extends Player {
    private int abilityColldown;
    private int remainingCooldwon;
    protected static final int extra_attack = 2;
    protected static final int extra_health = 5;


    public Warrior(String name , int healthPool, int attack, int defense, int coolDown) {
        super(name, healthPool, attack, defense);
        this.abilityColldown = coolDown;
        this.remainingCooldwon = 0;

    }

    @Override
    public void castAbility(List<Enemy> en) {
        MessageCallback messageCallback;
        String msg;
        if (this.remainingCooldwon > 0) {
            messageCallback = this.messageCallback;
            msg = this.getName();
            messageCallback.send(msg + "  tried to cast Avenger’s Shield but there is a cool down: " + this.remainingCooldwon);
        }
        else {

            this.remainingCooldwon = this.abilityColldown;
            messageCallback = this.messageCallback;
            msg = this.getName();
            messageCallback.send(msg + " cast Avenger’s Shield healing for " + 10 * this.defense);
            this.remainingCooldwon = this.abilityColldown + 1;
            List<Enemy> nearEnemies = this.enemiesAtAbilityRange(en, 3);
            if (!nearEnemies.isEmpty()) {
                Enemy enemy = (Enemy)nearEnemies.get((int)(Math.random() * (double)nearEnemies.size()));
                this.attackingWithAbility((int)(0.1 * (double)this.health.getMax()), enemy, enemy.rollDefense());
            }
            //update warrior health amount
            this.health.setCurrent(this.health.getCurrent() + 10 * this.defense);
        }


    }

    @Override
    public void OnGameTick() {
        if (this.remainingCooldwon > 0) {
            this.remainingCooldwon -= 1;
        }
    }
    public int getRemainingCooldown() {
        return this.remainingCooldwon;
    }
    @Override
    public void levelUp() {
       super.levelUp();
        remainingCooldwon =0;
        health.addMax((plevel * extra_health));
        attack += (plevel*extra_attack);
        defense += (plevel);
        MessageCallback messageCallback = this.messageCallback;
        String msg = this.getName();
        messageCallback.send(msg + " reached level :" + (this.getPLevel()) + " +" + this.health + " =Health" + this.attack + " =Attack" + this.defense + " =Defense");

    }
    public void gameTick(){
        if (remainingCooldwon > 0) {
            remainingCooldwon--;
        }
    }


    @Override
    public void visit(Wall tile) {

    }

    @Override
    public String describe() {
        return String.format("%s\t\tCooldown: %s/%s ", super.describe(), this.remainingCooldwon, this.abilityColldown);
    }

}
