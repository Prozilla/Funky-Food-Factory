package source.tile;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;

import source.buildable.Buildable;
import source.buildable.Building;
import source.buildable.building.Exporter;
import source.buildable.building.Importer;
import source.buildable.connectable.Conveyor;
import source.item.Item;
import source.main.GamePanel;
import source.main.Mouse;
import source.main.UI;

public class TileManager {

	final String tilesPath = "textures/tiles/";
	final String itemsPath = "textures/items/";

	GamePanel gamePanel;

	public Tile currentTile;
	public Map<String, Tile> tiles;
	Map<String, BufferedImage> itemTextures;

	public Map<Point, Buildable> coordinateToBuildable = new HashMap<Point, Buildable>();
	public ArrayList<Item> items = new ArrayList<Item>();

	public TileManager(GamePanel gamePanel) {
		this.gamePanel = gamePanel;

		addTiles();
		addItems();
		placeBuildable(new Point(6, 3), "importer", 0);
		placeBuildable(new Point(9, 3), "exporter", 180);
	}

	public void addTiles() {
		tiles = new HashMap<String, Tile>();

		addTile(tiles, new String[]{"conveyor", "curved_conveyor"});
		addTile(tiles, new String[]{"floor"});
		addTile(tiles, new String[]{"importer"});
		addTile(tiles, new String[]{"exporter"});

		currentTile = tiles.get("conveyor");
	}

	public void addTile(Map<String, Tile> map, String[] names) {
		try {
			BufferedImage[] sprites = new BufferedImage[names.length];

			for (int i = 0; i < names.length; i++) {
				System.out.println(String.format("%s%s.png", tilesPath, names[i]));
				sprites[i] = ImageIO.read(getClass().getResourceAsStream(String.format("%s%s.png", tilesPath, names[i])));
			}

			map.put(names[0], new Tile(names[0], sprites));
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	public void addItems() {
		itemTextures = new HashMap<String, BufferedImage>();

		addItem("iron_ore");
	}

	public void addItem(String name) {
		try {
			itemTextures.put(name, ImageIO.read(getClass().getResourceAsStream(String.format("%s%s.png", itemsPath, name))));
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	public void spawnItem(String name, Point coordinate) {
		int xOffset = GamePanel.tileSize / 2 - Item.size / 2;
		int yOffset = GamePanel.tileSize / 2- Item.size / 2;

		Point center = new Point(coordinate.x * GamePanel.tileSize + GamePanel.tileSize + xOffset, coordinate.y * GamePanel.tileSize + yOffset);

		float range = GamePanel.tileSize - GamePanel.pixelScale * Conveyor.borderWidth;
		Point offset = new Point((int)Math.round(Math.random() * range - range / 2), (int)Math.round(Math.random() * range - range / 2));

		Item item = new Item(center.x, center.y, name, itemTextures.get(name));
		// item.offset = offset;

		items.add(item);
	}

	public static Point coordinateToPosition(Point coordinate) {
		return new Point(coordinate.x * GamePanel.tileSize, coordinate.y * GamePanel.tileSize);
	}

	public static Point positionToCoordinate(Point point) {
		return new Point((int)Math.floor(point.x / GamePanel.tileSize), (int)Math.floor(point.y / GamePanel.tileSize));
	}

	public static BufferedImage rotateImage(BufferedImage image, double degrees) {
		double rotationRequired = Math.toRadians(degrees);
		double locationX = image.getWidth() / 2;
		double locationY = image.getHeight() / 2;
		AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		return op.filter(image, null);
	}

	public static BufferedImage mirrorImageVertically(BufferedImage image) {
		AffineTransform transform = AffineTransform.getScaleInstance(-1, 1);
		transform.translate(-image.getWidth(null), 0);
		AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		return op.filter(image, null);
	}

	public static Point moveInDirection(int direction, int distance) {
		Point point = new Point(0, 0);

		switch (direction) {
			case 0:
				point = new Point(0, -distance);
				break;
			case 1:
				point = new Point(distance, 0);
				break;
			case 2:
				point = new Point(0, distance);
				break;
			case 3:
				point = new Point(-distance, 0);
				break;
		}

		return point;
	}

	public void placeBuildable(Point coordinate, String name, int rotation) {
		Point point = coordinateToPosition(coordinate);
		Buildable buildable;

		if (coordinateToBuildable.containsKey(coordinate))
			return;

		switch (name) {
			case "conveyor":
				buildable = new Conveyor(point.x, point.y, tiles.get("conveyor"), gamePanel, this);
				break;
			case "importer":
				buildable = new Importer(point.x, point.y, rotation, tiles.get("importer"), gamePanel, this);
				break;
			case "exporter":
				buildable = new Exporter(point.x, point.y, rotation, tiles.get("exporter"), gamePanel, this);
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

					if (buildable.input == -1 && buildables[i].output != -2) {
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

		ArrayList<Buildable> buildables = new ArrayList<Buildable>();

		// Draw floor and conveyors
		for (int column = 0; column < gamePanel.horizontalTiles; column++) {
			int x = column * GamePanel.tileSize;
			for (int row = 0; row < gamePanel.verticalTiles; row++) {
				int y = row * GamePanel.tileSize;

				drawTile(graphics2D, tiles.get("floor").sprites[0], x, y);

				// Hover
				if (UI.hoveringTile == null && column == Mouse.mouseCoordinate.x && row == Mouse.mouseCoordinate.y) {
					graphics2D.setColor(new Color(1f, 1f, 1f, 0.25f));
					graphics2D.fillRect(x, y, GamePanel.tileSize, GamePanel.tileSize);
				}

				Buildable buildable = coordinateToBuildable.get(new Point(column, row));

				if (buildable != null) {
					if (buildable instanceof Conveyor) {
						// Draw conveyor
						buildable.draw(graphics2D);
					} else {
						buildables.add(buildable);

						// Draw building conveyor
						if (buildable.conveyor != null)
							buildable.conveyor.draw(graphics2D);
					}
				}
			}
		}

		// Draw items
		drawItems(graphics2D);

		// Draw buildings
		for (int i = 0; i < buildables.size(); i++) {
			buildables.get(i).draw(graphics2D);
		}
	}

	// TO DO: implement random rotation
	public void drawTile(Graphics2D graphics2D, BufferedImage sprite, int x, int y) {
		graphics2D.drawImage(sprite, x, y, GamePanel.tileSize, GamePanel.tileSize, null);
	}

	public void drawItems(Graphics2D graphics2D) {
		for (int i = 0; i < items.size(); i++) {
			Item item = items.get(i);
			Point coordinate = item.coordinate;

			Buildable buildable = coordinateToBuildable.get(coordinate);
			Conveyor conveyor = (buildable != null && buildable instanceof Conveyor) ? (Conveyor)buildable : null;
			boolean buildingConveyor = false;

			if (conveyor == null && buildable != null && buildable.conveyor != null) {
				conveyor = buildable.conveyor;
				buildingConveyor = true;
			}

			if (conveyor != null) {
				int direction = !buildingConveyor ? (conveyor.input + 2) % 4 : (conveyor.rotation / 90 + 1) % 4;
				Point offset = moveInDirection(direction, GamePanel.tileSize / 2 + Item.size / 2);

				switch (direction) {
					case 0:
						offset.y += Item.size / 2;
						break;
					case 1:
						offset.x -= Item.size / 2;
						break;
					case 2:
						offset.y -= Item.size / 2;
						break;
					case 3:
						offset.x += Item.size / 2;
						break;
				}

				if (direction == 0) {
					offset.x += Item.size;
				} else if (direction == 1) {
					offset.y -= Item.size;
				}

				Point point = new Point(item.x + offset.x, item.y + offset.y);
				Point offsetCoordinate = positionToCoordinate(point);

				// Get previous conveyor coordinate
				Point previousCoordinateOffset = moveInDirection(direction, 1);
				Point previousCoordinate = new Point(coordinate.x + previousCoordinateOffset.x, coordinate.y + previousCoordinateOffset.y);

				if (buildingConveyor) {
					direction = (conveyor.rotation / 90 + 3) % 4;
				}

				if (!offsetCoordinate.equals(previousCoordinate)) {
					if (!buildingConveyor && conveyor.output != -1) {
						direction = conveyor.output;
					} else if (buildable instanceof Building) {
						((Building)buildable).processItem(item);
					}
					// graphics2D.setColor(Color.yellow);
				} else {
					// graphics2D.setColor(Color.cyan);
				}

				// graphics2D.fillRect(point.x, point.y, GamePanel.pixelScale, GamePanel.pixelScale);

				Point movement = moveInDirection(direction, (int)Math.round(gamePanel.deltaTime * conveyor.speed));

				item.x += movement.x;
				item.y += movement.y;
				item.coordinate = positionToCoordinate(new Point(item.x, item.y));
			}

			item.draw(graphics2D);
		}
	}

}
