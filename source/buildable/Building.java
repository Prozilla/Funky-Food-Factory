package source.buildable;

import source.item.Item;
import source.item.ItemManager;
import source.main.GamePanel;
import source.tile.Tile;
import source.tile.TileManager;

public class Building extends Buildable {

	public ItemManager itemManager;

	public Building(int x, int y, Tile tile, GamePanel gamePanel, TileManager tileManager, ItemManager itemManager) {
		super(x, y, tile, gamePanel, tileManager);
		this.itemManager = itemManager;
	}

	public void processItem(Item item) {

	}
	
}
