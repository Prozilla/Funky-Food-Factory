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
import source.item.AbstractItem;
import source.item.ItemManager;
import source.util.Vector4;

public class ItemPicker extends UIElement {

	public AbstractItem activeItem;
	public Integer activeItemIndex;
	public ArrayList<AbstractItem> possibleItems;

	public ItemPicker(String title, Point position, ArrayList<AbstractItem> possibleItems, AbstractItem activeItem, Clickable clickable) {
		super(position, new Vector4(12, 9, 12, 6), null, UI.cornerRadius, Color.white, UI.backgroundColorA, title, 0.65f, Direction.VERTICAL);

		this.activeItem = activeItem;
		this.possibleItems = possibleItems;

		UIElement itemsContainer = new UIElement(position, new Vector4(3, 0), new Vector4(0, 12, 0, 0), UI.cornerRadius, null, null, null, 0, Direction.HORIZONTAL);
		appendChild(itemsContainer);

		for (int i = 0; i < possibleItems.size(); i++) {
			AbstractItem item = possibleItems.get(i);
			Boolean active = item == activeItem;

			if (active)
				activeItemIndex = i;

			ImageElement itemImage = new ImageElement(position, new Vector4(6, 6), null, UI.cornerRadius, null, null, null, 0, Direction.HORIZONTAL, item.sprite, 25, 25);
			itemImage.name = item.name;

			itemImage.clickable = clickable;

			itemsContainer.appendChild(itemImage);
		}
	}

	public void setItem(String name) {
		activeItemIndex = possibleItems.indexOf(ItemManager.abstractItems.get(name));
		activeItem = possibleItems.get(activeItemIndex);
	}

	@Override
	public void draw(Graphics2D graphics2D) {
		// Update styling
		UIElement container = children.get(0);
		for (int i = 0; i < container.children.size(); i++) {
			UIElement element = container.children.get(i);

			boolean hovering = element.hovering || element.hoveringChild;
			boolean active = i == activeItemIndex;
			
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
