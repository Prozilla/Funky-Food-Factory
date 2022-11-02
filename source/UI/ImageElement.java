package source.UI;

import java.awt.Point;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import source.main.Mouse;
import source.main.Viewport;
import source.util.Vector4;

public class ImageElement extends UIElement {
	
	public BufferedImage sprite;

	public ImageElement(Point position, Vector4 padding, Vector4 margin, int radius, Color color, Color backgroundColor, String text, float fontSize, Direction direction, BufferedImage sprite, int width, int height) {
		super(position, padding, margin, radius, color, backgroundColor, text, fontSize, direction);

		this.sprite = sprite;
		this.width = width;
		this.height = height;
		this.autoSize = false;
	}

	@Override
	boolean isHovering() {
		Point viewportPosition = Viewport.instance.positionToViewport(position);
		viewportPosition.x += offset.x;
		viewportPosition.y += offset.y;
		
		int viewportWidth = (int)((width + padding.x + padding.z));
		int viewportHeight = (int)((height + padding.y + padding.w));

		return Mouse.mousePosition != null && 
			(Mouse.mousePosition.x >= viewportPosition.x) && 
			(Mouse.mousePosition.x < viewportPosition.x + viewportWidth) && 
			(Mouse.mousePosition.y >= viewportPosition.y) && 
			(Mouse.mousePosition.y < viewportPosition.y + viewportHeight);
	}

	public void draw(Graphics2D graphics2D) {
		super.draw(graphics2D);

		graphics2D.drawImage(sprite, offsetPosition.x + padding.x, offsetPosition.y + padding.y, width, height, null);
	}

}
