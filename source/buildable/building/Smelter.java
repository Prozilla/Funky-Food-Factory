package source.buildable.building;

import source.buildable.Building;
import source.item.Item;
import source.item.ItemManager;
import source.main.GamePanel;
import source.tile.Tile;
import source.tile.TileManager;

public class Smelter extends Building {

	public Smelter(int x, int y, int rotation, Tile tile, GamePanel gamePanel, TileManager tileManager, ItemManager itemManager) {
		super(x, y, tile, gamePanel, tileManager, itemManager);
		this.rotation = rotation;
		this.addConveyor(this.rotation);
	}

	@Override
	public void processItem(Item item) {
		String outputName = item.name;

		switch (item.name) {
			case "iron_ore":
				outputName = "iron_ingot";
				break;
		}

		if (outputName != item.name) {
			Item outputItem = new Item(item.x, item.y, outputName, itemManager.itemTextures.get(outputName), gamePanel, tileManager);
			itemManager.items.remove(item);
			itemManager.items.add(outputItem);
		}
	}
	
}
