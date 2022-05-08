package source.main;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Graphics;
import java.awt.Graphics2D;

import source.tile.TileManager;

public class GamePanel extends JPanel implements Runnable {

	// Screen settings
	public final static int originalTileSize = 16;
	public final static int pixelScale = 6;
	public final static int tileSize = originalTileSize * pixelScale;

	public final int horizontalTiles = 16;
	public final int verticalTiles = 10;

	// Window settings
	final int width = horizontalTiles * tileSize;
	final int height = verticalTiles * tileSize;

	public int fps = 60;
	public double time = 0;
	public double deltaTime = 0;

	Thread gameThread;
	TileManager tileManager = new TileManager(this);
	MouseListener mouseListener = new MouseListener(this, tileManager);

	public GamePanel() {
		this.setPreferredSize(new Dimension(width, height));
		this.setBackground(Color.black);
		this.addMouseMotionListener(mouseListener);
		this.setDoubleBuffered(true);
		this.setFocusable(true);

		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent event) {
				if (!SwingUtilities.isRightMouseButton(event)) {
					tileManager.placeBuildable(MouseListener.mouseCoordinate, 0);
				} else {
					tileManager.removeBuildable(MouseListener.mouseCoordinate);
				}
			}
		});
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

		tileManager.draw(graphics2D);

		graphics2D.dispose();
	}

}
