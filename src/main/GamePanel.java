package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import entity.Player;
import object.SuperObject;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable{
	final double BILLION = 1000000000;
	
	// Screen Settings
	final int originalTileSize = 16; // 16x16
	final int scale = 3;
	
	public int tileSize = originalTileSize * scale; // 48
	public int maxScreenCol = 16;
	public int maxScreenRow = 12;
	public int screenWidth = tileSize * maxScreenCol; // 768
	public int screenHeight = tileSize * maxScreenRow; // 576
	
	// World Settings
	public final int maxWorldCol = 50;
	public final int maxWorldRow = 50;
	public final int worldWidth = tileSize * maxScreenCol;
	public final int worldHeight = tileSize * maxScreenRow;
	
	int FPS = 60;
	
	KeyHandler keyH = new KeyHandler();
	TileManager tileM = new TileManager(this);
	Thread gameThread;
	public CollisionChecker cChecker = new CollisionChecker(this);
	public Player player = new Player(this, keyH);
	public AssetSetter aSetter = new AssetSetter(this);
	public SuperObject obj[] = new SuperObject[10];
	
	public GamePanel() {
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH);
		this.setFocusable(true);
	}
	
	public void setupGame() {
		aSetter.setObject();
	}
	
//	public void zoomInOut(int i) {
//		int oldWorldWidth = tileSize * maxWorldCol;
//		tileSize += i;
//		int newWorldWidth = tileSize * maxWorldCol;
//		
//		double multiplier = (double)newWorldWidth / oldWorldWidth;
//		
//		double newPlayerWorldX = player.worldX * multiplier;
//		double newPlayerWorldY = player.worldY * multiplier;
//		
//		player.worldX = newPlayerWorldX;
//		player.worldY = newPlayerWorldY;
//	}
	
	public void startGameThread() {
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	@Override
	public void run() {
		double drawInterval = BILLION / FPS;
		double delta = 0;
		long lastTime = System.nanoTime();
		long curTime;
		long timer = 0;
		int drawCount = 0;
		
		while (gameThread != null) {
			curTime = System.nanoTime();
			delta += (curTime - lastTime) / drawInterval;
			timer += (curTime - lastTime);
			lastTime = curTime;
			
			if (delta >= 1) {
				update();
				repaint();
				delta--;
				drawCount++;
			}
			
			if (timer >= BILLION) {
				// drawCount = frame/second
				drawCount = 0;
				timer = 0;
			}
		}
		
	}
	
	public void update() {
		player.update();
		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g;
		
		tileM.draw(g2);
		
		for (int i = 0; i < obj.length; i++) {
			if (obj[i] != null) {
				obj[i].draw(g2, this);
			}
		}
		
		player.draw(g2);
		
		g2.dispose();
	}
}
