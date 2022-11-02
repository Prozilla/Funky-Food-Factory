package source.buildable.building;

import source.buildable.Building;
import source.buildable.building.recipe.Recipe;
import source.item.Item;
import source.item.ItemManager;
import source.main.GamePanel;
import source.main.Viewport;
import source.tile.Tile;
import source.tile.TileManager;

public class Cutter extends Building {

	public Cutter(int x, int y, Tile tile, GamePanel gamePanel, TileManager tileManager, ItemManager itemManager, Viewport viewport) {
		super("Cutter", x, y, tile, true, gamePanel, tileManager, itemManager, viewport);
		this.addConveyor();
		this.allowAutoRotation = true;

		this.possibleRecipes.add(new Recipe(ItemManager.abstractItems.get(Item.MELON), ItemManager.abstractItems.get(Item.MELON_SLICE)));

		this.activeRecipe = this.possibleRecipes.get(0);
	}
	
}
