package source.UI;

import java.awt.Point;
import java.awt.Color;
import java.awt.Graphics2D;

import source.util.Vector4;

public class Modal extends UIElement {

	public Integer activeOptionIndex = 0;
	UIElement optionsContainer;

	public final int imageSize = 35;

	public Modal(String title, Point position, Direction direction, Clickable clickable) {
		super(position, new Vector4(12, 9, 0, 6), null, UI.cornerRadius, Color.white, UI.backgroundColorA, title, 0.65f, Direction.VERTICAL);

		optionsContainer = new UIElement(position, new Vector4(3, 0), new Vector4(0, 12, 0, 0), UI.cornerRadius, null, null, null, 0, direction);
		appendChild(optionsContainer);
	}

	public void addOption(UIElement option) {
		option.clickable = clickable;
		option.handCursor = true;

		optionsContainer.appendChild(option);
	}

	public void setOption(int index) {
		activeOptionIndex = index;
	}

	@Override
	public void draw(Graphics2D graphics2D) {
		// Update styling
		for (int i = 0; i < optionsContainer.children.size(); i++) {
			UIElement element = optionsContainer.children.get(i);

			boolean hovering = element.hovering || element.hoveringChild;
			boolean active = i == activeOptionIndex;
			
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
