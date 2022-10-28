package source.UI;

import java.awt.Point;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class ImageElement extends UIElement {
	
	public BufferedImage sprite;

	public ImageElement(Point position, Point padding, Point margin, int radius, Color color, Color backgroundColor, String text, float fontSize, BufferedImage sprite, int width, int height) {
		super(position, padding, margin, radius, color, backgroundColor, text, fontSize);

		this.sprite = sprite;
		this.width = width;
		this.height = height;
		this.autoSize = false;
	}

	public void draw(Graphics2D graphics2D) {
		super.draw(graphics2D);

		graphics2D.drawImage(sprite, position.x + offset.x + padding.x / 2, position.y + padding.y / 2, width + padding.x / 4, height + padding.y / 4, null);
	}

}
