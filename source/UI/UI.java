package source.UI;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.IOException;
import java.io.InputStream;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;

import source.tile.TileManager;
import source.main.GamePanel;
import source.main.Mouse;
import source.tile.Tile;

public class UI {

	// Debugging
	public static final boolean showConnections = false;
	public static final boolean showItemOrientation = false;
	public static final boolean showCurrentFrame = false;

	final String fontsPath = "fonts/";
	final String iconsPath = "icons/";

	final int invSlotSize = 100;
	final int invSlotIconSize = 75;
	final int invSlotRadius = 25;
	final int invHorizontalMargin = 20;
	final int invVerticalMargin = 35;

	public final static Color backgroundColorA = new Color(0.09f, 0.07f, 0.15f, 0.85f);
	public final static Color backgroundColorB = new Color(0.09f, 0.07f, 0.15f, 0.95f);
	
	GamePanel gamePanel;
	TileManager tileManager;

	public static Font font;
	final int fontSize = 33;

	public static Tile hoveringTile;

	Map<String, Tile> buildables = new HashMap<String, Tile>();
	Map<String, BufferedImage> iconTextures;

	public UI(GamePanel gamePanel, TileManager tileManager) {
		this.gamePanel = gamePanel;
		this.tileManager = tileManager;

		tileManager.addTile(buildables, new String[]{"conveyor"});
		tileManager.addTile(buildables, new String[]{"smelter"});
		tileManager.addTile(buildables, new String[]{"importer"});
		tileManager.addTile(buildables, new String[]{"exporter"});
		
		try {
			InputStream inputStream = getClass().getResourceAsStream(fontsPath + "Retro Gaming.ttf");
			font = Font.createFont(Font.TRUETYPE_FONT, inputStream);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		addIcons();
	}

	public void addIcons() {
		iconTextures = new HashMap<String, BufferedImage>();

		addIcon("coin");
	}

	public void addIcon(String name) {
		try {
			System.out.println(String.format("Reading %s%s.png", iconsPath, name));
			iconTextures.put(name, ImageIO.read(getClass().getResourceAsStream(String.format("%s%s.png", iconsPath, name))));
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	public void draw(Graphics2D graphics2D) {
		graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		drawScore(graphics2D);
		drawInventory(graphics2D);
	}

	public void drawScore(Graphics2D graphics2D) {
		graphics2D.drawImage(iconTextures.get("coin"), 50, 50 - (int)(fontSize / 10f * 9f), fontSize, fontSize, null);

		graphics2D.setFont(font);
		graphics2D.setColor(Color.white);
		graphics2D.setFont(graphics2D.getFont().deriveFont(Font.PLAIN, fontSize));
		graphics2D.drawString(Integer.toString(gamePanel.score), 50 + fontSize + 10, 50);
	}

	public void drawInventory(Graphics2D graphics2D) {
		int xStart = invHorizontalMargin;
		int yStart = gamePanel.height - invSlotSize - invVerticalMargin;
		hoveringTile = null;

		List<String> keyList = new ArrayList<String>(buildables.keySet());
		for(int i = 0; i < buildables.size(); i++) {
			int x = xStart + (invHorizontalMargin + invSlotSize) * i;
			String key = keyList.get(i);
			Tile tile = buildables.get(key);
			boolean hovering = false;

			if (Mouse.mousePosition != null && Mouse.mousePosition.x >= x && Mouse.mousePosition.x < x + invSlotSize && Mouse.mousePosition.y >= yStart && Mouse.mousePosition.y < yStart + invSlotSize)
			{
				hoveringTile = tile;
				hovering = true;
			}

			drawInventorySlot(graphics2D, x, yStart, tile.sprites[0], hovering);
		}
	}

	public void drawInventorySlot(Graphics2D graphics2D, int x, int y, BufferedImage icon, boolean hovering) {
		graphics2D.setColor(hovering ? backgroundColorB : backgroundColorA);

		graphics2D.fillRoundRect(x, y, invSlotSize, invSlotSize, invSlotRadius, invSlotRadius);

		int difference = (invSlotSize - invSlotIconSize) / 2;
		int iconX = x + difference;
		int iconY = y + difference;

		if (icon.getWidth() > GamePanel.originalTileSize || icon.getHeight() > GamePanel.originalTileSize)
			icon = icon.getSubimage(0, 0, GamePanel.originalTileSize, GamePanel.originalTileSize);

		graphics2D.drawImage(icon, iconX, iconY, invSlotIconSize, invSlotIconSize, null);
	}

}
