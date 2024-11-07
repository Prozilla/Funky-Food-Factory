package dev.prozilla.funkyfactory.buildable;

import dev.prozilla.funkyfactory.UI.UI;
import dev.prozilla.funkyfactory.buildable.connectable.Conveyor;
import dev.prozilla.funkyfactory.main.GamePanel;
import dev.prozilla.funkyfactory.main.Viewport;
import dev.prozilla.funkyfactory.tile.Tile;
import dev.prozilla.funkyfactory.tile.TileManager;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Buildable {
	
	public int x = 0;
	public int y = 0;
	public Point coordinate = new Point(0, 0);
	
	public Conveyor buildingConveyor;
	
	// Animation
	public float animationSpeed = 10;
	public int frameCount = 1;
	int frame = 0;
	float frameTime = 0;
	public BufferedImage currentSprite;
	public int spriteVariant = 0;
	public int tileSize = GamePanel.originalTileSize;
	
	public int spawnAnimationDuration = 10;
	
	// Sprite properties
	public int rotation = 0;
	public boolean mirrorSprite = false;
	public boolean cropToOutput = false;
	public boolean cropToInput = false;
	
	// Connections (-1: unset, -2: NaN)
	public int input = -1;
	public int output = -1;
	public boolean curved;
	public boolean allowAutoRotation = false;
	
	double age = 0;
	double death; // TO DO: Should be set to the current age when building gets deleted (to do remove animation)
	
	public ArrayList<Integer> connections = new ArrayList<Integer>();
	
	public Tile tile;
	public GamePanel gamePanel;
	public TileManager tileManager;
	public Viewport viewport;
	
	public Buildable(int x, int y, Tile tile, GamePanel gamePanel, TileManager tileManager, Viewport viewport) {
		this.x = x;
		this.y = y;
		this.coordinate = TileManager.positionToCoordinate(new Point(x, y));
		this.tile = tile;
		this.currentSprite = tile.sprites[0];
		this.gamePanel = gamePanel;
		this.tileManager = tileManager;
		this.viewport = viewport;
	}
	
	public void setConnection(boolean isInput, int direction) {
		if (isInput && input != -2) {
			input = direction;
		} else if (!isInput && output != -2) {
			output = direction;
		}
		
		if (buildingConveyor != null) {
			buildingConveyor.setConnection(true, this.input);
			buildingConveyor.setConnection(false, this.output);
		}
		
		connections = new ArrayList<Integer>();
		connections.add(input);
		connections.add(output);
	}
	
	public void addConveyor() {
		buildingConveyor = new Conveyor(x, y, tileManager.tiles.get("conveyor"), gamePanel, tileManager, viewport);
	}
	
	public float easeOutQuad(float time) {
		return 1 - (1 - time) * (1 - time);
	}
	
	public void draw(Graphics2D graphics2D, boolean isGhost) {
		if (frameCount > 0) {
			int frameOffset = 0;
			// int frameOffset = (coordinate.x % 3) * 4 + ((coordinate.y) % 3) * -4;
			
			// if (curved) {
			// 	frameOffset = (coordinate.x % 3) * -4;
			// }
			
			frame = Math.abs((int) Math.round(gamePanel.scaledTime * animationSpeed) + (mirrorSprite ? frameOffset : -frameOffset)) % frameCount;
		}
		
		// Crop to current frame
		BufferedImage sprite = currentSprite.getSubimage(0, frame * tileSize, tileSize, tileSize);
		
		// Crop to a side
		if (cropToOutput) {
			sprite = sprite.getSubimage(0, 0, tileSize / 2, tileSize);
		} else if (cropToInput) {
			sprite = sprite.getSubimage(tileSize / 2, 0, tileSize / 2, tileSize);
		}
		
		// Mirror image
		if (mirrorSprite) {
			sprite = TileManager.mirrorImageVertically(sprite);
		}
		
		// Apply rotation
		if (rotation != 0) {
			sprite = TileManager.rotateImage(sprite, rotation);
		}
		
		// Apply cropping
		int spriteX = x;
		int spriteY = y;
		int width = GamePanel.tileSize;
		int height = GamePanel.tileSize;
		
		if (rotation != 90) {
			if (((cropToOutput && !mirrorSprite) || (cropToInput && mirrorSprite))) {
				spriteX += GamePanel.tileSize / 2;
			}
			
			if (cropToInput || cropToOutput) {
				width = GamePanel.tileSize / 2;
			}
		} else {
			if (((cropToOutput && !mirrorSprite) || (cropToInput && mirrorSprite))) {
				spriteY += GamePanel.tileSize / 2;
			}
			
			if (cropToInput || cropToOutput) {
				height = GamePanel.tileSize / 2;
			}
		}
		
		if (!isGhost) {
			// Apply animation
			float scale = age < spawnAnimationDuration ? easeOutQuad((float) (age / spawnAnimationDuration)) : 1f;
			viewport.drawSprite(graphics2D, sprite, (int) (spriteX + (width - width * scale) / 2), (int) (spriteY + (height - height * scale) / 2), (int) (width * scale), (int) (height * scale), scale, false);
		} else {
			viewport.drawSprite(graphics2D, sprite, x, y, GamePanel.tileSize, GamePanel.tileSize, 0.25f, false);
		}
		
		if (UI.showConnections) {
			if (input != -1 && input != -2) {
				drawDirection(true, input, graphics2D);
			}
			
			if (output != -1 && output != -2) {
				drawDirection(false, output, graphics2D);
			}
		}
		
		age += gamePanel.deltaTime;
		
		if (UI.showCurrentFrame) {
			graphics2D.setColor(UI.backgroundColorB);
			graphics2D.setFont(graphics2D.getFont().deriveFont(Font.PLAIN, 22f));
			graphics2D.drawString(frame + "", x, y + 11);
		}
	}
	
	public void drawDirection(boolean in, int direction, Graphics2D graphics2D) {
		Point point = new Point(0, 0);
		int offset = GamePanel.tileSize / 3;
		int size = in ? GamePanel.tileSize / 6 : GamePanel.tileSize / 7;
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
