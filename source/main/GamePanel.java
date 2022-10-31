package source.main;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.Dimension;

import source.UI.UI;
import source.item.ItemManager;
import source.tile.TileManager;

public class GamePanel extends JPanel implements Runnable {

	// Screen
	public final static int originalTileSize = 32;
	public static float tileScaleMultiplier = 2f;
	public static float itemScaleMultiplier = tileScaleMultiplier * (3 / 4);
	public static int tileSize = (int)(originalTileSize * tileScaleMultiplier);

	public int horizontalTiles = 14;
	public int verticalTiles = 10;

	// Window
	public int width = horizontalTiles * tileSize;
	public int height = verticalTiles * tileSize;

	public int fps = 60;
	public double time = 0;
	public double deltaTime = 0;

	// System
	Thread gameThread;
	Viewport viewport = Viewport.instance;
	UI ui = UI.instance;
	TileManager tileManager = new TileManager(this, viewport);
	ItemManager itemManager = new ItemManager(this, tileManager, viewport);
	Mouse mouseListener = new Mouse(this, tileManager, viewport);

	// Player
	public int score = 0;
	public float scoreMultiplier = 5;

	public GamePanel() {
		UI.instance.setTileManager(tileManager);
		UI.instance.gamePanel = this;
		viewport.setGamePanel(this);

		this.setBackground(UI.backgroundColorC);
		this.addMouseMotionListener(mouseListener);
		this.setDoubleBuffered(true);
		this.setFocusable(true);

		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent event) {
				if (!SwingUtilities.isMiddleMouseButton(event))
					mouseListener.handleClick(SwingUtilities.isRightMouseButton(event));

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
				Mouse.mousePosition = null;
				Mouse.mouseCoordinate = null;
				UI.hoveringInventoryTile = null;
			}
		});

		addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent event) {
				viewport.zoom(event.getWheelRotation() / -2f);
			}
		});

		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent componentEvent) {
				Dimension newSize = componentEvent.getComponent().getBounds().getSize();
				width = (int)newSize.getWidth();
				height = (int)newSize.getHeight();
			}
		});

		tileManager.itemManager = itemManager;
		tileManager.loadFactory();
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

	public void addScore(int value) {
		score += Math.round(value * scoreMultiplier);
	}

}
