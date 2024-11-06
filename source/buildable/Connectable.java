package source.buildable;

import source.main.GamePanel;
import source.main.Viewport;
import source.tile.Tile;
import source.tile.TileManager;

public class Connectable extends Buildable {
	
	public Connectable(int x, int y, Tile tile, GamePanel gamePanel, TileManager tileManager, Viewport viewport) {
		super(x, y, tile, gamePanel, tileManager, viewport);
	}
	
	public static void updateBuildableConnections(Buildable centerBuildable, Buildable[] neighbourBuildables, boolean setNeighbourConnections) {
		if (centerBuildable != null) {
			// Check if any neighbouring buildables already have an input or output
			for (int i = 0; i < neighbourBuildables.length; i++) {
				Buildable neighbourBuildable = neighbourBuildables[i];
				int oppositeDirection = (i + 2) % 4;
				
				// Ignores loose conveyors
				if (neighbourBuildable != null && !(neighbourBuildable.input == -1 && neighbourBuildable.output == -1)) {
					if ((neighbourBuildable.input == -1 || (neighbourBuildable.buildingConveyor != null && neighbourBuildable.input == oppositeDirection))
							    && neighbourBuildable.output != -1 && centerBuildable.output == -1) {
						// Connect output
						if (setNeighbourConnections)
							neighbourBuildable.setConnection(true, oppositeDirection);
						centerBuildable.setConnection(false, i);
					} else if ((neighbourBuildable.output == -1 || (neighbourBuildable.buildingConveyor != null && neighbourBuildable.output == oppositeDirection))
							           && neighbourBuildable.input != -1 && centerBuildable.input == -1) {
						// Connect input
						if (setNeighbourConnections)
							neighbourBuildable.setConnection(false, oppositeDirection);
						centerBuildable.setConnection(true, i);
					}
				}
			}
			
			// Connect any loose conveyors
			for (int i = 0; i < neighbourBuildables.length; i++) {
				Buildable neighbourBuildable = neighbourBuildables[i];
				int oppositeDirection = (i + 2) % 4;
				
				if (neighbourBuildable != null && neighbourBuildable.input == -1 && neighbourBuildable.output == -1 && centerBuildable.input == -1) {
					// Connect input
					if (setNeighbourConnections)
						neighbourBuildable.setConnection(false, oppositeDirection);
					centerBuildable.setConnection(true, i);
				}
			}
		} else {
			// Remove connections to center buildable
			for (int i = 0; i < neighbourBuildables.length; i++) {
				if (neighbourBuildables[i] != null) {
					Buildable neighbourBuildable = neighbourBuildables[i];
					int oppositeDirection = (i + 2) % 4;
					
					if (neighbourBuildable.input == oppositeDirection && (neighbourBuildable.buildingConveyor == null || neighbourBuildable.allowAutoRotation)) {
						// Remove input of neighbour
						neighbourBuildable.setConnection(true, -1);
					} else if (neighbourBuildable.output == oppositeDirection && (neighbourBuildable.buildingConveyor == null || neighbourBuildable.allowAutoRotation)) {
						// Remove output of neighbour
						neighbourBuildable.setConnection(false, -1);
					}
				}
			}
		}
	}
	
}
