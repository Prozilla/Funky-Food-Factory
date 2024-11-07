package dev.prozilla.funkyfactory.main;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Viewport {
	
	public static Viewport instance = new Viewport();
	
	public float zoomFactor = 1f;
	final float minZoom = 1f;
	final float maxZoom = 4f;
	final float zoomSpeed = 0.1f;
	final float initialZoom = 0f;
	
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
	
	public static int panSpeed = 5;
	
	public static int panMargin = 500;
	
	GamePanel gamePanel;
	
	public void setGamePanel(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
		setViewportSize();
	}
	
	public void setViewportSize() {
		initWidth = gamePanel.width;
		initHeight = gamePanel.height;
		
		gamePanel.setPreferredSize(new Dimension(initWidth, initHeight));
		
		zoom(initialZoom);
	}
	
	public void zoom(float amount) {
		if (amount > 0) {
			amount *= amount;
		} else {
			amount *= -amount;
		}
		
		zoomedTileScale += amount * zoomFactor * zoomSpeed;
		
		int minTileScale = (int) (minZoom * GamePanel.tileScaleMultiplier);
		int maxTileScale = (int) (maxZoom * GamePanel.tileScaleMultiplier);
		
		if (zoomedTileScale < minTileScale) {
			zoomedTileScale = minTileScale;
		} else if (zoomedTileScale > maxTileScale) {
			zoomedTileScale = maxTileScale;
		}
		
		zoomedItemScale = zoomedTileScale / GamePanel.tileScaleMultiplier * GamePanel.itemScaleMultiplier;
		zoomedTileSize = Math.round(GamePanel.originalTileSize * zoomedTileScale / 2) * 2;
		
		width = gamePanel.horizontalTiles * zoomedTileSize;
		height = gamePanel.verticalTiles * zoomedTileSize;
		
		zoomFactor = (float) width / (float) initWidth;
		System.out.println("Zoom: " + zoomFactor);
		
		centerOffsetX = (initWidth - width) / 2;
		centerOffsetY = (initHeight - height) / 2;
		
		// TO DO: zoom around cursor instead of world center
		centerOffsetX = (initWidth - width) / 2;
		centerOffsetY = (initHeight - height) / 2;
		
		updatePan();
	}
	
	public void pan(Point pointA, Point pointB) {
		panX = oldPanX + pointB.x - pointA.x;
		panY = oldPanY + pointB.y - pointA.y;
		
		// Limit panning to viewport
		if (panX + centerOffsetX > panMargin) {
			panX = -centerOffsetX + panMargin;
			restartPan();
		}
		
		if (panY + centerOffsetY > panMargin) {
			panY = -centerOffsetY + panMargin;
			restartPan();
		}
		
		if (initWidth - width - panX - centerOffsetX > panMargin) {
			panX = initWidth - width - centerOffsetX - panMargin;
			restartPan();
		}
		
		if (initHeight - height - panY - centerOffsetY > panMargin) {
			panY = initHeight - height - centerOffsetY - panMargin;
			restartPan();
		}
	}
	
	void updatePan() {
		pan(new Point(), new Point());
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
		int zoomedWidth = (int) (spriteWidth * zoomFactor);
		int zoomedHeight = (int) (spriteHeight * zoomFactor);
		
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
		int zoomedWidth = (int) (rectWidth * zoomFactor);
		int zoomedHeight = (int) (rectHeight * zoomFactor);
		
		graphics2D.setColor(color);
		graphics2D.fillRect(position.x, position.y, zoomedWidth, zoomedHeight);
	}
	
	public Point positionToViewport(Point position) {
		return new Point((int) ((position.x * zoomFactor) + centerOffsetX + panX), (int) ((position.y * zoomFactor) + centerOffsetY + panY));
	}
	
	public Point viewportToPosition(Point position) {
		return new Point((int) ((position.x - centerOffsetX - panX) / zoomFactor), (int) ((position.y - centerOffsetY - panY) / zoomFactor));
	}
	
}
