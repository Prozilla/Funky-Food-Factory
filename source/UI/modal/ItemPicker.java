package source.UI.modal;

import java.awt.Point;
import java.util.ArrayList;

import source.UI.Clickable;
import source.UI.Direction;
import source.UI.ImageElement;
import source.UI.Modal;
import source.UI.UI;
import source.item.AbstractItem;
import source.item.ItemManager;
import source.util.Vector4;

public class ItemPicker extends Modal {

	public AbstractItem activeItem;
	public ArrayList<AbstractItem> possibleItems;

	public ItemPicker(String title, Point position, ArrayList<AbstractItem> possibleItems, AbstractItem activeItem, Clickable clickable) {
		super(title, position, Direction.HORIZONTAL, clickable);

		this.activeItem = activeItem;
		this.possibleItems = possibleItems;

		for (int i = 0; i < possibleItems.size(); i++) {
			AbstractItem item = possibleItems.get(i);
			Boolean active = item == activeItem;

			if (active)
				activeOptionIndex = i;

			ImageElement itemImage = new ImageElement(position, new Vector4(6, 6), null, UI.cornerRadius, null, null, null, 0, Direction.HORIZONTAL, item.sprite, imageSize, imageSize);
			itemImage.name = item.name;

			addOption(itemImage);
		}
	}

	public void setItem(String name) {
		setOption(possibleItems.indexOf(ItemManager.abstractItems.get(name)));
		activeItem = possibleItems.get(activeOptionIndex);
	}

}
