package source.item;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;

import source.main.GamePanel;
import source.tile.TileManager;

public class Item {

	final static int pixelSize = 8;
	public final static int size = pixelSize * GamePanel.pixelScale;
	
	public int x = 0;
	public int y = 0;
	public Point offset = new Point(0, 0);
	public Point coordinate = new Point(0, 0);

	BufferedImage sprite;

	public Item(int x, int y, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.coordinate = TileManager.positionToCoordinate(new Point(x, y));
		this.sprite = sprite;
	}

	public void draw(Graphics2D graphics2D) {
		graphics2D.drawImage(sprite, x + offset.x - size / 2, y + offset.y - size / 2, size, size, null);
	}

}
