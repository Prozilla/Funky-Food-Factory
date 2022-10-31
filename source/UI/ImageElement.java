package source.UI;

import java.awt.Point;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import source.main.Mouse;
import source.main.Viewport;

public class ImageElement extends UIElement {
	
	public BufferedImage sprite;

	public ImageElement(Point position, Point padding, Point margin, int radius, Color color, Color backgroundColor, String text, float fontSize, Direction direction, BufferedImage sprite, int width, int height) {
		super(position, padding, margin, radius, color, backgroundColor, text, fontSize, direction);

		this.sprite = sprite;
		this.width = width;
		this.height = height;
		this.autoSize = false;
	}

	@Override
	boolean isHovering() {
		return Mouse.mousePosition != null && 
			(Mouse.mousePosition.x >= position.x + offset.x + padding.x) && 
			(Mouse.mousePosition.x < position.x + offset.x + padding.x + width + padding.x / 4) && 
			(Mouse.mousePosition.y >= position.y + offset.y + padding.y) && 
			(Mouse.mousePosition.y < position.y + offset.y + padding.y + height + padding.y / 4);
	}

	public void draw(Graphics2D graphics2D) {
		super.draw(graphics2D);

		Point viewportPosition = Viewport.instance.positionToViewport(position);

		graphics2D.drawImage(sprite, viewportPosition.x + offset.x + padding.x / 2, viewportPosition.y + offset.y + padding.y / 2, width + padding.x / 2, height + padding.y / 2, null);
	}

}
