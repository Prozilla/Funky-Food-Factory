package source.main;

import java.awt.image.BufferedImage;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Color;
import java.awt.Dimension;

public class Viewport {

	public float zoomFactor = 1;
	final float minZoom = zoomFactor;
	final float maxZoom = 4;
	final float zoomSpeed = 1.25f;
	
	public static float zoomedTileScale;
	public static float zoomedItemScale;
	public static int zoomedTileSize;

	public int initWidth;
	public int initHeight;

	public int width;
	public int height;

	public int centerOffsetX = 0;
	public int centerOffsetY = 0;

	public int oldPanX = 0;
	public int oldPanY = 0;
	public int panX = 0;
	public int panY = 0;

	GamePanel gamePanel;

	public Viewport(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
		setViewportSize();
	}

	public void setViewportSize() {
		initWidth = gamePanel.width;
		initHeight = gamePanel.height;

		gamePanel.setPreferredSize(new Dimension(initWidth, initHeight));

		zoom(0);
	}

	public void zoom(float amount) {
		zoomedTileScale += Math.round(amount * zoomFactor * zoomSpeed);

		int minTileScale = (int)(minZoom * GamePanel.tileScaleMultiplier);
		int maxTileScale = (int)(maxZoom * GamePanel.tileScaleMultiplier);

		zoomedTileScale = zoomedTileScale < minTileScale ? minTileScale : zoomedTileScale > maxTileScale ? maxTileScale : zoomedTileScale;
		zoomedItemScale = zoomedTileScale / GamePanel.tileScaleMultiplier * GamePanel.itemScaleMultiplier;

		zoomedTileSize = (int)(GamePanel.originalTileSize * zoomedTileScale);

		width = gamePanel.horizontalTiles * zoomedTileSize;
		height = gamePanel.verticalTiles * zoomedTileSize;

		centerOffsetX = (initWidth - width) / 2;
		centerOffsetY = (initHeight - height) / 2;

		// TO DO: zoom around cursor instead of world center
		// centerOffsetX = (initWidth - width) / 2 - panX;
		// centerOffsetY = (initHeight - height) / 2 - panY;

		// if (Mouse.mousePosition != null) {
		// 	centerOffsetX -= Mouse.mousePosition.x + (initWidth - width) / 2;
		// 	centerOffsetY -= Mouse.mousePosition.y + (initHeight - width) / 2;
		// }

		zoomFactor = (float)width / (float)initWidth;
		System.out.println("Zoom: " + zoomFactor);

		pan(new Point(), new Point());
	}

	public void pan(Point pointA, Point pointB) {
		panX = oldPanX + pointB.x - pointA.x;
		panY = oldPanY + pointB.y - pointA.y;

		// Limit panning to viewport
		if (panX + centerOffsetX > 0) {
			panX = -centerOffsetX;
			restartPan();
		}

		if (panY + centerOffsetY > 0) {
			panY = -centerOffsetY;
			restartPan();
		}

		if (initWidth - width - panX - centerOffsetX > 0) {
			panX = initWidth - width - centerOffsetX;
			restartPan();
		}

		if (initHeight - height - panY - centerOffsetY > 0) {
			panY = initHeight - height - centerOffsetY;
			restartPan();
		}
	}

	public void stopPan() {
		oldPanX = panX;
		oldPanY = panY;
	}

	public void restartPan() {
		stopPan();
		Mouse.dragStart = Mouse.mousePosition;
	}

	public void drawSprite(Graphics2D graphics2D, BufferedImage sprite, int x, int y, int spriteWidth, int spriteHeight, float alpha, boolean isItem) {
		if (alpha <= 0)
			return;

		Point position = positionToViewport(new Point(x, y)); 
		int zoomedWidth = (int)(spriteWidth * zoomFactor);
		int zoomedHeight = (int)(spriteHeight * zoomFactor);

		// if (zoomedX < 0 || zoomedY < 0 || zoomedX > width || zoomedY > height)
		// 	return;

		if (alpha < 1)
			graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

		// System.out.println(spriteWidth + " * " + zoomMultiplier + " = " + zoomedWidth);

		graphics2D.drawImage(sprite, position.x, position.y, zoomedWidth, zoomedHeight, null);

		if (alpha < 1)
			graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
	}

	public void drawRect(Graphics2D graphics2D, int x, int y, int rectWidth, int rectHeight, Color color) {
		Point position = viewportToPosition(new Point(x, y)); 
		int zoomedWidth = (int)(rectWidth * zoomFactor);
		int zoomedHeight = (int)(rectHeight * zoomFactor);

		graphics2D.setColor(color);
		graphics2D.fillRect(position.x, position.y, zoomedWidth, zoomedHeight);
	}

	public Point positionToViewport(Point position) {
		return new Point((int)((position.x * zoomFactor) + centerOffsetX + panX), (int)((position.y * zoomFactor) + centerOffsetY + panY));
	}

	public Point viewportToPosition(Point position) {
		return new Point((int)((position.x - centerOffsetX - panX) / zoomFactor), (int)((position.y - centerOffsetY - panY) / zoomFactor));
	}

}
