package dev.prozilla.funkyfactory.UI;

import dev.prozilla.funkyfactory.main.Mouse;
import dev.prozilla.funkyfactory.main.Viewport;
import dev.prozilla.funkyfactory.util.Vector4;

import java.awt.*;
import java.util.ArrayList;

public class UIElement {
	
	public String name = null;
	
	public Point position;
	public Vector4 padding;
	public Vector4 margin;
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
	
	Point offsetPosition = new Point();
	
	public boolean hovering = false;
	public boolean hoveringChild = false;
	public boolean handCursor = false;
	
	public Clickable clickable;
	
	public UIElement parent;
	public ArrayList<UIElement> children = new ArrayList<UIElement>();
	
	public UIElement(Point position, Vector4 padding, Vector4 margin, int radius, Color color, Color backgroundColor, String text, float fontSize, Direction direction) {
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
			this.padding = new Vector4();
		
		if (this.margin == null)
			this.margin = new Vector4();
	}
	
	public void setBorder(int width, Color color) {
		borderWidth = width;
		borderColor = color;
	}
	
	public void appendChild(UIElement element) {
		children.add(element);
		element.parent = this;
		
		if (handCursor)
			element.handCursor = true;
		
		arrangeChildren();
	}
	
	// Horizontal
	void arrangeChildren() {
		totalSize.x = (width != null) ? width + padding.x : 0;
		totalSize.y = (height != null) ? height + padding.y : 0;
		
		if (children.size() > 0) {
			for (int i = 0; i < children.size(); i++) {
				UIElement element = children.get(i);
				element.arrangeChildren();
				
				int totalElementWidth = (element.totalSize != null) ? element.totalSize.x + element.padding.x + element.padding.z : 0;
				int totalElementheight = (element.totalSize != null) ? element.totalSize.y + element.padding.y + element.padding.w : 0;
				
				if (direction == Direction.VERTICAL) {
					element.offset = new Point(offset.x + padding.x + margin.x, offset.y + totalSize.y + margin.y);
					totalSize.y += totalElementheight + element.margin.y + element.margin.w;
					
					if (width == null || totalElementWidth > width)
						totalSize.x = totalElementWidth + padding.x + padding.z;
				} else {
					element.offset = new Point(offset.x + totalSize.x + margin.x, offset.y + padding.y + margin.y);
					totalSize.x += totalElementWidth + element.margin.x + element.margin.z;
					
					if (height == null || totalElementheight > height)
						totalSize.y = totalElementheight + padding.y + padding.w;
				}
			}
		}
		
		totalSize.y += padding.w;
		totalSize.x += padding.z;
	}
	
	void updateSize(Graphics2D graphics2D) {
		graphics2D.setFont(UI.font.deriveFont(Font.PLAIN, UI.fontSize * fontSize));
		
		width = (text != null) ? graphics2D.getFontMetrics().stringWidth(text) : 0;
		height = graphics2D.getFontMetrics().getHeight();
		
		arrangeChildren();
	}
	
	boolean isHovering() {
		Point viewportPosition = Viewport.instance.positionToViewport(position);
		viewportPosition.x += offset.x;
		viewportPosition.y += offset.y;
		
		int viewportWidth = totalSize.x;
		int viewportHeight = totalSize.y;
		
		return Mouse.mousePosition != null &&
				       (Mouse.mousePosition.x >= viewportPosition.x) &&
				       (Mouse.mousePosition.x < viewportPosition.x + viewportWidth) &&
				       (Mouse.mousePosition.y >= viewportPosition.y) &&
				       (Mouse.mousePosition.y < viewportPosition.y + viewportHeight);
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
	
	public static void drawBackground(Graphics2D graphics2D, Point position, Dimension size, Color color, int radius) {
		graphics2D.setColor(color);
		graphics2D.fillRoundRect(position.x, position.y, size.width, size.height, radius, radius);
	}
	
	public static void drawText(Graphics2D graphics2D, Point position, String content, Font font, int fontStyle, float fontSize, Color color, int radius) {
		graphics2D.setFont(font.deriveFont(fontStyle, fontSize));
		graphics2D.setColor(color);
		graphics2D.drawString(content, position.x, position.y);
	}
	
	public static void drawBorder(Graphics2D graphics2D, Point position, Dimension size, int thickness, Color color, int radius) {
		Stroke oldStroke = graphics2D.getStroke();
		graphics2D.setStroke(new BasicStroke(thickness));
		graphics2D.setColor(color);
		
		graphics2D.drawRoundRect(position.x, position.y, size.width, size.height, radius, radius);
		
		graphics2D.setStroke(oldStroke);
	}
	
	public void draw(Graphics2D graphics2D) {
		if (width == null || height == null || autoSize)
			updateSize(graphics2D);
		
		Point viewportPosition = Viewport.instance.positionToViewport(position);
		offsetPosition = new Point(viewportPosition.x + offset.x + margin.x, viewportPosition.y + offset.y + margin.y);
		
		// Draw background
		if (backgroundColor != null) {
			drawBackground(graphics2D, new Point(offsetPosition.x, offsetPosition.y), new Dimension(totalSize.x, totalSize.y), backgroundColor, radius);
		}
		
		// Draw text
		if (text != null && color != null) {
			drawText(graphics2D, new Point(offsetPosition.x + padding.x, offsetPosition.y + height + padding.y), text, UI.font, Font.PLAIN, UI.fontSize * fontSize, color, radius);
		}
		
		// Draw border
		if (borderWidth > 0 && borderColor != null) {
			drawBorder(graphics2D, new Point(offsetPosition.x, offsetPosition.y), new Dimension(totalSize.x, totalSize.y), borderWidth, borderColor, radius);
		}
		
		// Draw children
		for (int i = 0; i < children.size(); i++) {
			UIElement element = children.get(i);
			element.draw(graphics2D);
		}
	}
	
}
