package source.main;

import java.awt.event.MouseMotionListener;
import javax.swing.SwingUtilities;
import java.awt.Point;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

import source.tile.TileManager;

public class Mouse implements MouseMotionListener, MouseListener {

	GamePanel gamePanel;
	TileManager tileManager;

	public static Point mousePosition = new Point(0, 0);
	public static Point mouseCoordinate = new Point(0, 0);
	Point lastDragCoordinate;

	public Mouse(GamePanel gamePanel, TileManager tileManager) {
		this.gamePanel = gamePanel;
		this.tileManager = tileManager;
	}

	@Override
	public void mouseDragged(MouseEvent event) {
		updateCursor(event.getPoint());

		if (lastDragCoordinate == null || lastDragCoordinate != mouseCoordinate) {
			lastDragCoordinate = mouseCoordinate;

			if (!SwingUtilities.isRightMouseButton(event)) {
				tileManager.placeBuildable(Mouse.mouseCoordinate, 0, 0);
			} else {
				tileManager.removeBuildable(Mouse.mouseCoordinate);
			}
		}
	}

	@Override
    public void mouseReleased(MouseEvent event) {
		lastDragCoordinate = null;
    }

	@Override
	public void mouseMoved(MouseEvent event) {
		Point point = event.getPoint();
		updateCursor(point);
	}

	public static boolean hoveringTile(int x, int y) {
		Point point = new Point(x / GamePanel.tileSize, y / GamePanel.tileSize);
		return mouseCoordinate.equals(point);
	}

	public void updateCursor(Point point) {
		Point coordinate = TileManager.positionToCoordinate(point);

		if (mouseCoordinate.equals(coordinate))
			return;

		mousePosition = point;
		mouseCoordinate = coordinate;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}
	
}
