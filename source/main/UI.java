package source.main;

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
import java.awt.image.BufferedImage;

import source.tile.TileManager;
import source.tile.Tile;

public class UI {

	// Debugging
	public static final boolean showConnections = false;
	public static final boolean showItemOrientation = false;
	public static final boolean showCurrentFrame = false;

	final int invSlotSize = 125;
	final int invSlotIconSize = 100;
	final int invSlotRadius = 25;
	final int invHorizontalMargin = 25;
	final int invVerticalMargin = 50;

	public final static Color backgroundColorA = new Color(0.09f, 0.07f, 0.15f, 0.85f);
	public final static Color backgroundColorB = new Color(0.09f, 0.07f, 0.15f, 0.95f);
	
	GamePanel gamePanel;
	TileManager tileManager;
	public static Font font;

	public static Tile hoveringTile;

	Map<String, Tile> buildables = new HashMap<String, Tile>();

	public UI(GamePanel gamePanel, TileManager tileManager) {
		this.gamePanel = gamePanel;
		this.tileManager = tileManager;

		tileManager.addTile(buildables, new String[]{"conveyor"});
		tileManager.addTile(buildables, new String[]{"smelter"});
		tileManager.addTile(buildables, new String[]{"importer"});
		tileManager.addTile(buildables, new String[]{"exporter"});
		
		try {
			InputStream inputStream = getClass().getResourceAsStream("fonts/Retro Gaming.ttf");
			font = Font.createFont(Font.TRUETYPE_FONT, inputStream);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void draw(Graphics2D graphics2D) {
		graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON); 

		graphics2D.setFont(font);
		graphics2D.setColor(Color.white);
		graphics2D.setFont(graphics2D.getFont().deriveFont(font.PLAIN, 33f));
		graphics2D.drawString("Score: " + gamePanel.score, 50, 50);

		drawInventory(graphics2D);
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

			if (Mouse.mousePosition.x >= x && Mouse.mousePosition.x < x + invSlotSize && Mouse.mousePosition.y >= yStart && Mouse.mousePosition.y < yStart + invSlotSize)
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
