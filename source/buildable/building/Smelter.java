package source.buildable.building;

import source.buildable.Building;
import source.item.Item;
import source.item.ItemManager;
import source.main.GamePanel;
import source.main.Viewport;
import source.tile.Tile;
import source.tile.TileManager;

public class Smelter extends Building {

	public Smelter(int x, int y, Tile tile, GamePanel gamePanel, TileManager tileManager, ItemManager itemManager, Viewport viewport) {
		super(x, y, tile, gamePanel, tileManager, itemManager, viewport);
		this.addConveyor();
		this.allowAutoRotation = true;
		this.inputItem = "iron_ore";
		this.outputItem = "iron_ingot";
	}

	@Override
	public void processItem(Item item) {
		super.processItem(item);

		if (item.name == inputItem) {
			Item output = new Item(item.x, item.y, outputItem, itemManager.itemTextures.get(outputItem), gamePanel, tileManager, viewport, itemManager);
			itemManager.items.remove(item);
			itemManager.items.add(output);
		}
	}
	
}
