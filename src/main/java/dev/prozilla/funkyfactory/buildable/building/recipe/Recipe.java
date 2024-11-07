package dev.prozilla.funkyfactory.buildable.building.recipe;

import dev.prozilla.funkyfactory.item.AbstractItem;

public class Recipe {
	
	public AbstractItem inputItem;
	public AbstractItem outputItem;
	
	public Recipe(AbstractItem inputItem, AbstractItem outputItem) {
		this.inputItem = inputItem;
		this.outputItem = outputItem;
	}
	
}