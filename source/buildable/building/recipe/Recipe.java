package source.buildable.building.recipe;

import source.item.AbstractItem;

public class Recipe {
	
	public AbstractItem inputItem;
	public AbstractItem outputItem;
	
	public Recipe(AbstractItem inputItem, AbstractItem outputItem) {
		this.inputItem = inputItem;
		this.outputItem = outputItem;
	}
	
}