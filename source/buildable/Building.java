package source.buildable;

import java.awt.Point;
import java.util.ArrayList;

import source.UI.Clickable;
import source.UI.UI;
import source.UI.UIElement;
import source.UI.modal.RecipePicker;
import source.buildable.building.recipe.Recipe;
import source.item.Item;
import source.item.ItemManager;
import source.main.GamePanel;
import source.main.Viewport;
import source.tile.Tile;
import source.tile.TileManager;

public class Building extends Buildable {

	public String name;
	public Recipe activeRecipe;
	public ArrayList<Recipe> possibleRecipes = new ArrayList<Recipe>();

	public ItemManager itemManager;

	public Building(String name, int x, int y, Tile tile, GamePanel gamePanel, TileManager tileManager, ItemManager itemManager, Viewport viewport) {
		super(x, y, tile, gamePanel, tileManager, viewport);

		this.name = name;
		this.itemManager = itemManager;
	}

	public void processItem(Item item) {
		item.lastBuilding = this;

		// TO do: If recipe is null and recipe is required, clog conveyor
		if (activeRecipe != null && item.name == activeRecipe.inputItem.name) {
			Item output = new Item(item.x, item.y, activeRecipe.outputItem, gamePanel, tileManager, viewport, itemManager);
			itemManager.items.remove(item);
			itemManager.items.add(output);
		}
	}

	public void setRecipe(int index) {
		if (index >= possibleRecipes.size())
			return;

		activeRecipe = possibleRecipes.get(index);
	}

	public void openModal() {
		if (activeRecipe != null) {
			Clickable clickable = new Clickable() {
				@Override
				public void onClick(UIElement element) {
					int recipeIndex = Integer.parseInt(element.name.replace("recipe", ""));
					setRecipe(recipeIndex);
				}
			};

			UI.currentModal = new RecipePicker(name, new Point(x + GamePanel.tileSize, y), possibleRecipes, activeRecipe, clickable);
		} else {
			UI.currentModal = null;
		}
	}
	
}
