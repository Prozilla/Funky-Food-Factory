package source.buildable.building;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import source.UI.Clickable;
import source.UI.UI;
import source.UI.UIElement;
import source.UI.modal.ItemPicker;
import source.buildable.Building;
import source.item.AbstractItem;
import source.item.Item;
import source.item.ItemManager;
import source.main.GamePanel;
import source.main.Viewport;
import source.tile.Tile;
import source.tile.TileManager;

public class Importer extends Building {

	float itemSpawnDelay = 1.5f;
	double timeUntilNextSpawn = itemSpawnDelay;

	int itemsSpawned = 0;
	boolean stopSpawning = false;

	String itemName = "iron_ore";

	ItemPicker itemPicker;

	public Importer(int x, int y, int direction, Tile tile, GamePanel gamePanel, TileManager tileManager, ItemManager itemManager, Viewport viewport) {
		super("Importer", x, y, tile, gamePanel, tileManager, itemManager, viewport);
		this.input = -2;
		this.output = direction;
		this.addConveyor();
		this.setConnection(true, -2);
		this.setConnection(false, direction);
	}

	@Override
	public void draw(Graphics2D graphics2D, boolean isGhost) {
		if (!isGhost) {
			Point coordinateOffset = TileManager.moveInDirection(output, 1);
			Point neighbourCoordinate = new Point(coordinate.x + coordinateOffset.x, coordinate.y + coordinateOffset.y);

			if (tileManager.coordinateToBuildable.containsKey(neighbourCoordinate)) {
				timeUntilNextSpawn -= gamePanel.deltaTime / gamePanel.fps;

				if (timeUntilNextSpawn <= 0) {
					timeUntilNextSpawn = itemSpawnDelay;

					if (!stopSpawning) {
						boolean clogged = false;
						for (int i = 0; i < itemManager.items.size(); i++) {
							Item item = itemManager.items.get(i);
							if (coordinate.equals(item.coordinate))
								clogged = true;
						}

						if (!clogged) {
							itemManager.spawnItem(itemName, coordinate);
							itemsSpawned++;

							if (itemsSpawned == 1) {
								// stopSpawning = true;
							}
						}
					}
				}
			} else {
				timeUntilNextSpawn = itemSpawnDelay;
			}
		}

		super.draw(graphics2D, isGhost);
	}

	void setItem(String name) {
		itemName = name;
		itemPicker.setItem(name);
	}

	@Override
	public void openModal() {
		ArrayList<AbstractItem> possiblItems = new ArrayList<AbstractItem>(Arrays.asList(
			ItemManager.abstractItems.get("iron_ore"), 
			ItemManager.abstractItems.get("copper_ore"),
			ItemManager.abstractItems.get("uranium_ore")
		));

		Clickable clickable = new Clickable() {
			@Override
			public void onClick(UIElement element) {
				setItem(element.name);
			}
		};

		itemPicker = new ItemPicker(name, new Point(x + GamePanel.tileSize, y), possiblItems, ItemManager.abstractItems.get(itemName), clickable);
		UI.currentModal = itemPicker;
	}
	
}
