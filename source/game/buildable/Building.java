package source.game.buildable;

import java.awt.Point;
import java.util.ArrayList;

import source.game.UI.Clickable;
import source.game.UI.UI;
import source.game.UI.UIElement;
import source.game.UI.modal.RecipePicker;
import source.game.buildable.building.recipe.Recipe;
import source.game.tile.Tile;
import source.game.tile.TileManager;
import source.game.item.Item;
import source.game.item.ItemManager;
import source.game.main.GamePanel;
import source.game.main.Viewport;

public class Building extends Buildable {

	public String name;
	public Recipe activeRecipe;
	public ArrayList<Recipe> possibleRecipes = new ArrayList<Recipe>();
	public boolean hasModal;

	RecipePicker recipePicker;

	public ItemManager itemManager;

	public Building(String name, int x, int y, Tile tile, Boolean hasModal, GamePanel gamePanel, TileManager tileManager, ItemManager itemManager, Viewport viewport) {
		super(x, y, tile, gamePanel, tileManager, viewport);

		this.name = name;
		this.hasModal = hasModal;
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
		recipePicker.setRecipe(index);
	}

	public void openModal() {
		if (hasModal && activeRecipe != null) {
			Clickable clickable = new Clickable() {
				@Override
				public void onClick(UIElement element) {
					int recipeIndex = Integer.parseInt(element.name.replace("recipe", ""));
					setRecipe(recipeIndex);
				}
			};

			recipePicker = new RecipePicker(name, new Point(x + GamePanel.tileSize, y), possibleRecipes, activeRecipe, clickable);
			UI.currentModal = recipePicker;
		} else {
			UI.currentModal = null;
		}
	}
	
}
