package source.UI.modal;

import java.awt.Point;
import java.awt.Color;
import java.awt.Graphics2D;

import source.UI.ImageElement;
import source.UI.UI;
import source.UI.UIElement;
import source.buildable.building.recipe.Recipe;

public class RecipePicker extends UIElement {

	public Recipe activeRecipe;

	public RecipePicker(String title, Point position, Recipe recipe) {
		super(position, new Point(20, 20), null, UI.cornerRadius, Color.white, UI.backgroundColorA, title, 0.75f);

		ImageElement inputImage = new ImageElement(position, new Point(20, 20), null, 0, null, null, title, 0, recipe.inputItem.sprite, 40, 40);
		appendChild(inputImage);

		ImageElement arrowImage = new ImageElement(position, new Point(20, 20), null, 0, null, null, title, 0, UI.instance.iconTextures.get("arrow"), 40, 40);
		appendChild(arrowImage);

		ImageElement outputImage = new ImageElement(position, new Point(20, 20), null, 0, null, null, title, 0, recipe.outputItem.sprite, 40, 40);
		appendChild(outputImage);

		this.activeRecipe = recipe;
	}

	@Override
	public void draw(Graphics2D graphics2D) {
		super.draw(graphics2D);
	}

}
