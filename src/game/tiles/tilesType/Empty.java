package game.tiles.tilesType;
import game.tiles.Tile;
import game.utils.Position;
import game.tiles.units.Unit;


public class Empty extends Tile {
    public Empty() {
        super('.');
    }
    public Empty(Position p) {
        super('.');
        this.position = p;
    }

    @Override
    public void accept(Unit unit) {
        unit.visit(this);
    }
}