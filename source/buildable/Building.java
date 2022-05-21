package source.buildable;

import source.item.Item;
import source.main.GamePanel;
import source.tile.Tile;
import source.tile.TileManager;

public class Building extends Buildable {

	public Building(int x, int y, Tile tile, GamePanel gamePanel, TileManager tileManager) {
		super(x, y, tile, gamePanel, tileManager);
	}

	public void processItem(Item item) {

	}
	
}
