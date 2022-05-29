package source.UI;

import java.awt.Point;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.Color;

public class Modal {
	
	public String title;

	public Point position;

	public Modal(String title, Point position) {
		this.title = title;
		this.position = position;
	}

	public void draw(Graphics2D graphics2D) {
		graphics2D.setFont(UI.font.deriveFont(Font.PLAIN, UI.fontSize / 3 * 2));
		int width = graphics2D.getFontMetrics().stringWidth(title) + 20;
		int height = graphics2D.getFontMetrics().getHeight() + 20;

		graphics2D.setColor(UI.backgroundColorB);
		graphics2D.fillRoundRect(position.x, position.y, width, height, UI.cornerRadius, UI.cornerRadius);

		graphics2D.setColor(Color.white);
		graphics2D.drawString(title, position.x + 10, position.y + 10);
	}

}
