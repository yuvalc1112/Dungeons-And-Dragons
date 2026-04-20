package game.tiles.units.enemies;
import game.tiles.units.Unit;
import game.callBack.MessageCallback;
import game.tiles.units.player.Player;
import game.utils.Position;

public abstract class Enemy extends Unit{
    protected int ExperienceVal;


    public Enemy(char enemyChar,String name, int healthPool, int attackPosition, int defensePosition, int ExpirienceVal) {
        super(enemyChar,name, healthPool, attackPosition, defensePosition);
        this.ExperienceVal= ExpirienceVal;

    }
    public int getExperienceVal() {
        return ExperienceVal;
    }
    public void setExperienceVal(int expirienceVal) {
        ExperienceVal = expirienceVal;
    }
    public void onDeath(Player player) {
        MessageCallback MessCB = this.messageCallback;
        String name = this.getName();
        MessCB.send(name + " was killed by " + player.getName() + ".");

        if (deathCallBack != null) {
            deathCallBack.onDeath();
        }
    }
    public abstract Position enemyTurn (Player var1);

    public Position gameEnemyTick (Player p){
        return this.enemyTurn(p);
    }
    public void visit(Enemy e) {
        // Do nothing
    }

    public void accept(Unit u) {
        u.visit(this);
    }

    public void visit(Player player) {
        MessageCallback MesCB = this.messageCallback;
        String name = this.getName();
        MesCB.send(name + " engaged in combat with " + player.getName());
        this.messageCallback.send(this.describe());
        this.messageCallback.send(player.describe());
        int attack = this.rollAttack();
        int defense = player.rollDefense();
        this.attacking(attack, player, defense);
        if (player.isDead()) {
            player.onDeath(this);
        }

    }
}
