package source.game.buildable.building;

import source.game.buildable.Building;
import source.game.buildable.building.recipe.Recipe;
import source.game.tile.Tile;
import source.game.tile.TileManager;
import source.game.item.Item;
import source.game.item.ItemManager;
import source.game.main.GamePanel;
import source.game.main.Viewport;

public class Cutter extends Building {

	public Cutter(int x, int y, Tile tile, GamePanel gamePanel, TileManager tileManager, ItemManager itemManager, Viewport viewport) {
		super("Cutter", x, y, tile, true, gamePanel, tileManager, itemManager, viewport);
		this.addConveyor();
		this.allowAutoRotation = true;

		this.possibleRecipes.add(new Recipe(ItemManager.abstractItems.get(Item.MELON), ItemManager.abstractItems.get(Item.MELON_SLICE)));

		this.activeRecipe = this.possibleRecipes.get(0);
	}
	
}
