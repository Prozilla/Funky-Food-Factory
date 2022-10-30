package source.UI;

import java.awt.Point;
import java.util.ArrayList;

import source.main.Mouse;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.BasicStroke;
import java.awt.Stroke;

public class UIElement {

	public String name = null;

	public Point position;
	public Point padding;
	public Point margin;
	public int radius;
	public Color color;
	public Color backgroundColor;
	public String text;
	public float fontSize;
	public Direction direction;

	public int borderWidth = 0;
	public Color borderColor = null;

	public Integer width;
	public Integer height;
	public Point offset = new Point();
	public Point totalSize = new Point();
	public boolean autoSize = true;

	public boolean hovering = false;
	public boolean hoveringChild = false;

	public Clickable clickable;

	public UIElement parent;
	public ArrayList<UIElement> children = new ArrayList<UIElement>();

	public UIElement(Point position, Point padding, Point margin, int radius, Color color, Color backgroundColor, String text, float fontSize, Direction direction) {
		this.position = position;
		this.padding = padding;
		this.margin = margin;
		this.radius = radius;
		this.color = color;
		this.backgroundColor = backgroundColor;
		this.text = text;
		this.fontSize = fontSize;
		this.direction = direction;

		if (this.padding == null)
			this.padding = new Point();

		if (this.margin == null)
			this.margin = new Point();
	}

	public void setBorder(int width, Color color) {
		borderWidth = width;
		borderColor = color;
	}

	public void appendChild(UIElement element) {
		children.add(element);
		element.parent = this;

		arrangeChildren();
	}

	// Horizontal
	void arrangeChildren() {
		totalSize.x = (width != null) ? width + padding.x / 2 : 0;
		totalSize.y = (height != null) ? height + padding.y / 2 : 0;

		if (children.size() > 0) {
			for (int i = 0; i < children.size(); i++) {
				UIElement element = children.get(i);
				element.arrangeChildren();

				int totalElementWidth = (element.totalSize != null) ? element.totalSize.x + element.padding.x : 0;
				int totalElementheight = (element.totalSize != null) ? element.totalSize.y + element.padding.y : 0;

				if (direction == Direction.VERTICAL) {
					element.offset = new Point(offset.x + padding.x / 2, offset.y + totalSize.y);
					totalSize.y += totalElementheight;

					if (width == null || totalElementWidth > width)
						totalSize.x = totalElementWidth + padding.x;
				} else {
					element.offset = new Point(offset.x + totalSize.x, offset.y + padding.y / 2);
					totalSize.x += totalElementWidth;

					if (height == null || totalElementheight > height)
						totalSize.y = totalElementheight + padding.y;
				}
			}
		}

		if (direction == Direction.VERTICAL) {
			totalSize.y += padding.y / 2;
		} else {
			totalSize.x += padding.x / 2;
		}
	}

	void updateSize(Graphics2D graphics2D) {
		graphics2D.setFont(UI.font.deriveFont(Font.PLAIN, UI.fontSize * fontSize));

		width = (text != null) ? graphics2D.getFontMetrics().stringWidth(text) : 0;
		height = graphics2D.getFontMetrics().getHeight();

		arrangeChildren();
	}

	boolean isHovering() {
		return Mouse.mousePosition != null && 
			(Mouse.mousePosition.x >= position.x + offset.x) && 
			(Mouse.mousePosition.x < position.x + offset.x + totalSize.x) && 
			(Mouse.mousePosition.y >= position.y + offset.y) && 
			(Mouse.mousePosition.y < position.y + offset.y + totalSize.y);
	}

	boolean checkHoverState() {
		hoveringChild = false;

		if (children.size() > 0) {
			for (int i = 0; i < children.size(); i++) {
				UIElement element = children.get(i);

				if (element.checkHoverState())
					hoveringChild = true;
			}
		}

		if (!hoveringChild && isHovering()) {
			UI.hoveringElement = this;
		} else if (hovering) {
			// Stopped hovering
			UI.hoveringElement = null;
			hovering = false;
		}

		return (UI.hoveringElement == this || hoveringChild);
	}

	public void draw(Graphics2D graphics2D) {
		if (width == null || height == null || autoSize)
			updateSize(graphics2D);

		// Draw background
		if (backgroundColor != null) {
			graphics2D.setColor(backgroundColor);
			graphics2D.fillRoundRect(position.x + offset.x, position.y + offset.y, totalSize.x, totalSize.y, radius, radius);
		}

		// Draw text
		if (text != null && color != null) {
			graphics2D.setFont(UI.font.deriveFont(Font.PLAIN, UI.fontSize * fontSize));
			graphics2D.setColor(color);
			graphics2D.drawString(text, position.x + offset.x + padding.x / 2, position.y + offset.y + height + padding.y / 2);
		}

		// Draw border
		if (borderWidth > 0 && borderColor != null) {
			graphics2D.setColor(borderColor);
			Stroke oldStroke = graphics2D.getStroke();
			graphics2D.setStroke(new BasicStroke(borderWidth));

			graphics2D.drawRoundRect(position.x + offset.x, position.y + offset.y, totalSize.x, totalSize.y, radius, radius);

			graphics2D.setStroke(oldStroke);
		}

		// Draw children
		for (int i = 0; i < children.size(); i++) {
			UIElement element = children.get(i);
			element.draw(graphics2D);
		}
	}

}
