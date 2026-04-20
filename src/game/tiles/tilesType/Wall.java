package game.tiles.tilesType;
import game.tiles.Tile;
import game.utils.Position;
import game.tiles.units.Unit;

public class Wall extends Tile {
    public Wall() {
        super('#');
    }
    public Wall(Position p) {
        super('#');
        this.position = p;
    }

    @Override
    public void accept(Unit unit) {

    }
}