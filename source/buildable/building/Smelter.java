package source.buildable.building;

import source.buildable.Building;
import source.buildable.building.recipe.Recipe;
import source.item.ItemManager;
import source.main.GamePanel;
import source.main.Viewport;
import source.tile.Tile;
import source.tile.TileManager;

public class Smelter extends Building {

	public Smelter(int x, int y, Tile tile, GamePanel gamePanel, TileManager tileManager, ItemManager itemManager, Viewport viewport) {
		super("Smelter", x, y, tile, gamePanel, tileManager, itemManager, viewport);
		this.addConveyor();
		this.allowAutoRotation = true;

		this.possibleRecipes.add(new Recipe(ItemManager.abstractItems.get("iron_ore"), ItemManager.abstractItems.get("iron_ingot")));
		this.possibleRecipes.add(new Recipe(ItemManager.abstractItems.get("copper_ore"), ItemManager.abstractItems.get("copper_ingot")));

		this.activeRecipe = this.possibleRecipes.get(0);
	}
	
}
