package gui;

import java.awt.Toolkit;
import javax.swing.JFrame;

//the main game frame of the game, the first layer 
public class GameFrame extends JFrame {

  private static final long serialVersionUID = 1L;

 public GameFrame(GamePanel gamePanel){
  
  //set frame to appear at the center of the screen
  this.setLocation((int)((Toolkit.getDefaultToolkit().getScreenSize().getWidth()-WIDTH)/2),
    ((int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight()-HEIGHT)/2));  

  this.setSize(WIDTH,HEIGHT);
  this.setTitle("fiddle");
  this.setVisible(true);
  this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  
  //made frame impossible to resize bc of convenience
  this.setResizable(false);
  
  //add second layer of sandwich 
  this.add(gamePanel);
  
  //then slide focus onto that 
  gamePanel.grabFocus();
  gamePanel.requestFocusInWindow();
 }
 
 public static final int WIDTH=1280;
 public static final int HEIGHT=640;
}
