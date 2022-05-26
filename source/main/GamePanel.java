package source.main;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Graphics;
import java.awt.Graphics2D;

import source.item.ItemManager;
import source.tile.TileManager;

public class GamePanel extends JPanel implements Runnable {

	// Screen settings
	public final static int originalTileSize = 32;
	public final static int tileScale = 3;
	public final static int itemScale = 2;
	public final static int tileSize = originalTileSize * tileScale;

	public final int horizontalTiles = 16;
	public final int verticalTiles = 10;

	// Window settings
	final int width = horizontalTiles * tileSize;
	final int height = verticalTiles * tileSize;

	public int fps = 60;
	public double time = 0;
	public double deltaTime = 0;

	// System
	Thread gameThread;
	TileManager tileManager = new TileManager(this);
	ItemManager itemManager = new ItemManager(this, tileManager);
	Mouse mouseListener = new Mouse(this, tileManager);
	UI ui = new UI(this, tileManager);

	// Player
	public int score = 0;

	public GamePanel() {
		this.setPreferredSize(new Dimension(width, height));
		this.setBackground(Color.black);
		this.addMouseMotionListener(mouseListener);
		this.setDoubleBuffered(true);
		this.setFocusable(true);

		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent event) {
				handleClick(SwingUtilities.isRightMouseButton(event));
			}
		});

		tileManager.itemManager = itemManager;
		tileManager.loadFactory();
	}

	public void handleClick(boolean isRightMouseButton) {
		if (UI.hoveringTile == null) {
			if (!isRightMouseButton) {
				tileManager.placeBuildable(Mouse.mouseCoordinate, tileManager.currentTile.name, 1);
			} else {
				tileManager.removeBuildable(Mouse.mouseCoordinate);
			}
		} else if (!isRightMouseButton) {
			tileManager.currentTile = UI.hoveringTile;
			System.out.println(UI.hoveringTile.name);
		}
	}

	public void startGameThread() {
		gameThread = new Thread(this);
		gameThread.start();
	}

	@Override
	public void run() {
		double drawInterval = 1000000000 / fps;
		double delta = 0;

		double lastTime = System.nanoTime();
		long currentTime;

		long timer = 0;

		while (gameThread != null) {
			currentTime = System.nanoTime();

			delta += (currentTime - lastTime) / drawInterval;
			timer += (currentTime - lastTime);
			lastTime = currentTime;

			if (delta >= 1) {
				deltaTime = delta;
				time += deltaTime / fps;

				update();
				repaint();

				delta--;
			}

			if (timer >= 1000000000) {
				timer = 0;
			}
		}
	}

	public void update() {
		// player.update();
	}

	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);

		Graphics2D graphics2D = (Graphics2D)graphics;

		// Draw tiles
		tileManager.draw(graphics2D);

		// Draw UI
		ui.draw(graphics2D);

		graphics2D.dispose();
	}

}
