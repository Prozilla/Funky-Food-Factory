package source.main;

import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import source.item.ItemManager;
import source.tile.TileManager;

public class GamePanel extends JPanel implements Runnable {

	// Screen settings
	public final static int originalTileSize = 32;
	public static float tileScaleMultiplier = 2;
	public static float itemScaleMultiplier = tileScaleMultiplier / 3 * 2;
	public static int tileSize = (int)(originalTileSize * tileScaleMultiplier);

	public int horizontalTiles = 14;
	public int verticalTiles = 10;

	// Window settings
	public int width = horizontalTiles * tileSize;
	public int height = verticalTiles * tileSize;

	public int fps = 60;
	public double time = 0;
	public double deltaTime = 0;

	// System
	Thread gameThread;
	Viewport viewport = new Viewport(this);
	TileManager tileManager = new TileManager(this, viewport);
	ItemManager itemManager = new ItemManager(this, tileManager, viewport);
	Mouse mouseListener = new Mouse(this, tileManager, viewport);
	UI ui = new UI(this, tileManager);

	// Player
	public int score = 0;

	public GamePanel() {
		this.setBackground(Color.black);
		this.addMouseMotionListener(mouseListener);
		this.setDoubleBuffered(true);
		this.setFocusable(true);

		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent event) {
				if (!SwingUtilities.isMiddleMouseButton(event))
					handleClick(SwingUtilities.isRightMouseButton(event));

				if (SwingUtilities.isMiddleMouseButton(event))
					Mouse.dragStart = event.getPoint();

				if (SwingUtilities.isRightMouseButton(event))
					Mouse.holdingRightMouseButton = true;
			}

			public void mouseReleased(MouseEvent event) {
				Mouse.dragStart = null;
				viewport.stopPan();
				Mouse.holdingRightMouseButton = false;
			}

			public void mouseExited(MouseEvent event) {
				System.out.println("Mouse exit");
				Mouse.mousePosition = null;
				Mouse.mouseCoordinate = null;
				UI.hoveringTile = null;
			}
		});

		addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent event) {
				viewport.zoom(event.getWheelRotation() / -2f);
			}
		});

		tileManager.itemManager = itemManager;
		// tileManager.loadFactory();
	}

	public void handleClick(boolean isRightMouseButton) {
		if (UI.hoveringTile == null) {
			if (!isRightMouseButton) {
				tileManager.placeBuildable(Mouse.viewportMouseCoordinate, tileManager.currentTile.name, 1);
			} else {
				tileManager.removeBuildable(Mouse.viewportMouseCoordinate);
			}
		} else if (!isRightMouseButton) {
			tileManager.currentTile = UI.hoveringTile;
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
