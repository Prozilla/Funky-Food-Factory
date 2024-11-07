package dev.prozilla.funkyfactory.buildable;

import dev.prozilla.funkyfactory.UI.Clickable;
import dev.prozilla.funkyfactory.UI.UI;
import dev.prozilla.funkyfactory.UI.UIElement;
import dev.prozilla.funkyfactory.UI.modal.RecipePicker;
import dev.prozilla.funkyfactory.buildable.building.recipe.Recipe;
import dev.prozilla.funkyfactory.item.Item;
import dev.prozilla.funkyfactory.item.ItemManager;
import dev.prozilla.funkyfactory.main.GamePanel;
import dev.prozilla.funkyfactory.main.Viewport;
import dev.prozilla.funkyfactory.tile.Tile;
import dev.prozilla.funkyfactory.tile.TileManager;

import java.awt.*;
import java.util.ArrayList;

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
		
		if (recipePicker != null)
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
