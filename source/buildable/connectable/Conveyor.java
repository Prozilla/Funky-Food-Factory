package source.buildable.connectable;

import source.buildable.Connectable;
import source.main.GamePanel;
import source.main.Viewport;
import source.tile.Tile;
import source.tile.TileManager;

public class Conveyor extends Connectable {

	public static final int borderWidth = 3;
	public static float itemSpeed = 0.5f;

	public Conveyor(int x, int y, Tile tile, GamePanel gamePanel, TileManager tileManager, Viewport viewport) {
		super(x, y, tile, gamePanel, tileManager, viewport);
		super.frameCount = 8;
		this.animationSpeed = 30;
	}

	@Override
	public void setConnection(boolean isInput, int direction) {
		super.setConnection(isInput, direction);

		spriteVariant = 0;
		currentSprite = tile.sprites[0];
		rotation = 0;
		cropToInput = false;
		cropToOutput = false;

		curved = (input > -1 && output > -1 && !((connections.contains(0) && connections.contains(2)) || (connections.contains(1) && connections.contains(3))));

		if (!curved) {
			if (input == 3 || input == 0 || output == 1 || output == 2) {
				mirrorSprite = true;
			} else {
				mirrorSprite = false;
			}

			if (connections.contains(2) || connections.contains(0)) {
				rotation = 90;
			}

			if (input == -2) {
				cropToOutput = true;
			} else if (output == -2) {
				cropToInput = true;
			}
		} else {
			if (input == (output + 1) % 4) {
				mirrorSprite = true;
				rotation += 270;
			} else {
				mirrorSprite = false;
			}

			if (connections.contains(1) && connections.contains(2)) {
				currentSprite = tile.sprites[1];
				spriteVariant = mirrorSprite ? 2 : 3;
				rotation += 0;
			} else if (connections.contains(2) && connections.contains(3)) {
				currentSprite = tile.sprites[1];
				rotation += 90;
			} else if (connections.contains(3) && connections.contains(0)) {
				currentSprite = tile.sprites[1];
				spriteVariant = mirrorSprite ? 3 : 2;
				rotation += 180;
			} else if (connections.contains(0) && connections.contains(1)) {
				currentSprite = tile.sprites[1];
				spriteVariant = 1;
				rotation += 270;
			}
		}
	}
	
}
