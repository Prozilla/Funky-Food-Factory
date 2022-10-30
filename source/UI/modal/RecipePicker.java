package source.UI.modal;

import java.awt.Point;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Graphics2D;

import source.UI.Direction;
import source.UI.ImageElement;
import source.UI.UI;
import source.UI.UIElement;
import source.buildable.building.recipe.Recipe;

public class RecipePicker extends UIElement {

	public Recipe activeRecipe;
	public Integer activeRecipeIndex;
	public ArrayList<Recipe> possibleRecipes;

	public RecipePicker(String title, Point position, ArrayList<Recipe> possibleRecipes, Recipe activeRecipe) {
		super(position, new Point(20, 20), null, UI.cornerRadius, Color.white, UI.backgroundColorA, title, 0.65f, Direction.VERTICAL);

		this.activeRecipe = activeRecipe;
		this.possibleRecipes = possibleRecipes;

		for (int i = 0; i < possibleRecipes.size(); i++) {
			Recipe recipe = possibleRecipes.get(i);
			Boolean active = recipe == activeRecipe;

			if (active)
				activeRecipeIndex = i;

			UIElement recipeContainer = new UIElement(position, new Point(5, 0), null, UI.cornerRadius, null, null, null, 0, Direction.HORIZONTAL);
			recipeContainer.name = String.format("recipe%s", i);
			appendChild(recipeContainer);

			ImageElement inputImage = new ImageElement(position, new Point(10, 10), null, 0, null, null, null, 0, Direction.HORIZONTAL, recipe.inputItem.sprite, 25, 25);
			recipeContainer.appendChild(inputImage);

			ImageElement arrowImage = new ImageElement(position, new Point(5, 10), null, 0, null, null, null, 0, Direction.HORIZONTAL, UI.instance.iconTextures.get("arrow"), 25, 25);
			recipeContainer.appendChild(arrowImage);

			ImageElement outputImage = new ImageElement(position, new Point(10, 10), null, 0, null, null, null, 0, Direction.HORIZONTAL, recipe.outputItem.sprite, 25, 25);
			recipeContainer.appendChild(outputImage);
		}
	}

	@Override
	public void draw(Graphics2D graphics2D) {
		// Set hover color
		for (int i = 0; i < children.size(); i++) {
			UIElement element = children.get(i);
			
			if (element.hovering || element.hoveringChild) {
				element.backgroundColor = UI.backgroundColorA;
			} else {
				element.backgroundColor = null;
			}
		}

		super.draw(graphics2D);
	}

}
