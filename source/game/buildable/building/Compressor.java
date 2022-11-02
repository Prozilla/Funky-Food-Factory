package source.game.buildable.building;

import source.game.buildable.Building;
import source.game.buildable.building.recipe.Recipe;
import source.game.tile.Tile;
import source.game.tile.TileManager;
import source.game.item.Item;
import source.game.item.ItemManager;
import source.game.main.GamePanel;
import source.game.main.Viewport;

public class Compressor extends Building {

	public Compressor(int x, int y, Tile tile, GamePanel gamePanel, TileManager tileManager, ItemManager itemManager, Viewport viewport) {
		super("Compressor", x, y, tile, true, gamePanel, tileManager, itemManager, viewport);
		this.addConveyor();
		this.allowAutoRotation = true;

		this.possibleRecipes.add(new Recipe(ItemManager.abstractItems.get(Item.TOMATO), ItemManager.abstractItems.get(Item.TOMATO_SAUCE)));
		this.possibleRecipes.add(new Recipe(ItemManager.abstractItems.get(Item.LETTUCE), ItemManager.abstractItems.get(Item.SALAD)));

		this.activeRecipe = this.possibleRecipes.get(0);
	}
	
}
