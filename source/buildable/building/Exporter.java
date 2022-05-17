package source.buildable.building;

import source.buildable.Building;
import source.main.GamePanel;
import source.tile.Tile;
import source.tile.TileManager;

public class Exporter extends Building {

	public Exporter(int x, int y, int rotation, Tile tile, GamePanel gamePanel, TileManager tileManager) {
		super(x, y, tile, gamePanel, tileManager);
		this.rotation = rotation;
		this.output = -2;
		this.addConveyor(this.rotation);
	}
	
}
