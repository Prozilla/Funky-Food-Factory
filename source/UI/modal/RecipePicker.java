package source.UI.modal;

import java.awt.Point;
import java.util.ArrayList;
import java.awt.Graphics2D;

import source.UI.Modal;
import source.buildable.building.recipe.Recipe;
import source.item.AbstractItem;

public class RecipePicker extends Modal {

	public Recipe activeRecipe;

	public RecipePicker(String title, Point position, Recipe recipe) {
		super(title, position);
		this.activeRecipe = recipe;
	}

	@Override
	public void draw(Graphics2D graphics2D) {
		super.draw(graphics2D);
	}

}
