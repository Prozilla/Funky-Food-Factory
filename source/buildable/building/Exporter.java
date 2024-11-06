package source.buildable.building;

import source.buildable.Building;
import source.item.Item;
import source.item.ItemManager;
import source.main.GamePanel;
import source.main.Viewport;
import source.tile.Tile;
import source.tile.TileManager;

public class Exporter extends Building {
	
	public Exporter(int x, int y, int direction, Tile tile, GamePanel gamePanel, TileManager tileManager, ItemManager itemManager, Viewport viewport) {
		super("Exporter", x, y, tile, false, gamePanel, tileManager, itemManager, viewport);
		this.addConveyor();
		this.setConnection(false, -2);
		this.setConnection(true, (direction + 2) % 4);
	}
	
	@Override
	public void processItem(Item item) {
		itemManager.items.remove(item);
		
		int reward = 0;
		
		switch (item.name) {
			case Item.COPPER_ORE:
			case Item.IRON_ORE:
				reward = 1;
				break;
			case Item.COPPER_INGOT:
			case Item.IRON_INGOT:
				reward = 2;
				break;
			case Item.COPPER_ROD:
			case Item.IRON_SCREW:
				reward = 4;
				break;
		}
		
		gamePanel.addScore(reward);
	}
	
}
