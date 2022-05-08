package source.tile;

import java.awt.image.BufferedImage;

public class Tile {

	public String name;
	public BufferedImage[] sprites;
	public boolean animated = false;

	public Tile(String name, BufferedImage[] sprites) {
		this.name = name;
		this.sprites = sprites;

		animated = sprites.length > 1;
	}
	
}
