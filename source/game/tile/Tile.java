package source.game.tile;

import java.awt.image.BufferedImage;

public class Tile {

	// Tile names
	public static final String COMPRESSOR = "compressor";
	public static final String CONVEYOR = "conveyor";
	public static final String CURVED_CONVEYOR = "curved_conveyor";
	public static final String CUTTER = "cutter";
	public static final String EXPORTER = "exporter";
	public static final String FLOOR = "floor";
	public static final String IMPORTER = "importer";
	public static final String SMELTER = "smelter";

	public String name;
	public BufferedImage[] sprites;
	public boolean animated = false;

	public Tile(String name, BufferedImage[] sprites) {
		this.name = name;
		this.sprites = sprites;

		animated = sprites.length > 1;
	}
	
}
