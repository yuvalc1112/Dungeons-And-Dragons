package game.tiles.units;
import game.tiles.Tile;
import game.utils.Position;
import game.utils.Resource;
import game.tiles.units.player.Player;
import game.callBack.DeathCallback;
import game.callBack.MessageCallback;
import game.tiles.units.enemies.Enemy;
import game.tiles.tilesType.Empty;
import game.tiles.tilesType.Wall;




public abstract class Unit extends Tile {
    protected String name;
    protected int attack;
    protected int defense;
    protected Resource health;


    protected DeathCallback deathCallback;
    protected MessageCallback messageCallback;


    public Unit(char tileChar,String name, int healthPool, int attack, int defense) {
        super(tileChar);
        this.health = new Resource(healthPool, healthPool);
        this.name = name;
        this.attack = attack;
        this.defense = defense;

    }

    public char getTileChar() {
        return tileChar;
    }
    public int getAttack() {
        return attack;
    }

    public String getName() {
        return name;
    }

    public int getDefense() {
        return defense;
    }
    public Resource getHealth() {
        return health;
    }
    public boolean isAlive() {
        return health.getCurrent() > 0;
    }
    public Boolean isDead() {
        return this.getHealth().getCurrent() <= 0;
    }

    public int rollAttack() {
        int attackRoll = (int)(Math.random() * (double)(this.getAttack() + 1));
        messageCallback.send(name + " rolled " + attackRoll + " attack points");
        return attackRoll;
    }
    public int rollDefense() {
        int defenseRoll = (int)(Math.random() * (double)(this.getDefense() + 1));
        messageCallback.send(name + " rolled " + defenseRoll + " defense points");
        return defenseRoll;
    }
    public void setDeathCallBack(DeathCallback deathCallBack) {
        this.deathCallBack = deathCallBack;
    }

    public void SetMessageCallBack(MessageCallback messageCallback) {
        this.messageCallback = messageCallback;
    }

public void attacking(int attackRoll, Unit defender, int defenseRoll) {
    int potentialDamage = attackRoll - defenseRoll;

    int actualDamage = Math.max(0, potentialDamage);
    if (actualDamage > 0) {
        defender.getHealth().reduceCurrent(actualDamage);

        this.messageCallback.send(this.getName() + " dealt " + actualDamage + " damage to " + defender.getName());
    } else {
        this.messageCallback.send(this.getName() + "'s attack was blocked by " + defender.getName() + "'s defense!");
    }
}


    public void interact(Tile tile) {
        tile.accept(this);
    }

    public void visit(Empty tile){
        this.swap(this, tile);
    }

    public void visit(Wall tile){}
    public abstract void visit(Player Player );
    public abstract void visit(Enemy Enemy );
    public abstract void accept(Unit unit);

    public String describe() {
        return String.format("%s\t\tHealth: %s\t\tAttack: %d\t\tDefense: %d", this.getName(), this.getHealth(), this.getAttack(), this.getDefense());
    }
}