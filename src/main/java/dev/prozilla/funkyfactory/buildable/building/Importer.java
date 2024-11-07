package dev.prozilla.funkyfactory.buildable.building;

import dev.prozilla.funkyfactory.UI.Clickable;
import dev.prozilla.funkyfactory.UI.UI;
import dev.prozilla.funkyfactory.UI.UIElement;
import dev.prozilla.funkyfactory.UI.modal.ItemPicker;
import dev.prozilla.funkyfactory.buildable.Building;
import dev.prozilla.funkyfactory.item.AbstractItem;
import dev.prozilla.funkyfactory.item.Item;
import dev.prozilla.funkyfactory.item.ItemManager;
import dev.prozilla.funkyfactory.main.GamePanel;
import dev.prozilla.funkyfactory.main.Viewport;
import dev.prozilla.funkyfactory.tile.Tile;
import dev.prozilla.funkyfactory.tile.TileManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Importer extends Building {
	
	float itemSpawnDelay = 1.5f;
	double timeUntilNextSpawn = itemSpawnDelay;
	
	int itemsSpawned = 0;
	boolean stopSpawning = false;
	
	String itemName = Item.IRON_ORE;
	
	ItemPicker itemPicker;
	
	public Importer(int x, int y, int direction, Tile tile, GamePanel gamePanel, TileManager tileManager, ItemManager itemManager, Viewport viewport) {
		super("Importer", x, y, tile, true, gamePanel, tileManager, itemManager, viewport);
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
				timeUntilNextSpawn -= gamePanel.scaledDeltaTime / gamePanel.fps;
				
				if (timeUntilNextSpawn <= 0) {
					timeUntilNextSpawn = itemSpawnDelay;
					
					if (!stopSpawning) {
						boolean clogged = false;
						for (int i = 0; i < itemManager.items.size(); i++) {
							Item item = itemManager.items.get(i);
							if (coordinate.equals(item.coordinate)) {
								clogged = true;
								break;
							}
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
	
	public void setItem(String name) {
		itemName = name;
		
		if (itemPicker != null)
			itemPicker.setItem(name);
	}
	
	@Override
	public void openModal() {
		ArrayList<AbstractItem> possiblItems = new ArrayList<AbstractItem>(Arrays.asList(
				ItemManager.abstractItems.get(Item.IRON_ORE),
				ItemManager.abstractItems.get(Item.COPPER_ORE)
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
