package dev.prozilla.funkyfactory.buildable.building;

import dev.prozilla.funkyfactory.buildable.Building;
import dev.prozilla.funkyfactory.buildable.building.recipe.Recipe;
import dev.prozilla.funkyfactory.item.Item;
import dev.prozilla.funkyfactory.item.ItemManager;
import dev.prozilla.funkyfactory.main.GamePanel;
import dev.prozilla.funkyfactory.main.Viewport;
import dev.prozilla.funkyfactory.tile.Tile;
import dev.prozilla.funkyfactory.tile.TileManager;

public class Smelter extends Building {
	
	public Smelter(int x, int y, Tile tile, GamePanel gamePanel, TileManager tileManager, ItemManager itemManager, Viewport viewport) {
		super("Smelter", x, y, tile, true, gamePanel, tileManager, itemManager, viewport);
		this.addConveyor();
		this.allowAutoRotation = true;
		
		this.possibleRecipes.add(new Recipe(ItemManager.abstractItems.get(Item.IRON_ORE), ItemManager.abstractItems.get(Item.IRON_INGOT)));
		this.possibleRecipes.add(new Recipe(ItemManager.abstractItems.get(Item.COPPER_ORE), ItemManager.abstractItems.get(Item.COPPER_INGOT)));
		
		this.activeRecipe = this.possibleRecipes.get(0);
	}
	
}
