package source.tile;

import java.awt.Graphics2D;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import source.UI.UI;
import source.buildable.Buildable;
import source.buildable.Connectable;
import source.buildable.building.Exporter;
import source.buildable.building.Importer;
import source.buildable.building.Smelter;
import source.buildable.connectable.Conveyor;
import source.item.ItemManager;
import source.main.GamePanel;
import source.main.Mouse;
import source.main.Viewport;

public class TileManager {

	final String tilesPath = "textures/";

	GamePanel gamePanel;
	public ItemManager itemManager;
	Viewport viewport;

	public Tile currentTile;
	public Map<String, Tile> tiles;

	public Map<Point, Buildable> coordinateToBuildable = new HashMap<Point, Buildable>();

	public TileManager(GamePanel gamePanel, Viewport viewport) {
		this.gamePanel = gamePanel;
		this.viewport = viewport;

		addTiles();
	}

	public void loadFactory() {
		placeBuildable(new Point(3, 5), "importer", 1);
		placeBuildable(new Point(4, 5), "conveyor", 0);
		placeBuildable(new Point(5, 5), "constructor", 0);
		placeBuildable(new Point(6, 5), "conveyor", 0);
		placeBuildable(new Point(7, 5), "smelter", 0);
		placeBuildable(new Point(8, 5), "conveyor", 0);
		placeBuildable(new Point(9, 5), "exporter", 1);
	}

	public void addTiles() {
		tiles = new HashMap<String, Tile>();

		addTile(tiles, new String[]{"conveyor", "curved_conveyor"});
		addTile(tiles, new String[]{"floor"});
		addTile(tiles, new String[]{"importer"});
		addTile(tiles, new String[]{"exporter"});
		addTile(tiles, new String[]{"smelter"});
		addTile(tiles, new String[]{"constructor"});

		currentTile = tiles.get("conveyor");
	}

	public void addTile(Map<String, Tile> map, String[] names) {
		try {
			BufferedImage[] sprites = new BufferedImage[names.length];

			for (int i = 0; i < names.length; i++) {
				String path = String.format("%s%s.png", tilesPath, names[i]);
				System.out.println("Reading: " + path);
				sprites[i] = ImageIO.read(getClass().getResourceAsStream(path));
			}

			map.put(names[0], new Tile(names[0], sprites));
		} catch (IOException exception) {
			exception.printStackTrace();
		}
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

	public Buildable createBuildable(Point coordinate, String name, int direction) {
		Buildable buildable;
		Point point = coordinateToPosition(coordinate);

		switch (name) {
			case "conveyor":
				buildable = new Conveyor(point.x, point.y, tiles.get("conveyor"), gamePanel, this, viewport);
				break;
			case "importer":
				buildable = new Importer(point.x, point.y, direction, tiles.get("importer"), gamePanel, this, itemManager, viewport);
				break;
			case "exporter":
				buildable = new Exporter(point.x, point.y, direction, tiles.get("exporter"), gamePanel, this, itemManager, viewport);
				break;
			case "smelter":
				buildable = new Smelter(point.x, point.y, tiles.get("smelter"), gamePanel, this, itemManager, viewport);
				break;
			case "constructor":
				buildable = new source.buildable.building.Constructor(point.x, point.y, tiles.get("constructor"), gamePanel, this, itemManager, viewport);
				break;
			default:
				buildable = new Conveyor(point.x, point.y, tiles.get("conveyor"), gamePanel, this, viewport);
				break;
		}

		return buildable;
	}

	public void placeBuildable(Point coordinate, String name, int direction) {
		if (coordinateToBuildable.containsKey(coordinate))
			return;

		Buildable buildable = createBuildable(coordinate, name, direction);

		coordinateToBuildable.put(coordinate, buildable);
		updateCoordinate(coordinate);
	}

	public void removeBuildable(Point coordinate) {
		coordinateToBuildable.remove(coordinate);
		updateCoordinate(coordinate);
	}

	void updateCoordinate(Point coordinate) {
		Buildable buildable = coordinateToBuildable.get(coordinate);
		Buildable[] buildables = new Buildable[]{
			coordinateToBuildable.get(new Point(coordinate.x, coordinate.y - 1)), // 0
			coordinateToBuildable.get(new Point(coordinate.x + 1, coordinate.y)), // 1
			coordinateToBuildable.get(new Point(coordinate.x, coordinate.y + 1)), // 2
			coordinateToBuildable.get(new Point(coordinate.x - 1, coordinate.y)), // 3
		};

		Connectable.updateBuildableConnections(buildable, buildables, true);
	}

	void updateBuilding(Buildable buildable) {
		Point coordinate = buildable.coordinate;
		Buildable[] buildables = new Buildable[]{
			coordinateToBuildable.get(new Point(coordinate.x, coordinate.y - 1)), // 0
			coordinateToBuildable.get(new Point(coordinate.x + 1, coordinate.y)), // 1
			coordinateToBuildable.get(new Point(coordinate.x, coordinate.y + 1)), // 2
			coordinateToBuildable.get(new Point(coordinate.x - 1, coordinate.y)), // 3
		};

		Connectable.updateBuildableConnections(buildable, buildables, false);
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
				Point coordinate = new Point(column, row);

				drawTile(graphics2D, tiles.get("floor").sprites[0], x, y);

				// Hover
				if (UI.hoveringInventoryTile == null && UI.hoveringElement == null && Mouse.mouseCoordinate != null && column == Mouse.viewportMouseCoordinate.x && row == Mouse.viewportMouseCoordinate.y) {
					// viewport.drawRect(graphics2D, x, y, GamePanel.tileSize, GamePanel.tileSize, new Color(1f, 1f, 1f, 0.25f));

					// Draw ghost building
					if (!coordinateToBuildable.containsKey(coordinate) && !Mouse.holdingRightMouseButton) {
						Buildable ghostBuildable = createBuildable(coordinate, currentTile.name, 1);
						updateBuilding(ghostBuildable);

						drawGhostBuildable(graphics2D, ghostBuildable, x, y);
					}
				}

				Buildable buildable = coordinateToBuildable.get(coordinate);

				if (buildable != null) {
					if (buildable instanceof Conveyor) {
						// Draw conveyor
						buildable.draw(graphics2D, false);
					} else {
						buildables.add(buildable);

						// Draw building conveyor
						if (buildable.buildingConveyor != null)
							buildable.buildingConveyor.draw(graphics2D, false);
					}
				}
			}
		}

		// Draw items
		itemManager.drawItems(graphics2D);

		// Draw buildings
		for (int i = 0; i < buildables.size(); i++) {
			buildables.get(i).draw(graphics2D, false);
		}
	}

	public void drawGhostBuildable(Graphics2D graphics2D, Buildable ghostBuildable, int x, int y) {
		ghostBuildable.draw(graphics2D, true);
	}

	// TO DO: implement random rotation
	public void drawTile(Graphics2D graphics2D, BufferedImage sprite, int x, int y) {
		viewport.drawSprite(graphics2D, sprite, x, y, GamePanel.tileSize, GamePanel.tileSize, 1, false);
	}

}
