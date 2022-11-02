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
		super("Smelter", x, y, tile, true, gamePanel, tileManager, itemManager, viewport);
		this.addConveyor();
		this.allowAutoRotation = true;

		this.possibleRecipes.add(new Recipe(ItemManager.abstractItems.get("tomato_sauce"), ItemManager.abstractItems.get("pizza")));
		this.possibleRecipes.add(new Recipe(ItemManager.abstractItems.get("corn"), ItemManager.abstractItems.get("popcorn")));

		this.activeRecipe = this.possibleRecipes.get(0);
	}
	
}
