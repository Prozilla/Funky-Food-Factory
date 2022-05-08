package source.tile;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;

import source.buildable.Buildable;
import source.buildable.Conveyor;
import source.main.GamePanel;
import source.main.Mouse;

public class TileManager {

	final String tilesPath = "../../textures/";

	GamePanel gamePanel;

	Map<String, Tile> tiles;
	public Map<Point, Buildable> coordinateToBuildable = new HashMap<Point, Buildable>();

	public TileManager(GamePanel gamePanel) {
		this.gamePanel = gamePanel;

		addTiles();
		placeBuildable(new Point(5, 4), 0);
		placeBuildable(new Point(10, 4), 0);
	}

	public void addTiles() {
		tiles = new HashMap<String, Tile>();

		addTile(new String[]{"conveyor", "curved_conveyor"});
		addTile(new String[]{"floor"});
	}

	public void addTile(String[] names) {
		try {
			BufferedImage[] sprites = new BufferedImage[names.length];

			for (int i = 0; i < names.length; i++) {
				sprites[i] = ImageIO.read(getClass().getResourceAsStream(String.format("%s%s.png", tilesPath, names[i])));
			}

			tiles.put(names[0], new Tile(names[0], sprites));
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	public static Point coordinateToPosition(Point coordinate) {
		return new Point(coordinate.x * GamePanel.tileSize, coordinate.y * GamePanel.tileSize);
	}

	public static Point positionToCoordinate(Point coordinate) {
		return new Point(coordinate.x / GamePanel.tileSize, coordinate.y / GamePanel.tileSize);
	}

	public static BufferedImage rotateImage(BufferedImage image, double degrees) {
		double rotationRequired = Math.toRadians(degrees);
		double locationX = image.getWidth() / 2;
		double locationY = image.getHeight() / 2;
		AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		return op.filter(image, null);
	}

	public void placeBuildable(Point coordinate, int type) {
		Point point = coordinateToPosition(coordinate);
		Buildable buildable;

		if (coordinateToBuildable.containsKey(coordinate))
			return;

		switch (type) {
			case 0:
				buildable = new Conveyor(point.x, point.y, tiles.get("conveyor"), gamePanel, this);
				break;
			default:
				buildable = new Conveyor(point.x, point.y, tiles.get("conveyor"), gamePanel, this);
				break;
		}

		coordinateToBuildable.put(coordinate, buildable);

		updateBuildableConnections(coordinate);
	}

	public void removeBuildable(Point coordinate) {
		coordinateToBuildable.remove(coordinate);

		updateBuildableConnections(coordinate);
	}

	public void updateBuildableConnections(Point coordinate) {
		Buildable buildable = coordinateToBuildable.get(coordinate);
		Buildable[] buildables = new Buildable[]{
			coordinateToBuildable.get(new Point(coordinate.x, coordinate.y - 1)), // 0
			coordinateToBuildable.get(new Point(coordinate.x + 1, coordinate.y)), // 1
			coordinateToBuildable.get(new Point(coordinate.x, coordinate.y + 1)), // 2
			coordinateToBuildable.get(new Point(coordinate.x - 1, coordinate.y)), // 3
		};

		// System.out.println("Updating building connections at " + coordinate);

		if (buildable != null) {
			for (int i = 0; i < buildables.length; i++) {
				if (buildables[i] != null) {
					int oppositeDirection = (i + 2) % 4;

					// Neighbouring buildings already has an input, it should force it to connect to this building
					if (buildables[i].input != -1 && buildables[i].output == -1) {
						buildables[i].setConnection(false, oppositeDirection);
						buildable.setConnection(true, i);
					}
				}
			}

			for (int i = 0; i < buildables.length; i++) {
				if (buildables[i] != null) {
					int oppositeDirection = (i + 2) % 4;

					if (buildable.input == -1) {
						buildables[i].setConnection(false, oppositeDirection);
						buildable.setConnection(true, i);
					} else if (buildable.input != i && buildables[i].input == -1) {
						buildables[i].setConnection(true, oppositeDirection);
						buildable.setConnection(false, i);
					}
				}
			}

			// System.out.println("final in: " + building.input);
			// System.out.println("final out: " + building.output);
		} else {
			for (int i = 0; i < buildables.length; i++) {
				if (buildables[i] != null) {
					int oppositeDirection = (i + 2) % 4;

					if (buildables[i].input == oppositeDirection) {
						buildables[i].setConnection(true, -1);
					} else if (buildables[i].output == oppositeDirection) {
						buildables[i].setConnection(false, -1);
					}
				}
			}
		}
	}

	public void draw(Graphics2D graphics2D) {
		if (tiles == null)
			return;

		for (int column = 0; column < gamePanel.horizontalTiles; column++) {
			int x = column * GamePanel.tileSize;
			for (int row = 0; row < gamePanel.verticalTiles; row++) {
				int y = row * GamePanel.tileSize;

				drawTile(graphics2D, tiles.get("floor").sprites[0], x, y);

				// Hover
				if (column == Mouse.mouseCoordinate.x && row == Mouse.mouseCoordinate.y) {
					graphics2D.setColor(new Color(1f, 1f, 1f, 0.25f));
					graphics2D.fillRect(x, y, GamePanel.tileSize, GamePanel.tileSize);
				}

				Buildable buildable = coordinateToBuildable.get(new Point(column, row));

				if (buildable != null)
					buildable.draw(graphics2D);
			}
		}
	}

	// TO DO: implement random rotation
	public void drawTile(Graphics2D graphics2D, BufferedImage sprite, int x, int y) {
		graphics2D.drawImage(sprite, x, y, GamePanel.tileSize, GamePanel.tileSize, null);
	}

}
