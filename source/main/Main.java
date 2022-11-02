package source.main;

import javax.swing.JFrame;

public class Main {
	
	public static JFrame frame;

	public static void main(String[] args) {
		frame = new JFrame();
		frame.setTitle(GamePanel.gameName);

		GamePanel gamePanel = new GamePanel();
		frame.add(gamePanel);
		frame.pack();

		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		gamePanel.startGameThread();
	}
	
}
