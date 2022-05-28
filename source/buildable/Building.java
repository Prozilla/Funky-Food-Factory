package source.buildable;

import source.item.Item;
import source.item.ItemManager;
import source.main.GamePanel;
import source.main.Viewport;
import source.tile.Tile;
import source.tile.TileManager;

public class Building extends Buildable {

	public String inputItem;
	public String outputItem;

	public ItemManager itemManager;

	public Building(int x, int y, Tile tile, GamePanel gamePanel, TileManager tileManager, ItemManager itemManager, Viewport viewport) {
		super(x, y, tile, gamePanel, tileManager, viewport);
		this.itemManager = itemManager;
	}

	public void processItem(Item item) {
		item.lastBuilding = this;
	}
	
}
