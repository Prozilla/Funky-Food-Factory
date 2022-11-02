package source.item;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.IOException;
import java.awt.Graphics2D;
import java.util.Map;
import javax.imageio.ImageIO;

import source.buildable.connectable.Conveyor;
import source.main.GamePanel;
import source.main.Viewport;
import source.tile.TileManager;

public class ItemManager {

	public final static String texturesPath = "textures/";
	public final String recipesPath = "building/recipes/Recipes.json";

	public static Map<String, AbstractItem> abstractItems;
	public ArrayList<Item> items = new ArrayList<Item>();

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
		abstractItems = new HashMap<String, AbstractItem>();

		addItem(Item.CORN);
		addItem(Item.LETTUCE);
		addItem(Item.MELON);
		addItem(Item.MELON_SLICE);
		addItem(Item.PIZZA);
		addItem(Item.POPCORN);
		addItem(Item.SALAD);
		addItem(Item.TOMATO);
		addItem(Item.TOMATO_SAUCE);
	}

	public void addItem(String name) {
		try {
			String path = String.format("%s%s.png", texturesPath, name);
			System.out.println("Reading: " + path);
			abstractItems.put(name, new AbstractItem(name, ImageIO.read(getClass().getResourceAsStream(path))));
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	// TO DO: Load recipes from JSON file
	public void addRecipes() {
		// recipes.put("")
	}

	public void spawnItem(String name, Point coordinate) {
		int xOffset = GamePanel.tileSize / 2;
		int yOffset = GamePanel.tileSize / 2;

		Point center = new Point(coordinate.x * GamePanel.tileSize + xOffset, coordinate.y * GamePanel.tileSize + yOffset);

		float range = GamePanel.tileSize - GamePanel.tileScaleMultiplier * Conveyor.borderWidth;
		Point offset = new Point((int)Math.round(Math.random() * range - range / 2), (int)Math.round(Math.random() * range - range / 2));

		Item item = new Item(center.x, center.y, abstractItems.get(name), gamePanel, tileManager, viewport, this);
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
