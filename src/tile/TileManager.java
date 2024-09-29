package tile;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import main.GamePanel;

public class TileManager {
	GamePanel gp;
	public Tile[] tile;
	public int mapTileNum[][];
	
	int numTile = 6;
	String imageName[] = {"grass", "wall", "water", "earth", "tree", "sand"};
	boolean collisionArr[] = {false, true, true, false, true, false};
	
	public TileManager(GamePanel gp) {
		this.gp = gp;
		
		tile = new Tile[10];
		mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];
	
		getTileImage();
		loadMap("/maps/world01.txt");
	}
	
	public void getTileImage() {
		try {
			
			for (int i = 0; i < numTile; i++) {
				tile[i] = new Tile();
				
				String strPath = String.format("/tiles/%s.png", imageName[i]);
				tile[i].image  = ImageIO.read(getClass().getResourceAsStream(strPath));
				tile[i].collision = collisionArr[i];
			}
			
//			tile[0].image  = ImageIO.read(getClass().getResourceAsStream("/tiles/grass.png"));
//			tile[1].image  = ImageIO.read(getClass().getResourceAsStream("/tiles/wall.png"));
//			tile[2].image  = ImageIO.read(getClass().getResourceAsStream("/tiles/water.png"));
//			tile[3].image  = ImageIO.read(getClass().getResourceAsStream("/tiles/earth.png"));
//			tile[4].image  = ImageIO.read(getClass().getResourceAsStream("/tiles/tree.png"));
//			tile[5].image  = ImageIO.read(getClass().getResourceAsStream("/tiles/sand.png"));
//			
//			tile[1].collision = true;
//			tile[2].collision = true;
//			tile[4].collision = true;
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loadMap(String filePath) {
		try {
			InputStream is = getClass().getResourceAsStream(filePath);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			
			int col = 0;
			int row = 0;
			while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
				String line = br.readLine();
				
				while (col < gp.maxWorldCol) {
					String numbers[] = line.split(" ");
					
					int num = Integer.parseInt(numbers[col]);
				
					mapTileNum[col][row] = num;
					col++;
				}
				col = 0;
				row++;
			}
			br.close();
		} catch (Exception e) {
			
		}
	}
	
	public void draw(Graphics2D g2) {
		int worldCol = 0;
		int worldRow = 0;
		
		while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {
			int tileNum = mapTileNum[worldCol][worldRow];
			
			int worldX = worldCol * gp.tileSize;
			int worldY = worldRow * gp.tileSize;
			int screenX = worldX - gp.player.worldX + gp.player.screenX;
			int screenY = worldY - gp.player.worldY + gp.player.screenY;
			
			if (worldX + gp.player.screenX + gp.tileSize > gp.player.worldX && 
				worldX - gp.player.screenX - gp.tileSize< gp.player.worldX && 
				worldY + gp.player.screenY + gp.tileSize > gp.player.worldY && 
				worldY - gp.player.screenY - gp.tileSize< gp.player.worldY) {
				g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);				
			}
			worldCol++;
			
			if (worldCol >= gp.maxWorldCol) {
				worldCol = 0;
				worldRow++;
			}
			
		}

	}
}
