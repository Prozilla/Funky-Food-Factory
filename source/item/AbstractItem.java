package source.item;

import java.awt.image.BufferedImage;

public class AbstractItem {
	
	public String name;
	public BufferedImage sprite;
	
	public AbstractItem(String name, BufferedImage sprite) {
		this.name = name;
		this.sprite = sprite;
	}
	
}
