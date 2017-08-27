package intermediary;

import java.awt.event.KeyEvent;
import java.util.HashSet;

import logic.Sprite;
import logic.KeyboardController;
import logic.World;
import gui.GamePanel;

//main thread of the game, repaints all panels and puts keyevents to actual actions 
public class GameManager extends Thread {
 public GameManager(GamePanel gamePanel){
  this.world=new World();
  this.world.initializeStage(currentLevel);
  

  this.sprite=new Sprite();
 
  this.gamePanel=gamePanel;
  this.gamePanel.addSprite(sprite);
  
  this.gameIsRunning=true;
 }
 
 @Override
 public void run() {
  while(gameIsRunning){
   
   if(sprite.outOfBounds()){
     //once there are multiple levels this will matter 
    //world.initializeStage(++currentLevel);
    sprite.reinitialize();
   }
   
   sprite.checkFallingState();
   sprite.checkJumpState();
   
   manageKeys();
   
   sprite.checkBlockCollisions();
   sprite.checkRestoringCount();
   
   gamePanel.repaintGame();
   
   try {
    Thread.sleep(MAIN_SLEEP_TIME);
   } catch (InterruptedException e) {
    e.printStackTrace();
   }
  }
 }
 

 private void manageKeys() {
  HashSet<Integer> currentKeys=KeyboardController.getActiveKeys();
  

  if(currentKeys.contains(KeyEvent.VK_RIGHT)){
   sprite.move(KeyEvent.VK_RIGHT);
  } else if (currentKeys.contains(KeyEvent.VK_LEFT)){
   sprite.move(KeyEvent.VK_LEFT);
  } else if(currentKeys.isEmpty() && !sprite.getJumping() && !sprite.getFalling()){
   sprite.stop();
  }
  
  if(currentKeys.contains(KeyEvent.VK_SPACE)) {
   if(!sprite.getJumping() && !sprite.getFalling()){
    sprite.jump();
   }
  }
  
 }

 public Sprite getSprite(){
  return sprite;
 }
 
 //number of the current level (granted there is only one right now) 
 private int currentLevel=1;
 private boolean gameIsRunning;
 private GamePanel gamePanel;
 private static final int MAIN_SLEEP_TIME=16;
 private Sprite sprite;
 private World world;
}
