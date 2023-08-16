package main;

import java.awt.Graphics;
import gamestates.Gamestate;
import gamestates.Menu;
import gamestates.Playing;

public class Game implements Runnable {

	private GameWindow gameWindow;
	private GamePanel gamePanel;
	private Thread gameThread;
	private final int FPS_SET = 120;
	private final int UPS_SET = 200;

	private Playing playing;
	private Menu menu;

	public final static int TILES_DEFAULT_SIZE = 32;
	public final static float SCALE = 1f;
	public final static int TILES_IN_WIDTH = 26;
	public final static int TILES_IN_HEIGHT = 14;
	public final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
	public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
	public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;

	public Game() {
		initClasses();

		gamePanel = new GamePanel(this);
		gameWindow = new GameWindow(gamePanel);
		gamePanel.requestFocus();

		startGameLoop();

	}

	private void initClasses() {
		menu = new Menu(this);
		playing = new Playing(this);
	}

	private void startGameLoop() {
		gameThread = new Thread(this);
		gameThread.start();
	}

	public void update() {
		switch (Gamestate.state) {
		case MENU:
			menu.update();
			break;
		case PLAYING:
			playing.update();
			break;
		default:
			break;

		}
	}

	public void render(Graphics g) {
		switch (Gamestate.state) {
		case MENU:
			menu.draw(g);
			break;
		case PLAYING:
			playing.draw(g);
			break;
		default:
			break;
		}
	}

	@Override
	public void run() {

		// FPS_SET và UPS_SET là hằng số chứa số khung hình mục tiêu và số lần cập nhật
		// mục tiêu mỗi giây.
		// timePerFrame và timePerUpdate tính toán thời gian giữa mỗi khung hình và mỗi
		// lần cập nhật dựa trên số FPS và UPS mục tiêu.
		double timePerFrame = 1000000000.0 / FPS_SET;
		double timePerUpdate = 1000000000.0 / UPS_SET;

		long previousTime = System.nanoTime();

		int frames = 0;
		int updates = 0;
		long lastCheck = System.currentTimeMillis();

		double deltaU = 0;
		double deltaF = 0;
		// một vòng lặp điều khiển để quản lý thời gian và tốc
		// độ cập nhật (UPS - Updates Per Second) cùng với tốc độ khung hình (FPS -
		// Frames Per Second) trong một trò chơi
		// p

		while (true) {
// Tính toán thời gian delta:
//Tính toán thời gian đã trôi qua kể từ lần lặp trước đó thông qua currentTime - previousTime.
//deltaU và deltaF tích luỹ thời gian đã trôi qua kể từ lần cập nhật và lần vẽ khung hình trước đó.
			long currentTime = System.nanoTime();

			deltaU += (currentTime - previousTime) / timePerUpdate;
			deltaF += (currentTime - previousTime) / timePerFrame;
			previousTime = currentTime;
			// deltaU và deltaF cộng dồn thời gian đã trôi qua kể từ lần cập nhật và lần vẽ
			// khung hình trước đó.
			// Nếu deltaU đạt hoặc vượt quá 1 (nghĩa là đã đủ thời gian để thực hiện một lần
			// cập nhật), thì update() được gọi để cập nhật trạng thái của trò chơi, sau đó
			// deltaU giảm đi 1.
			// Tương tự, nếu deltaF đạt hoặc vượt quá 1 (nghĩa là đã đủ thời gian để vẽ một
			// khung hình), thì gamePanel.repaint() được gọi để vẽ lại khung hình của trò
			// chơi, sau đó deltaF giảm đi 1.
			if (deltaU >= 1) {
				update();
				updates++;
				deltaU--;
			}

			if (deltaF >= 1) {
				gamePanel.repaint();
				frames++;
				deltaF--;
			}

			if (System.currentTimeMillis() - lastCheck >= 1000) {
				lastCheck = System.currentTimeMillis();
				System.out.println("FPS: " + frames + " | UPS: " + updates);
				frames = 0;
				updates = 0;

			}
		}

	}

	public void windowFocusLost() {
		if (Gamestate.state == Gamestate.PLAYING)
			playing.getPlayer().resetDirBooleans();
	}

	public Menu getMenu() {
		return menu;
	}

	public Playing getPlaying() {
		return playing;
	}
}