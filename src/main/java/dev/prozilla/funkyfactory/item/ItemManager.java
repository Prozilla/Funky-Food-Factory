package dev.prozilla.funkyfactory.item;

import dev.prozilla.funkyfactory.main.GamePanel;
import dev.prozilla.funkyfactory.main.Viewport;
import dev.prozilla.funkyfactory.tile.TileManager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ItemManager {
	
	public final static String texturesPath = "/textures/";
	public final String recipesPath = "/recipes/Recipes.json";
	
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
		
		addItem(Item.COPPER_ORE);
		addItem(Item.COPPER_INGOT);
		addItem(Item.COPPER_ROD);
		addItem(Item.IRON_ORE);
		addItem(Item.IRON_INGOT);
		addItem(Item.IRON_SCREW);
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
		
		Item item = new Item(center.x, center.y, abstractItems.get(name), gamePanel, tileManager, viewport, this);
		
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
