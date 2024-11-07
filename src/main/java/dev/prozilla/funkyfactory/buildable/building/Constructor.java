package dev.prozilla.funkyfactory.buildable.building;

import dev.prozilla.funkyfactory.buildable.Building;
import dev.prozilla.funkyfactory.buildable.building.recipe.Recipe;
import dev.prozilla.funkyfactory.item.Item;
import dev.prozilla.funkyfactory.item.ItemManager;
import dev.prozilla.funkyfactory.main.GamePanel;
import dev.prozilla.funkyfactory.main.Viewport;
import dev.prozilla.funkyfactory.tile.Tile;
import dev.prozilla.funkyfactory.tile.TileManager;

public class Constructor extends Building {
	
	public Constructor(int x, int y, Tile tile, GamePanel gamePanel, TileManager tileManager, ItemManager itemManager, Viewport viewport) {
		super("Constructor", x, y, tile, true, gamePanel, tileManager, itemManager, viewport);
		this.addConveyor();
		this.allowAutoRotation = true;
		
		this.possibleRecipes.add(new Recipe(ItemManager.abstractItems.get(Item.IRON_INGOT), ItemManager.abstractItems.get(Item.IRON_SCREW)));
		this.possibleRecipes.add(new Recipe(ItemManager.abstractItems.get(Item.COPPER_INGOT), ItemManager.abstractItems.get(Item.COPPER_ROD)));
		
		this.activeRecipe = this.possibleRecipes.get(0);
	}
	
}
