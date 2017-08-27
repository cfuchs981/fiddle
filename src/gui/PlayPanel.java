package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import logic.Sprite;
import logic.Tile;
import logic.World;

//this is the panel that actually contains the game elements, the third and final layer of sandwich 
public class PlayPanel extends JPanel{

 private static final long serialVersionUID = 1L;

 public PlayPanel(){
  
  this.setSize(GameFrame.WIDTH, PLAY_PANEL_HEIGHT);
  //random background color to make different from lower panels 
  this.setBackground(Color.DARK_GRAY);
  this.setLayout(null);
  this.setDoubleBuffered(true);
 }
 
 @Override
 protected void paintComponent(Graphics g) {
  super.paintComponent(g);
  
  Graphics2D g2=(Graphics2D)g;
  
  g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
  g2.drawImage(World.CURRENT_BACKGROUND,0,-Tile.TILE_SIZE,GameFrame.WIDTH,PLAY_PANEL_HEIGHT, null);
  
  for(int i=0; i<World.ROWS; i++){
   for(int j=0; j<World.COLS; j++){
    if(World.tiledMap[i][j]!=null){
     g2.drawImage(World.tiledMap[i][j].getImage(), j*Tile.TILE_SIZE, i*Tile.TILE_SIZE, null);
    }
   }
  }
  
  //draw the protagonist of the game
  if(!sprite.getRestoring()){
   g2.drawImage(sprite.getCurrentFrame(),sprite.getCurrentX(),sprite.getCurrentY(),null);
   g2.draw(sprite.getBoundingBox());
  }
 }
 
 //function called by the GameManager to add the sprite to the play panel at runtime
 public void addSprite(Sprite sprite) {
  this.sprite=sprite;
 }
 
 //distance of the sprite's feet from the bottom border of the window you play the game in
 public static final int TERRAIN_HEIGHT=192;
 public static final int PLAY_PANEL_HEIGHT=640;
 private Sprite sprite;
}
