package game.gameManager;
import game.tiles.Tile;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Board {
    private List<Tile> tiles = new ArrayList();

    public Board() {
    }

    public void add(Tile t) {
        this.tiles.add(t);
    }

    public Tile GetTile(int x, int y) {
        Iterator<Tile> iterator = this.tiles.iterator();
        while (iterator.hasNext()) {
            Tile t = iterator.next();
            if (t.getPosition().x == x && t.getPosition().y == y) {
                return t;
            }
        }
        return null;
    }

    public void Remove(Tile toremove) {
        this.tiles.remove(toremove);
    }

    public String PrintBoard() {
        this.sortTiles();
        String board = "";

        Tile t;
        for(Iterator var2 = this.tiles.iterator(); var2.hasNext(); board = board + t.toString()) {
            t = (Tile)var2.next();
            if (t.getPosition().x == 0) {
                board = board + "\n";
            }
        }

        return board;
    }

    private void sortTiles() {
        this.tiles.sort(Tile::compareTo);
    }
}
