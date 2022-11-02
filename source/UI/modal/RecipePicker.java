package source.UI.modal;

import java.awt.Point;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Graphics2D;

import source.UI.Clickable;
import source.UI.Direction;
import source.UI.ImageElement;
import source.UI.UI;
import source.UI.UIElement;
import source.buildable.building.recipe.Recipe;
import source.util.Vector4;

public class RecipePicker extends UIElement {

	public Recipe activeRecipe;
	public Integer activeRecipeIndex;
	public ArrayList<Recipe> possibleRecipes;

	public RecipePicker(String title, Point position, ArrayList<Recipe> possibleRecipes, Recipe activeRecipe, Clickable clickable) {
		super(position, new Vector4(12, 9, 0, 12), null, UI.cornerRadius, Color.white, UI.backgroundColorA, title, 0.65f, Direction.VERTICAL);

		this.activeRecipe = activeRecipe;
		this.possibleRecipes = possibleRecipes;

		for (int i = 0; i < possibleRecipes.size(); i++) {
			Recipe recipe = possibleRecipes.get(i);
			Boolean active = recipe == activeRecipe;

			if (active)
				activeRecipeIndex = i;

			int topMargin = i == 0 ? 15 : 3;

			UIElement recipeContainer = new UIElement(position, new Vector4(15, 6), new Vector4(0, topMargin, 0, 0), UI.cornerRadius, null, null, null, 0, Direction.HORIZONTAL);
			recipeContainer.name = String.format("recipe%s", i);
			recipeContainer.clickable = clickable;
			appendChild(recipeContainer);

			System.out.println(recipeContainer.margin.y);

			ImageElement inputImage = new ImageElement(position, null, null, 0, null, null, null, 0, Direction.HORIZONTAL, recipe.inputItem.sprite, 25, 25);
			recipeContainer.appendChild(inputImage);

			ImageElement arrowImage = new ImageElement(position, null, new Vector4(12, 0), 0, null, null, null, 0, Direction.HORIZONTAL, UI.instance.iconTextures.get("arrow"), 25, 25);
			recipeContainer.appendChild(arrowImage);

			ImageElement outputImage = new ImageElement(position, null, null, 0, null, null, null, 0, Direction.HORIZONTAL, recipe.outputItem.sprite, 25, 25);
			recipeContainer.appendChild(outputImage);
		}
	}

	public void setRecipe(int index) {
		activeRecipe = possibleRecipes.get(index);
		activeRecipeIndex = index;
	}

	@Override
	public void draw(Graphics2D graphics2D) {
		// Update styling
		for (int i = 0; i < children.size(); i++) {
			UIElement element = children.get(i);

			boolean hovering = element.hovering || element.hoveringChild;
			boolean active = i == activeRecipeIndex;
			
			if (hovering || active) {
				element.backgroundColor = UI.backgroundColorB;
			} else {
				element.backgroundColor = null;
			}

			if (active) {
				element.setBorder(UI.borderWidth, Color.WHITE);
			} else {
				element.setBorder(0, null);
			}
		}

		super.draw(graphics2D);
	}

}
