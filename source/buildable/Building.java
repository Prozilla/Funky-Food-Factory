package source.buildable;

import java.awt.Point;
import java.util.ArrayList;

import source.UI.UI;
import source.UI.modal.RecipePicker;
import source.buildable.building.recipe.Recipe;
import source.item.Item;
import source.item.ItemManager;
import source.main.GamePanel;
import source.main.Viewport;
import source.tile.Tile;
import source.tile.TileManager;

public class Building extends Buildable {

	public Recipe recipe;
	public ArrayList<Recipe> possibleRecipes = new ArrayList<Recipe>();

	public ItemManager itemManager;

	public Building(int x, int y, Tile tile, GamePanel gamePanel, TileManager tileManager, ItemManager itemManager, Viewport viewport) {
		super(x, y, tile, gamePanel, tileManager, viewport);
		this.itemManager = itemManager;
	}

	public void processItem(Item item) {
		item.lastBuilding = this;

		// TO do: If recipe is null and recipe is required, clog conveyor
		if (recipe != null && item.name == recipe.inputItem.name) {
			Item output = new Item(item.x, item.y, recipe.outputItem, gamePanel, tileManager, viewport, itemManager);
			itemManager.items.remove(item);
			itemManager.items.add(output);
		}
	}

	public void openModal() {
		if (recipe != null) {
			UI.currentModal = new RecipePicker(String.format("%s > %s", recipe.inputItem.name, recipe.outputItem.name), new Point(x + GamePanel.tileSize, y), recipe);
		} else {
			UI.currentModal = null;
		}
	}
	
}
