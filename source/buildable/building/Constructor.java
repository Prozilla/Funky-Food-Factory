package source.buildable.building;

import source.buildable.Building;
import source.buildable.building.recipe.Recipe;
import source.item.ItemManager;
import source.main.GamePanel;
import source.main.Viewport;
import source.tile.Tile;
import source.tile.TileManager;

public class Constructor extends Building {

	public Constructor(int x, int y, Tile tile, GamePanel gamePanel, TileManager tileManager, ItemManager itemManager, Viewport viewport) {
		super("Constructor", x, y, tile, gamePanel, tileManager, itemManager, viewport);
		this.addConveyor();
		this.allowAutoRotation = true;

		this.possibleRecipes.add(new Recipe(ItemManager.abstractItems.get("iron_ingot"), ItemManager.abstractItems.get("iron_screw")));
		this.possibleRecipes.add(new Recipe(ItemManager.abstractItems.get("copper_ingot"), ItemManager.abstractItems.get("copper_sheet")));
		this.possibleRecipes.add(new Recipe(ItemManager.abstractItems.get("uranium_ore"), ItemManager.abstractItems.get("uranium_cell")));
		this.possibleRecipes.add(new Recipe(ItemManager.abstractItems.get("uranium_cell"), ItemManager.abstractItems.get("uranium_fuel_rod")));

		this.activeRecipe = this.possibleRecipes.get(0);
	}
	
}
