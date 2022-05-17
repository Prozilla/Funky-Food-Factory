package source.buildable.building;

import java.awt.Graphics2D;
import java.awt.Point;

import source.buildable.Building;
import source.buildable.connectable.Conveyor;
import source.main.GamePanel;
import source.tile.Tile;
import source.tile.TileManager;

public class Importer extends Building {

	float itemSpawnDelay = 1f;
	double timeUntilNextSpawn = itemSpawnDelay;

	boolean stopSpawning = false;

	public Importer(int x, int y, int rotation, Tile tile, GamePanel gamePanel, TileManager tileManager) {
		super(x, y, tile, gamePanel, tileManager);
		this.rotation = rotation;
		this.input = -2;
		this.addConveyor(this.rotation);
	}

	@Override
	public void draw(Graphics2D graphics2D) {
		Conveyor conveyor = (Conveyor)tileManager.coordinateToBuildable.get(new Point(coordinate.x + 1, coordinate.y));

		if (conveyor != null) {
			timeUntilNextSpawn -= gamePanel.deltaTime / gamePanel.fps;

			if (timeUntilNextSpawn <= 0) {
				timeUntilNextSpawn = itemSpawnDelay;

				if (!stopSpawning) {
					tileManager.spawnItem("iron_ore", coordinate);
					// stopSpawning = true;
				}
			}
		} else {
			timeUntilNextSpawn = itemSpawnDelay;
		}

		super.draw(graphics2D);
	}
	
}
