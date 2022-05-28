package source.item;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.awt.Graphics2D;
import java.util.Map;
import javax.imageio.ImageIO;

import source.buildable.connectable.Conveyor;
import source.main.GamePanel;
import source.main.Viewport;
import source.tile.TileManager;

public class ItemManager {

	final String itemsPath = "textures/";
	final String recipesPath = "building/recipes/Recipes.json";

	public Map<String, BufferedImage> itemTextures;
	public ArrayList<Item> items = new ArrayList<Item>();

	public Map<Item, Item> recipes;

	GamePanel gamePanel;
	TileManager tileManager;
	Viewport viewport;

	public ItemManager(GamePanel gamePanel, TileManager tileManager, Viewport viewport) {
		this.gamePanel = gamePanel;
		this.tileManager = tileManager;
		this.viewport = viewport;

		addItems();
		addRecipes();
	}

	public void addItems() {
		itemTextures = new HashMap<String, BufferedImage>();

		addItem("iron_ore");
		addItem("iron_ingot");
	}

	public void addItem(String name) {
		try {
			System.out.println(String.format("Reading %s%s.png", itemsPath, name));
			itemTextures.put(name, ImageIO.read(getClass().getResourceAsStream(String.format("%s%s.png", itemsPath, name))));
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	public void addRecipes() {
		// recipes.put("")
	}

	public void spawnItem(String name, Point coordinate) {
		int xOffset = GamePanel.tileSize / 2;
		int yOffset = GamePanel.tileSize / 2;

		Point center = new Point(coordinate.x * GamePanel.tileSize + xOffset, coordinate.y * GamePanel.tileSize + yOffset);

		float range = GamePanel.tileSize - GamePanel.tileScaleMultiplier * Conveyor.borderWidth;
		Point offset = new Point((int)Math.round(Math.random() * range - range / 2), (int)Math.round(Math.random() * range - range / 2));

		Item item = new Item(center.x, center.y, name, itemTextures.get(name), gamePanel, tileManager, viewport, this);
		// item.offset = offset;

		items.add(item);
	}

	public void drawItems(Graphics2D graphics2D) {
		for (int i = 0; i < items.size(); i++) {
			Item item = items.get(i);

			item.move(graphics2D);
			item.draw(graphics2D);
		}
	}

}
