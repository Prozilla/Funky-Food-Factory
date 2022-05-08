package source.main;

import java.awt.event.MouseMotionListener;
import java.awt.Point;
import java.awt.event.MouseEvent;

import source.tile.TileManager;

public class MouseListener implements MouseMotionListener {

	GamePanel gamePanel;
	TileManager tileManager;

	public static Point mousePosition = new Point(0, 0);
	public static Point mouseCoordinate = new Point(0, 0);

	public MouseListener(GamePanel gamePanel, TileManager tileManager) {
		this.gamePanel = gamePanel;
		this.tileManager = tileManager;
	}

	@Override
	public void mouseDragged(MouseEvent event) {

	}

	@Override
	public void mouseMoved(MouseEvent event) {
		Point point = event.getPoint();
		Point coordinate = TileManager.positionToCoordinate(point);

		if (mouseCoordinate.equals(coordinate))
			return;

		mousePosition = point;
		mouseCoordinate = coordinate;

		updateCursor();
	}

	public static boolean hoveringTile(int x, int y) {
		Point point = new Point(x / GamePanel.tileSize, y / GamePanel.tileSize);
		return mouseCoordinate.equals(point);
	}

	public void updateCursor() {
		// if (hovering) {
		// 	Main.frame.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		// } else {
		// 	Main.frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		// }
	}
	
}
