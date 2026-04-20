package game.tiles.units.player;
import game.tiles.units.Unit;
import game.callBack.MessageCallback;
import game.tiles.units.enemies.Enemy;
import game.utils.Position;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public abstract class Player extends Unit {
    public static final char PLAYER_CHAR = '@';
    protected static final int REQUIRED_EXP = 50;
    protected static final int ATTACK_BONUS = 4;
    protected static final int DEFENSE_BONUS = 1;
    protected static final int HEALTH_BONUS = 10;
    protected int experience;
    protected int plevel;

    protected Player(String name, int healthPool, int attack, int defense) {
        super(PLAYER_CHAR,name, healthPool, attack, defense);
        this.experience = 0;
        this.plevel = 1;
    }

    public void addExperience(int exp) {
        this.experience += exp;
        int nextLevelExp = levelUpRequ();
        while (this.experience >= nextLevelExp) {
            levelUp();
            this.experience -= levelUpRequ();
            nextLevelExp = levelUpRequ();
        }
    }
    public void attackingWithAbility(int attack, Enemy e, int defense) {
        int damage = attack - defense;
        if (damage > 0) {
            MessageCallback MSB = this.messageCallback;
            String name = this.getName();
            MSB.send(name + " hit " + e.getName() + " for " + damage + " ability damage");
            e.getHealth().setCurrent(e.getHealth().getCurrent() - damage);
        }

        if (e.isDead()) {
            e.onDeath(this);
        }

    }
    public List<Enemy> enemiesAtAbilityRange(List<Enemy> enemies, int range) {
        List<Enemy> enemiesAbilityRange = new ArrayList();
        Iterator itr = enemies.iterator();

        while(itr.hasNext()) {
            Enemy e = (Enemy)itr.next();
            if (this.getPosition().distance(e.getPosition()) < range) {
                enemiesAbilityRange.add(e);
            }
        }

        return enemiesAbilityRange;
    }
    public abstract void castAbility(List<Enemy> en);
    public abstract void OnGameTick();


    public void levelUp(){
        this.plevel++;
        health.addMax(currentHealth());
        health.reHelth();
        attack += currentAttack();
        defense += currentDefense();
        //להוסיף הדפסה
    }
    public void onDeath(Enemy e) {
        this.tileChar = 'X';
        MessageCallback MCB = this.messageCallback;
        String name = this.getName();
        MCB.send(name + " was killed by " + e.getName());
        this.messageCallback.send("You lost.");
        if (deathCallback != null) {
            deathCallback.onDeath();
        }
    }

    private int levelUpRequ(){
        return REQUIRED_EXP * plevel;

    }
    public int getExperience() {
        return experience;
    }
    public int getPLevel() {
        return plevel;
    }
    protected int currentHealth() {
        return getPLevel() * HEALTH_BONUS;
    }
    protected int currentAttack() {
        return getPLevel() * ATTACK_BONUS;
    }
    protected int currentDefense() {
        return getPLevel() * DEFENSE_BONUS;
    }
    public void visit(Enemy enemy) {
        this.messageCallback.send(this.getName() + " engaged in combat with " + enemy.getName());
        this.messageCallback.send(this.describe());
        this.messageCallback.send(enemy.describe());
        int attack = this.rollAttack();
        int defense = enemy.rollDefense();
        attacking(attack, enemy, defense);

        if (enemy.isDead()) {
            enemy.onDeath(this);
        }

    }
    public void visit(Player player) {
        // Players do not interact with each other in this context
        // This method can be overridden in subclasses if needed
    }

    public void accept(Unit u) {
        u.visit(this);
    }



}