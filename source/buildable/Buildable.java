package source.buildable;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.awt.image.BufferedImage;

import source.buildable.connectable.Conveyor;
import source.main.GamePanel;
import source.tile.Tile;
import source.tile.TileManager;

public class Buildable {

	// Debugging
	boolean showConnections = false;

	int x = 0;
	int y = 0;
	public int rotation = 0;
	public Point coordinate = new Point(0, 0);

	public Conveyor conveyor;

	float animationSpeed = 10f;
	public int frameCount = 1;
	int frame = 0;
	float frameTime = 0;
	public BufferedImage currentSprite;
	public boolean mirrorSprite = false;

	// -1: unset, -2: NaN
	public int input = -1;
	public int output = -1;

	public ArrayList<Integer> connections = new ArrayList<Integer>();

	public Tile tile;
	public GamePanel gamePanel;
	public TileManager tileManager;

	public Buildable(int x, int y, Tile tile, GamePanel gamePanel, TileManager tileManager) {
		this.x = x;
		this.y = y;
		this.coordinate = TileManager.positionToCoordinate(new Point(x, y));
		this.tile = tile;
		this.currentSprite = tile.sprites[0];
		this.gamePanel = gamePanel;
		this.tileManager = tileManager;
	}

	public void setConnection(boolean isInput, int direction) {
		if (isInput && input != -2) {
			input = direction;
		} else if (!isInput && output != -2) {
			output = direction;
		}

		connections = new ArrayList<Integer>();
		connections.add(input);
		connections.add(output);
	}

	public void addConveyor(int rotation) {
		conveyor = new Conveyor(x, y, tileManager.tiles.get("conveyor"), gamePanel, tileManager);
		conveyor.rotation = rotation;
	}

	public void draw(Graphics2D graphics2D) {
		frame = ((int)Math.round(gamePanel.time * animationSpeed)) % frameCount;

		// Crop to current frame
		BufferedImage sprite = currentSprite.getSubimage(0, frame * GamePanel.originalTileSize, GamePanel.originalTileSize, GamePanel.originalTileSize);

		// Mirror image
		if (mirrorSprite) {
			sprite = TileManager.mirrorImageVertically(sprite);
		}

		// Apply rotation
		if (rotation != 0) {
			sprite = TileManager.rotateImage(sprite, rotation);
		}

		graphics2D.drawImage(sprite, x, y, GamePanel.tileSize, GamePanel.tileSize, null);

		if (showConnections) {
			if (input != -1 && input != -2) {
				drawDirection(true, input, graphics2D);
			}

			if (output != -1 && output != -2) {
				drawDirection(false, output, graphics2D);
			}
		}
	}

	public void drawDirection(boolean in, int direction, Graphics2D graphics2D) {
		Point point = new Point(0, 0);
		int offset = GamePanel.tileSize / 3;
		int size = in ? GamePanel.tileSize / 4 : GamePanel.tileSize / 5;
		Point center = new Point(x + GamePanel.tileSize / 2 - size / 2, y + GamePanel.tileSize / 2 - size / 2);

		graphics2D.setColor(Color.black);
		graphics2D.fillOval(center.x, center.y, size, size);

		switch (direction) {
			case 0:
				point = new Point(0, -offset);
				break;
			case 1:
				point = new Point(offset, 0);
				break;
			case 2:
				point = new Point(0, offset);
				break;
			case 3:
				point = new Point(-offset, 0);
				break;
		}

		graphics2D.setColor(in ? Color.red : Color.green);
		graphics2D.fillOval(center.x + point.x, center.y + point.y, size, size);
	}

}
