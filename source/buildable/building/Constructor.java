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

		this.possibleRecipes.add(new Recipe(ItemManager.abstractItems.get("tomato"), ItemManager.abstractItems.get("tomato_sauce")));
		this.possibleRecipes.add(new Recipe(ItemManager.abstractItems.get("lettuce"), ItemManager.abstractItems.get("salad")));

		this.activeRecipe = this.possibleRecipes.get(0);
	}
	
}
