package dev.prozilla.funkyfactory.buildable.connectable;

import dev.prozilla.funkyfactory.buildable.Connectable;
import dev.prozilla.funkyfactory.main.GamePanel;
import dev.prozilla.funkyfactory.main.Viewport;
import dev.prozilla.funkyfactory.tile.Tile;
import dev.prozilla.funkyfactory.tile.TileManager;

public class Conveyor extends Connectable {
	
	public static final int borderWidth = 3;
	public static float itemSpeed = 1;
	
	public Conveyor(int x, int y, Tile tile, GamePanel gamePanel, TileManager tileManager, Viewport viewport) {
		super(x, y, tile, gamePanel, tileManager, viewport);
		super.frameCount = 8;
		this.animationSpeed = 12;
	}
	
	@Override
	public void setConnection(boolean isInput, int direction) {
		super.setConnection(isInput, direction);
		
		spriteVariant = 0;
		currentSprite = tile.sprites[0];
		rotation = 0;
		cropToOutput = false;
		cropToInput = false;
		
		curved = (input > -1 && output > -1 && !((connections.contains(0) && connections.contains(2)) || (connections.contains(1) && connections.contains(3))));
		
		if (!curved) {
			mirrorSprite = input == 3 || input == 0 || output == 1 || output == 2;
			
			if (connections.contains(2) || connections.contains(0)) {
				rotation = 90;
			}
			
			if (input == -2) {
				cropToOutput = true;
			} else if (output == -2) {
				cropToInput = true;
			}
			
			if (cropToInput || cropToOutput) {
				rotation += 180;
				mirrorSprite = !mirrorSprite;
			}
		} else {
			if (input == (output + 1) % 4) {
				// Right turn
				mirrorSprite = true;
				rotation += 180;
			} else {
				// Left turn
				mirrorSprite = false;
				rotation += 90;
			}
			
			if (connections.contains(1) && connections.contains(2)) {
				currentSprite = tile.sprites[1];
			} else if (connections.contains(2) && connections.contains(3)) {
				currentSprite = tile.sprites[1];
				rotation += 90;
			} else if (connections.contains(3) && connections.contains(0)) {
				currentSprite = tile.sprites[1];
				rotation += 180;
			} else if (connections.contains(0) && connections.contains(1)) {
				currentSprite = tile.sprites[1];
				rotation += 270;
			}
			
			rotation += 180;
			
			if (mirrorSprite) {
				rotation -= 90;
			} else {
				rotation += 90;
			}
		}
	}
	
}
