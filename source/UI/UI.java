package source.UI;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.io.IOException;
import java.io.InputStream;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Color;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import javax.imageio.ImageIO;
import java.awt.Cursor;
import java.awt.image.BufferedImage;

import source.tile.TileManager;
import source.buildable.Buildable;
import source.buildable.Building;
import source.main.GamePanel;
import source.main.Mouse;
import source.tile.Tile;

public class UI {

	public static UI instance = new UI();

	// Debugging
	public static final boolean showConnections = false;
	public static final boolean showItemOrientation = false;
	public static final boolean showCurrentFrame = false;

	final String fontsPath = "fonts/";
	final String iconsPath = "icons/";

	public final static Color backgroundColorA = Color.decode("#31373D");
	public final static Color backgroundColorB = Color.decode("#25292e");
	public final static Color backgroundColorC = Color.decode("#181b1f");
	
	public GamePanel gamePanel;
	TileManager tileManager;

	final String fontName = "Poppins-Bold";
	public static Font font;
	public final static int fontSize = 33;
	public final static int cornerRadius = 25;
	public final static int borderWidth = 2;

	final int invSlotSize = 100;
	final int invSlotIconSize = 75;
	final int invSlotGap = 20;
	final int invHorizontalMargin = 35;
	final int invVerticalMargin = 35;

	final int scoreTextSize = fontSize;
	final int scoreGap = 10;
	final int scoreMargin = 35;
	final int scorePadding = 10;
	final int scoreIconSize = fontSize;

	final int menuButtonSize = 30;
	final int menuMargin = 25;
	final int menuGap = 20;

	int scoreTextWidth;
	String previousScoreText;

	public static Modal currentModal;
	public static UIElement hoveringElement = null;
	public static Tile hoveringInventoryTile;
	public static Integer hoveringMenuButton = null;

	Map<String, Tile> buildables = new HashMap<String, Tile>();
	public Map<String, BufferedImage> iconTextures;

	int currentCursor;

	private UI() {
		try {
			InputStream inputStream = getClass().getResourceAsStream(String.format("%s%s.ttf", fontsPath, fontName));
			font = Font.createFont(Font.TRUETYPE_FONT, inputStream);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		addIcons();
	}

	public void setTileManager(TileManager tileManager) {
		this.tileManager = tileManager;

		tileManager.addTile(buildables, new String[]{Tile.CONVEYOR});
		tileManager.addTile(buildables, new String[]{Tile.SMELTER});
		tileManager.addTile(buildables, new String[]{Tile.IMPORTER});
		tileManager.addTile(buildables, new String[]{Tile.EXPORTER});
		tileManager.addTile(buildables, new String[]{Tile.COMPRESSOR});
		tileManager.addTile(buildables, new String[]{Tile.CUTTER});
	}

	public void addIcons() {
		iconTextures = new HashMap<String, BufferedImage>();

		addIcon(Icon.COIN);
		addIcon(Icon.ARROW);
		addIcon(Icon.PLAY);
		addIcon(Icon.PAUSE);
		addIcon(Icon.TOOLS);
		addIcon(Icon.WAREHOUSE);
	}

	public void addIcon(String name) {
		try {
			System.out.println(String.format("Reading %s%s.png", iconsPath, name));
			iconTextures.put(name, ImageIO.read(getClass().getResourceAsStream(String.format("%s%s.png", iconsPath, name))));
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	public void clickMenuButton(int index) {
		switch (index) {
			case 0:
				gamePanel.togglePause();
				break;
		}
	}

	public void draw(Graphics2D graphics2D) {
		graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		if (UI.hoveringElement != null)
			UI.hoveringElement.hovering = true;

		drawInventory(graphics2D);
		drawModal(graphics2D);
		drawScore(graphics2D);
		drawMenu(graphics2D);

		// Check if hovering a UI element that should show the hand cursor
		boolean hoveringInteractable = UI.hoveringInventoryTile != null || (UI.hoveringElement != null && UI.hoveringElement.handCursor) || hoveringMenuButton != null;

		if (!hoveringInteractable) {
			// Check if hovering a building with a modal
			Buildable hoveringBuildable = tileManager.coordinateToBuildable.get(Mouse.viewportMouseCoordinate);

			if (hoveringBuildable != null && hoveringBuildable instanceof Building && ((Building)hoveringBuildable).hasModal)
				hoveringInteractable = true;
		}

		int newCursor = hoveringInteractable ? Cursor.HAND_CURSOR : Cursor.DEFAULT_CURSOR;

		if (newCursor != currentCursor) {
			Cursor cursor = Cursor.getPredefinedCursor(newCursor);
			gamePanel.setCursor(cursor);
			currentCursor = newCursor;
		}
	}

	void drawModal(Graphics2D graphics2D) {
		if (currentModal != null) {
			currentModal.checkHoverState();
			currentModal.draw(graphics2D);
		}
	}

	void setScoreSize(Graphics2D graphics2D, String text) {
		scoreTextWidth = graphics2D.getFontMetrics().stringWidth(text);
	}

	void drawScore(Graphics2D graphics2D) {
		Point position = new Point(scoreMargin, scoreMargin);

		// Calculate text width
		String text = Integer.toString(gamePanel.score);
		graphics2D.setFont(font.deriveFont(Font.PLAIN, scoreTextSize));
		if (previousScoreText == null || previousScoreText != text)
			setScoreSize(graphics2D, text);

		// Draw background
		int backgroundWidth = scoreIconSize + scoreGap + scoreTextWidth + scorePadding * 2;
		int backgroundHeight = scoreIconSize + scorePadding * 2;
		UIElement.drawBackground(graphics2D, new Point(position.x - scorePadding, position.y - scorePadding), new Dimension(backgroundWidth, backgroundHeight), backgroundColorA, cornerRadius);

		// Draw icon
		graphics2D.drawImage(iconTextures.get(Icon.COIN), position.x, position.y, scoreIconSize, scoreIconSize, null);

		// Draw text
		graphics2D.setColor(Color.white);
		graphics2D.drawString(text, position.x + scoreIconSize + scoreGap, position.y + (int)(scoreTextSize / 10f * 9f));
	}

	void drawInventory(Graphics2D graphics2D) {
		int xStart = invHorizontalMargin;
		int yStart = gamePanel.height - invSlotSize - invVerticalMargin;
		hoveringInventoryTile = null;

		List<String> keyList = new ArrayList<String>(buildables.keySet());
		for(int i = 0; i < buildables.size(); i++) {
			int x = xStart + (invSlotGap + invSlotSize) * i;
			String key = keyList.get(i);
			Tile tile = buildables.get(key);
			boolean active = tileManager.currentTile.name == tile.name;

			boolean hovering = false;
			if (UI.hoveringElement == null && Mouse.mousePosition != null && Mouse.mousePosition.x >= x && Mouse.mousePosition.x < x + invSlotSize && Mouse.mousePosition.y >= yStart && Mouse.mousePosition.y < yStart + invSlotSize)
			{
				hoveringInventoryTile = tile;
				hovering = true;
			}

			drawInventorySlot(graphics2D, x, yStart, tile.sprites[0], hovering, active);
		}
	}

	// Should be adapted to new UIElement class
	void drawInventorySlot(Graphics2D graphics2D, int x, int y, BufferedImage icon, boolean hovering, boolean active) {
		UIElement.drawBackground(graphics2D, new Point(x, y), new Dimension(invSlotSize, invSlotSize), hovering ? backgroundColorB : backgroundColorA, cornerRadius);

		int difference = (invSlotSize - invSlotIconSize) / 2;
		int iconX = x + difference;
		int iconY = y + difference;

		if (icon.getWidth() > GamePanel.originalTileSize || icon.getHeight() > GamePanel.originalTileSize)
			icon = icon.getSubimage(0, 0, GamePanel.originalTileSize, GamePanel.originalTileSize);

		graphics2D.drawImage(icon, iconX, iconY, invSlotIconSize, invSlotIconSize, null);

		if (active) {
			UIElement.drawBorder(graphics2D, new Point(x, y), new Dimension(invSlotSize, invSlotSize), 5, UI.backgroundColorB, cornerRadius);
		}
	}

	void drawMenu(Graphics2D graphics2D) {
		int x = gamePanel.width - menuMargin - menuButtonSize;

		BufferedImage[] sprites = new BufferedImage[]{
			iconTextures.get(Icon.PAUSE),
			iconTextures.get(Icon.WAREHOUSE),
			iconTextures.get(Icon.TOOLS)
		};

		hoveringMenuButton = null;

		for (int i = 0; i < sprites.length; i++) {
			BufferedImage sprite = sprites[i];
			int y = menuMargin + (menuButtonSize + menuGap) * i;

			if (Mouse.mousePosition != null && Mouse.mousePosition.x >= x && Mouse.mousePosition.x < x + menuButtonSize && Mouse.mousePosition.y >= y && Mouse.mousePosition.y < y + menuButtonSize)
			{
				hoveringMenuButton = i;
			}

			if (i == 0 && GamePanel.paused)
				sprite = iconTextures.get(Icon.PLAY);

			graphics2D.drawImage(sprite, x, y, menuButtonSize, menuButtonSize, null);
		}
	}

}
