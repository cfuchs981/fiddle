package gui;

import java.awt.Color;

import javax.swing.JPanel;

import logic.Sprite;
import logic.KeyboardController;

//the background layer of the game that everything else is drawn onto (second layer) 
//Basically is just a layer separating the physical frame and terrain components 

public class GamePanel extends JPanel{
 
 private static final long serialVersionUID = 1L;
 
 
 public GamePanel(){
  
  this.setRequestFocusEnabled(true);
  this.setSize(WIDTH, HEIGHT);
  this.setLayout(null);
  this.setBackground(Color.BLACK);

  this.add(statsPanel);
  //status bar currently displays at top of screen 
  statsPanel.setLocation(0, 0);

  this.add(playPanel);
  //places actual game graphics in remaining pane before status panel cut-off 
  playPanel.setLocation(0, StatsPanel.STATS_HEIGHT);
  
  //initialize keyboard controller 
  keyboardController=new KeyboardController();
  this.addKeyListener(keyboardController);
 }
 
 public void addSprite(Sprite sprite) {
  this.sprite=sprite;
  playPanel.addSprite(sprite);
  statsPanel.addSprite(sprite);
 }
 
 public void repaintGame(){
  playPanel.repaint();
  statsPanel.repaint();
 }
 
 private KeyboardController keyboardController;
 private StatsPanel statsPanel=new StatsPanel();
 private PlayPanel playPanel=new PlayPanel();
 @SuppressWarnings("unused")
 private Sprite sprite;
}
