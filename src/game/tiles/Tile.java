package game.tiles;
import game.utils.Position;
import game.tiles.units.Unit;
import game.callBack.DeathCallback;
import game.callBack.MessageCallback;

public abstract class Tile implements Comparable<Tile> {
    protected char tileChar;
    protected Position position;
    protected DeathCallback deathCallBack;
    protected MessageCallback messageCallback;

    public Tile(char tile){
        this.tileChar = tile;
    }

    public void initialize(Position p){
        this.position = p;

    }


    public char getTileChar() {
        return tileChar;
    }

    public Position getPosition() {
        return position;
    }
    public void setPosition(Position position) {
        this.position = position;
    }
    public int compareTo(Tile tile) {
        return this.getPosition().compareTo(tile.getPosition());
    }


    public abstract void accept(Unit unit);

    @Override
    public String toString() {
        return String.valueOf(tileChar);
    }
    public void swap(Tile t1, Tile t2) {
        Position p = new Position(t1.getPosition().getX(), t1.getPosition().getY());
        t1.setPosition(t2.getPosition());
        t2.setPosition(p);
    }
}