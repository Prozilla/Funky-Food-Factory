package dev.prozilla.funkyfactory.item;

import dev.prozilla.funkyfactory.UI.UI;
import dev.prozilla.funkyfactory.buildable.Buildable;
import dev.prozilla.funkyfactory.buildable.Building;
import dev.prozilla.funkyfactory.buildable.connectable.Conveyor;
import dev.prozilla.funkyfactory.main.GamePanel;
import dev.prozilla.funkyfactory.main.Viewport;
import dev.prozilla.funkyfactory.tile.TileManager;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Item extends AbstractItem {
	
	// Item names
	public static final String COPPER_ORE = "copper_ore";
	public static final String COPPER_INGOT = "copper_ingot";
	public static final String COPPER_ROD = "copper_rod";
	public static final String IRON_ORE = "iron_ore";
	public static final String IRON_INGOT = "iron_ingot";
	public static final String IRON_SCREW = "iron_screw";
	
	public final static int pixelSize = 16;
	public static int size = (int) (pixelSize * GamePanel.itemScaleMultiplier);
	
	public int x = 0;
	public int y = 0;
	public Point offset = new Point(0, 0);
	public Point coordinate = new Point(0, 0);
	
	public boolean clogged = false;
	final int clogGap = 0;
	
	public Building lastBuilding;
	
	GamePanel gamePanel;
	TileManager tileManager;
	Viewport viewport;
	ItemManager itemManager;
	
	public Item(int x, int y, String name, BufferedImage sprite, GamePanel gamePanel, TileManager tileManager, Viewport viewport, ItemManager itemManager) {
		super(name, sprite);
		this.x = x;
		this.y = y;
		this.name = name;
		this.coordinate = TileManager.positionToCoordinate(new Point(x, y));
		this.gamePanel = gamePanel;
		this.tileManager = tileManager;
		this.viewport = viewport;
		this.itemManager = itemManager;
	}
	
	public Item(int x, int y, AbstractItem abstractItem, GamePanel gamePanel, TileManager tileManager, Viewport viewport, ItemManager itemManager) {
		super(abstractItem.name, abstractItem.sprite);
		this.x = x;
		this.y = y;
		this.coordinate = TileManager.positionToCoordinate(new Point(x, y));
		this.gamePanel = gamePanel;
		this.tileManager = tileManager;
		this.viewport = viewport;
		this.itemManager = itemManager;
	}
	
	public void move(Graphics2D graphics2D) {
		Buildable buildable = tileManager.coordinateToBuildable.get(coordinate);
		Conveyor conveyor = (buildable != null && buildable instanceof Conveyor) ? (Conveyor) buildable : null;
		boolean buildingConveyor = false;
		
		if (conveyor == null && buildable != null && buildable.buildingConveyor != null) {
			conveyor = buildable.buildingConveyor;
			buildingConveyor = true;
		}
		
		if (conveyor != null) {
			if (!clogged) {
				int direction = conveyor.input > -1
						                ? (conveyor.input + 2) % 4
						                : conveyor.output;
				
				Point offset = TileManager.moveInDirection(direction, GamePanel.tileSize / 2 + Item.size / 2);
				
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
				
				Point point = new Point(x + offset.x, y + offset.y);
				Point offsetCoordinate = TileManager.positionToCoordinate(point);
				
				// Get previous conveyor coordinate
				Point previousCoordinateOffset = TileManager.moveInDirection(direction, 1);
				Point previousCoordinate = new Point(coordinate.x + previousCoordinateOffset.x / 2, coordinate.y + previousCoordinateOffset.y / 2);
				
				if (!offsetCoordinate.equals(previousCoordinate)) {
					if (!buildingConveyor && conveyor.output != -1) {
						direction = conveyor.output;
						graphics2D.setColor(Color.yellow);
					} else if (buildable instanceof Building) {
						// Only process item if it has passed the center of the building
						Point nextPositionOffset = TileManager.moveInDirection(direction, GamePanel.tileSize / 2 + Item.size / 2);
						Point nextPosition = new Point(x + nextPositionOffset.x, y + nextPositionOffset.y);
						
						Point nextCoordinate = TileManager.positionToCoordinate(nextPosition);
						
						if (!nextCoordinate.equals(coordinate)) {
							((Building) buildable).processItem(this);
							graphics2D.setColor(Color.green);
						}
					}
				} else {
					graphics2D.setColor(Color.cyan);
				}
				
				if (UI.showItemOrientation) {
					graphics2D.fillRect(point.x, point.y, (int) GamePanel.tileScaleMultiplier, (int) GamePanel.tileScaleMultiplier);
					graphics2D.setColor(Color.white);
					graphics2D.fillRect(x, y, (int) GamePanel.tileScaleMultiplier, (int) GamePanel.tileScaleMultiplier);
				}
				
				
				Point movement = TileManager.moveInDirection(direction, (int) Math.round(gamePanel.scaledDeltaTime * Conveyor.itemSpeed));
				
				x += movement.x;
				y += movement.y;
				coordinate = TileManager.positionToCoordinate(new Point(x, y));
			}
			
			if (lastBuilding != null && !coordinate.equals(lastBuilding.coordinate))
				lastBuilding = null;
			
			checkForClogs(buildable, conveyor);
		} else {
			clogged = true;
		}
	}
	
	public void checkForClogs(Buildable buildable, Conveyor conveyor) {
		clogged = false;
		
		if (buildable instanceof Building) {
			Building building = (Building) (buildable);
			
			if (building != lastBuilding && building.activeRecipe != null && name != building.activeRecipe.inputItem.name) {
				clogged = true;
			}
		}
		
		for (int i = 0; i < itemManager.items.size(); i++) {
			Item item = itemManager.items.get(i);
			boolean inFront = conveyor.mirrorSprite ? x > item.x || y > item.y : x < item.x || y < item.y;
			
			if (item.clogged && inFront) {
				double distance = (x - item.x) * (x - item.x) + (y - item.y) * (y - item.y);
				double minDistance = (clogGap + size) * (clogGap + size);
				
				if (distance < minDistance) {
					clogged = true;
				}
			}
		}
	}
	
	public void draw(Graphics2D graphics2D) {
		viewport.drawSprite(graphics2D, sprite, (x + offset.x - size / 2), (y + offset.y - size / 2), size, size, 1, true);
	}
	
}
