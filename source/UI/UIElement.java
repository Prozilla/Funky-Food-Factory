package source.UI;

import java.awt.Point;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Font;

public class UIElement {

	public Point position;
	public Point padding;
	public Point margin;
	public int radius;
	public Color color;
	public Color backgroundColor;
	public String text;
	public float fontSize;

	public Integer width;
	public Integer height;
	public Point offset = new Point();
	public Point totalSize = new Point();
	public boolean autoSize = true;

	public ArrayList<UIElement> children = new ArrayList<UIElement>();

	public UIElement(Point position, Point padding, Point margin, int radius, Color color, Color backgroundColor, String text, float fontSize) {
		this.position = position;
		this.padding = padding;
		this.margin = margin;
		this.radius = radius;
		this.color = color;
		this.backgroundColor = backgroundColor;
		this.text = text;
		this.fontSize = fontSize;

		if (this.padding == null)
			this.padding = new Point();

		if (this.margin == null)
			this.margin = new Point();
	}

	public void appendChild(UIElement element) {
		children.add(element);
		arrangeChildren();
	}

	// Horizontal
	void arrangeChildren() {
		totalSize.x = (width != null) ? width + padding.x : 0;
		totalSize.y = (height != null) ? height + padding.y : 0;

		for (int i = 0; i < children.size(); i++) {
			UIElement element = children.get(i);
			element.offset = new Point(totalSize.x, 0);

			int totalElementWidth = element.width + element.padding.x;
			int totalElementheight = element.height + element.padding.y;

			totalSize.x += totalElementWidth;

			if (totalElementheight > totalSize.y)
				totalSize.y = totalElementheight;
		}
	}

	void updateSize(Graphics2D graphics2D) {
		graphics2D.setFont(UI.font.deriveFont(Font.PLAIN, UI.fontSize * fontSize));

		width = (text != null) ? graphics2D.getFontMetrics().stringWidth(text) : 0;
		height = graphics2D.getFontMetrics().getHeight();

		arrangeChildren();
	}

	public void draw(Graphics2D graphics2D) {
		if (width == null || height == null || autoSize)
			updateSize(graphics2D);

		// Draw background
		if (backgroundColor != null) {
			graphics2D.setColor(backgroundColor);
			graphics2D.fillRoundRect(position.x + offset.x, position.y, totalSize.x, totalSize.y, radius, radius);
		}

		// Draw text
		if (text != null && color != null) {
			graphics2D.setFont(UI.font.deriveFont(Font.PLAIN, UI.fontSize * fontSize));
			graphics2D.setColor(color);
			graphics2D.drawString(text, position.x + offset.x + padding.x / 2, position.y + height + padding.y / 2);
		}

		// Draw children
		for (int i = 0; i < children.size(); i++) {
			UIElement element = children.get(i);
			element.draw(graphics2D);
		}
	}

}
