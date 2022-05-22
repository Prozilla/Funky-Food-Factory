package source.buildable;

import java.awt.Point;

import source.main.GamePanel;
import source.tile.Tile;
import source.tile.TileManager;

public class Connectable extends Buildable {

	public Connectable(int x, int y, Tile tile, GamePanel gamePanel, TileManager tileManager) {
		super(x, y, tile, gamePanel, tileManager);
	}

	public static void updateBuildableConnections(Buildable centerBuildable, Buildable[] neighbourBuildables) {
		if (centerBuildable != null) {
			for (int i = 0; i < neighbourBuildables.length; i++) {
				if (neighbourBuildables[i] != null) {
					int oppositeDirection = (i + 2) % 4;

					// Neighbouring buildings already has an input, it should force it to connect to this building
					if (neighbourBuildables[i].input != -1 && neighbourBuildables[i].output == -1) {
						neighbourBuildables[i].setConnection(false, oppositeDirection);
						centerBuildable.setConnection(true, i);
					}
				}
			}

			for (int i = 0; i < neighbourBuildables.length; i++) {
				if (neighbourBuildables[i] != null) {
					int oppositeDirection = (i + 2) % 4;

					if (centerBuildable.input == -1 && neighbourBuildables[i].output != -2) {
						neighbourBuildables[i].setConnection(false, oppositeDirection);
						centerBuildable.setConnection(true, i);
					} else if (centerBuildable.input != i && neighbourBuildables[i].input == -1) {
						neighbourBuildables[i].setConnection(true, oppositeDirection);
						centerBuildable.setConnection(false, i);
					}
				}
			}

			// System.out.println("final in: " + building.input);
			// System.out.println("final out: " + building.output);
		} else {
			for (int i = 0; i < neighbourBuildables.length; i++) {
				if (neighbourBuildables[i] != null) {
					int oppositeDirection = (i + 2) % 4;

					if (neighbourBuildables[i].input == oppositeDirection) {
						neighbourBuildables[i].setConnection(true, -1);
					} else if (neighbourBuildables[i].output == oppositeDirection) {
						neighbourBuildables[i].setConnection(false, -1);
					}
				}
			}
		}
	}
	
}
