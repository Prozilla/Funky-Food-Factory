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
		super("Exporter", x, y, tile, gamePanel, tileManager, itemManager, viewport);
		this.addConveyor();
		this.setConnection(false, -2);
		this.setConnection(true, (direction + 2) % 4);
	}

	@Override
	public void processItem(Item item) {
		itemManager.items.remove(item);

		int reward = 0;

		switch (item.name) {
			case "tomato":
			case "corn":
			case "lettuce":
			case "melon":
				reward = 1;
				break;
			case "tomato_sauce":
			case "salad":
				reward = 2;
				break;
			case "melon_slice":
				reward = 3;
				break;
			case "pizza":
				reward = 6;
				break;
		}

		gamePanel.addScore(reward);
	}
	
}
