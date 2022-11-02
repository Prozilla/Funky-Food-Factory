package source.game.buildable.building.recipe;

import source.game.item.AbstractItem;

public class Recipe {
	
	public AbstractItem inputItem;
	public AbstractItem outputItem;

	public Recipe(AbstractItem inputItem, AbstractItem outputItem) {
		this.inputItem = inputItem;
		this.outputItem = outputItem;
	}

}