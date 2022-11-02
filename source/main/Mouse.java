package source.main;

import java.awt.event.MouseMotionListener;
import javax.swing.SwingUtilities;
import java.awt.Point;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

import source.UI.UI;
import source.UI.UIElement;
import source.buildable.Buildable;
import source.buildable.Building;
import source.tile.TileManager;

public class Mouse implements MouseMotionListener, MouseListener {

	public static Point mousePosition;
	public static Point mouseCoordinate;

	public static Point viewportMousePosition;
	public static Point viewportMouseCoordinate;

	Point lastDragCoordinate;
	public static Point dragStart;

	public static boolean holdingRightMouseButton = false;

	GamePanel gamePanel;
	TileManager tileManager;
	Viewport viewport;

	public Mouse(GamePanel gamePanel, TileManager tileManager, Viewport viewport) {
		this.gamePanel = gamePanel;
		this.tileManager = tileManager;
		this.viewport = viewport;
	}

	public void handleClick(boolean isRightMouseButton) {
		boolean openedModal = false;

		if (UI.hoveringElement == null) {
			if (UI.currentModal == null) {
				if (UI.hoveringInventoryTile == null) {
					if (!isRightMouseButton) {
						Buildable buildable = tileManager.coordinateToBuildable.get(Mouse.viewportMouseCoordinate);

						if (buildable == null) {
							tileManager.placeBuildable(Mouse.viewportMouseCoordinate, tileManager.currentTile.name, 1);
						} else if (buildable instanceof Building) {
							Building building = (Building)buildable;
							building.openModal();
							openedModal = true;
						}
					} else {
						tileManager.removeBuildable(Mouse.viewportMouseCoordinate);
					}
				} else if (!isRightMouseButton) {
					tileManager.currentTile = UI.hoveringInventoryTile;
				}
			} else if (!openedModal) {
				UI.currentModal = null;
			}
		} else {
			UIElement element = UI.hoveringElement;

			while (element != null) {
				if (element.clickable != null) {
					element.clickable.onClick(element);
					break;
				} else {
					element = element.parent;
				}
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent event) {
		updateCursor(event.getPoint());

		if (UI.hoveringInventoryTile == null) {
			if (!SwingUtilities.isMiddleMouseButton(event) && (lastDragCoordinate == null || lastDragCoordinate != mouseCoordinate)) {
				lastDragCoordinate = mouseCoordinate;

				handleClick(SwingUtilities.isRightMouseButton(event));

				if (SwingUtilities.isRightMouseButton(event))
					holdingRightMouseButton = true;
			}
		}

		if (dragStart != null) {
			viewport.pan(dragStart, mousePosition);
		}
	}

	@Override
    public void mouseReleased(MouseEvent event) {
		lastDragCoordinate = null;
    }

	@Override
	public void mouseMoved(MouseEvent event) {
		updateCursor(event.getPoint());
	}

	public static boolean hoveringTile(int x, int y) {
		Point point = new Point(x / GamePanel.tileSize, y / GamePanel.tileSize);
		return mouseCoordinate.equals(point);
	}

	public void updateCursor(Point point) {
		mousePosition = point;
		mouseCoordinate = TileManager.positionToCoordinate(point);

		viewportMousePosition = viewport.viewportToPosition(point);
		viewportMouseCoordinate = TileManager.positionToCoordinate(viewportMousePosition);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent event) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {

	}
	
}
