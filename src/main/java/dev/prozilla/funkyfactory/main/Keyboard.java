package dev.prozilla.funkyfactory.main;

import dev.prozilla.funkyfactory.tile.TileManager;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

public class Keyboard implements KeyListener {
	
	public static Set<Integer> keysPressed = new HashSet<>();
	
	GamePanel gamePanel;
	TileManager tileManager;
	Viewport viewport;
	
	public Keyboard(GamePanel gamePanel, TileManager tileManager, Viewport viewport) {
		this.gamePanel = gamePanel;
		this.tileManager = tileManager;
		this.viewport = viewport;
	}
	
	public void update() {
		for (Integer key : keysPressed) {
			switch (key) {
				case KeyEvent.VK_LEFT:
					viewport.panX += Viewport.panSpeed;
					break;
				case KeyEvent.VK_RIGHT:
					viewport.panX -= Viewport.panSpeed;
					break;
				case KeyEvent.VK_UP:
					viewport.panY += Viewport.panSpeed;
					break;
				case KeyEvent.VK_DOWN:
					viewport.panY -= Viewport.panSpeed;
					break;
			}
		}
	}
	
	void handlePress(int keyCode) {
		keysPressed.add(keyCode);
	}
	
	void handleRelease(int keyCode) {
		keysPressed.remove(keyCode);
	}
	
	@Override
	public void keyTyped(KeyEvent event) {
	}
	
	@Override
	public void keyPressed(KeyEvent event) {
		handlePress(event.getKeyCode());
	}
	
	@Override
	public void keyReleased(KeyEvent event) {
		handleRelease(event.getKeyCode());
	}
	
}
