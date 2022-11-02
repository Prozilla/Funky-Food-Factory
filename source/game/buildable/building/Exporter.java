package source.game.buildable.building;

import source.game.buildable.Building;
import source.game.tile.Tile;
import source.game.tile.TileManager;
import source.game.item.Item;
import source.game.item.ItemManager;
import source.game.main.GamePanel;
import source.game.main.Viewport;

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
			case Item.TOMATO:
			case Item.CORN:
			case Item.LETTUCE:
			case Item.MELON:
				reward = 1;
				break;
			case Item.TOMATO_SAUCE:
			case Item.SALAD:
				reward = 2;
				break;
			case Item.MELON_SLICE:
				reward = 3;
				break;
			case Item.PIZZA:
				reward = 6;
				break;
		}

		gamePanel.addScore(reward);
	}
	
}
