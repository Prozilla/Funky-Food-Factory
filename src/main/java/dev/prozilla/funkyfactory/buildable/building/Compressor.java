package dev.prozilla.funkyfactory.buildable.building;

import dev.prozilla.funkyfactory.buildable.Building;
import dev.prozilla.funkyfactory.item.ItemManager;
import dev.prozilla.funkyfactory.main.GamePanel;
import dev.prozilla.funkyfactory.main.Viewport;
import dev.prozilla.funkyfactory.tile.Tile;
import dev.prozilla.funkyfactory.tile.TileManager;

public class Compressor extends Building {
	
	public Compressor(int x, int y, Tile tile, GamePanel gamePanel, TileManager tileManager, ItemManager itemManager, Viewport viewport) {
		super("Compressor", x, y, tile, true, gamePanel, tileManager, itemManager, viewport);
		this.addConveyor();
		this.allowAutoRotation = true;
		
		// this.possibleRecipes.add(new Recipe(ItemManager.abstractItems.get(Item.TOMATO), ItemManager.abstractItems.get(Item.TOMATO_SAUCE)));
		// this.possibleRecipes.add(new Recipe(ItemManager.abstractItems.get(Item.LETTUCE), ItemManager.abstractItems.get(Item.SALAD)));
		
		// this.activeRecipe = this.possibleRecipes.get(0);
	}
	
}
