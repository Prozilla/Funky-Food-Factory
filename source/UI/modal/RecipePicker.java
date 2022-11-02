package source.UI.modal;

import java.awt.Point;
import java.util.ArrayList;

import source.UI.Clickable;
import source.UI.Direction;
import source.UI.ImageElement;
import source.UI.Modal;
import source.UI.UI;
import source.UI.UIElement;
import source.buildable.building.recipe.Recipe;
import source.util.Vector4;

public class RecipePicker extends Modal {

	public Recipe activeRecipe;
	public ArrayList<Recipe> possibleRecipes;

	public RecipePicker(String title, Point position, ArrayList<Recipe> possibleRecipes, Recipe activeRecipe, Clickable clickable) {
		super(title, position, Direction.VERTICAL, clickable);

		this.activeRecipe = activeRecipe;
		this.possibleRecipes = possibleRecipes;

		for (int i = 0; i < possibleRecipes.size(); i++) {
			Recipe recipe = possibleRecipes.get(i);
			Boolean active = recipe == activeRecipe;

			if (active)
				activeOptionIndex = i;

			int topMargin = i == 0 ? 15 : 3;

			UIElement recipeContainer = new UIElement(position, new Vector4(15, 6), new Vector4(0, topMargin, 0, 0), UI.cornerRadius, null, null, null, 0, Direction.HORIZONTAL);
			recipeContainer.name = String.format("recipe%s", i);

			addOption(recipeContainer);

			ImageElement inputImage = new ImageElement(position, null, null, 0, null, null, null, 0, Direction.HORIZONTAL, recipe.inputItem.sprite, imageSize, imageSize);
			recipeContainer.appendChild(inputImage);

			ImageElement arrowImage = new ImageElement(position, null, new Vector4(12, 0), 0, null, null, null, 0, Direction.HORIZONTAL, UI.instance.iconTextures.get("arrow"), imageSize, imageSize);
			recipeContainer.appendChild(arrowImage);

			ImageElement outputImage = new ImageElement(position, null, null, 0, null, null, null, 0, Direction.HORIZONTAL, recipe.outputItem.sprite, imageSize, imageSize);
			recipeContainer.appendChild(outputImage);
		}
	}

	public void setRecipe(int index) {
		activeRecipe = possibleRecipes.get(index);
		setOption(index);
	}

}
