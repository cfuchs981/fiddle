package logic;

import gui.GameFrame;
import gui.GamePanel;
import gui.PlayPanel;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;


//this is the sprite you control, maybe narcissistically made in my image but oh well 
public class Sprite {

 public Sprite(){
  //initialize the buffers that will store the run sprites
  run_L=new BufferedImage[BUFFER_RUN_SIZE];
  run_R=new BufferedImage[BUFFER_RUN_SIZE];
  
  //load all the images
  loadInformations();
  
  //initilize sprite standing there 
  currentFrame=idle_R;
  boundingBox=new Rectangle(BOY_START_X+DISPLACEMENT,currentY,BOY_WIDTH,BOY_HEIGHT);
 }
 
 
 //loads all the sprites needed to animate the character 
 private void loadInformations() {
  try {
   idle_R=ImageIO.read(getClass().getResource("../images/idle_R.png"));
   idle_L=ImageIO.read(getClass().getResource("../images/idle_L.png"));
   
   run_R[0]=ImageIO.read(getClass().getResource("../images/run_R0.png"));
   run_L[0]=ImageIO.read(getClass().getResource("../images/run_L0.png"));
   
   run_R[1]=ImageIO.read(getClass().getResource("../images/run_R1.png"));
   run_L[1]=ImageIO.read(getClass().getResource("../images/run_L1.png"));
   
   run_R[2]=ImageIO.read(getClass().getResource("../images/run_R2.png"));
   run_L[2]=ImageIO.read(getClass().getResource("../images/run_L2.png"));
   
   run_R[3]=ImageIO.read(getClass().getResource("../images/run_R3.png"));
   run_L[3]=ImageIO.read(getClass().getResource("../images/run_L3.png"));
   
   run_R[4]=ImageIO.read(getClass().getResource("../images/run_R4.png"));
   run_L[4]=ImageIO.read(getClass().getResource("../images/run_L4.png"));
   
   run_R[5]=ImageIO.read(getClass().getResource("../images/run_R5.png"));
   run_L[5]=ImageIO.read(getClass().getResource("../images/run_L5.png"));
   
  } catch (IOException e) {
   e.printStackTrace();
  }
 }
 
 //function called by the GameManager's manageKeys() function
 public void move(int direction) {
  this.idle=false;
  switch (direction) {

   case KeyEvent.VK_LEFT:
    currentX=currentX-DISPLACEMENT;
    //if you can't go back
    if(currentX<=0){
     currentX=0;
    }
    //update the character's bounding box position
    boundingBox.setLocation(currentX, currentY);
    
    //change the current frame in animation
    if(!jumping && !falling){
     setFrameNumber();
     currentFrame=run_L[currentFrameNumber];
    } else {
     currentFrame=run_L[0];
    }
    
    //set the left direction as the last one 
    last_direction=KeyEvent.VK_LEFT;
    break;
   

   case KeyEvent.VK_RIGHT:
    currentX=currentX+DISPLACEMENT;
    
    //update the character's bounding box position
    boundingBox.setLocation(currentX, currentY);
    //change the current frame in animation
    if(!jumping && !falling){
     setFrameNumber();
     currentFrame=run_R[currentFrameNumber];
    } else {
     currentFrame=run_R[0];
    }
    
    //set the right direction as the last one 
    last_direction=KeyEvent.VK_RIGHT;
    break;
    
   default:
    break;
  }
  currentRow=currentY/Tile.TILE_SIZE;
  currentCol=currentX/Tile.TILE_SIZE;
  
  moveCounter++;
 }
 
 public void checkRestoringCount() {
  if(restoring_count>0){
   restoring_count--;
   if(restoring_count%RESTORING_MODULE==0){
    restoring=!restoring;
   }
  } 
 }
 
 //checks and handles possible collisions with static blocks (Block class)
 public void checkBlockCollisions(){
  
  //position of the character's feet on the y-axis
  int footY=(int)(boundingBox.getMaxY());
  
  //if the character is jumping and touches a block, best start falling 
  if(jumping){
   
   //row position of the cell above the character's head (in the tiled map)
   int upRow=(int)((boundingBox.getMinY()-1)/Tile.TILE_SIZE);
   
   //tile position relative to the upper-left corner of the character's bounding box
   int upLeftCornerCol=(int)(boundingBox.getMinX()/Tile.TILE_SIZE);
   
   //tile position relative to the upper-right corner of the character's bounding box
   int upRightCornerCol=(int)((boundingBox.getMaxX())/Tile.TILE_SIZE);

   if(currentRow>=0){
    if(World.tiledMap[upRow][upLeftCornerCol] instanceof Block){
     //if the upper-left corner stats intersecting a block, stop the jumping phase
     //and start the falling phase, setting the jump_count to 0
     if(World.tiledMap[upRow][upLeftCornerCol].getBoundingBox().intersects(boundingBox)){
      jumping=false;
      jump_count=0;
      falling=true;
      return;
     }
    }
    if(World.tiledMap[upRow][upRightCornerCol] != null){
     //if the upper-right corner stats intersecting a block, stop the jumping phase
     //and start the falling phase, setting the jump_count to 0
     if(World.tiledMap[upRow][upRightCornerCol].getBoundingBox().intersects(boundingBox)){
      jumping=false;
      jump_count=0;
      falling=true;
      return;
     }
    }
   }
  
  }
  
  //if last direction was right..
  if(last_direction==KeyEvent.VK_RIGHT){
   
   //get the left side of the bounding box
   int footX=(int)boundingBox.getMinX();
   
   //get the tile position (in the tiled map) 
   //relative to the tile in front of the character
   int tileInFrontOfFootRow=((footY-1)/Tile.TILE_SIZE);
   int tileInFrontOfFootCol=(footX/Tile.TILE_SIZE)+1;
   
   if(tileInFrontOfFootCol<World.COLS){
    //if the tile in front of the character contains a block..
    if(World.tiledMap[tileInFrontOfFootRow][tileInFrontOfFootCol] instanceof Block){
     //..and the character's bounding box intersect the block's one
     if(boundingBox.intersects(World.tiledMap[tileInFrontOfFootRow][tileInFrontOfFootCol].getBoundingBox())){
      //push the character away and re-set its position
      currentX-=DISPLACEMENT;
      boundingBox.setLocation(currentX, currentY);
      currentCol=currentX/Tile.TILE_SIZE;
     }
    }
    
    if(World.tiledMap[currentRow][currentCol] instanceof Block){
     //if the tile the character finds himself in contains a block, act like above
     if(boundingBox.intersects(World.tiledMap[currentRow][currentCol].getBoundingBox())){
      currentX-=DISPLACEMENT;
      boundingBox.setLocation(currentX, currentY);
      currentCol=currentX/Tile.TILE_SIZE;
     }
    }
   }
  } else {
   //get the right side of the bounding box
   int footX=(int) boundingBox.getMaxX();
   
   //get the tile position (in the tiled map) 
   //relative to the tile in front of the character
   int tileInFrontOfFootRow=((footY-1)/Tile.TILE_SIZE);
   int tileInFrontOfFootCol=(footX/Tile.TILE_SIZE)-1;
   
   if(tileInFrontOfFootCol>=0){
    //if the tile in front of the character contains a block..
    if(World.tiledMap[tileInFrontOfFootRow][tileInFrontOfFootCol] instanceof Block){
     //..and the character's bounding box intersect the block's one
     if(boundingBox.intersects(World.tiledMap[tileInFrontOfFootRow][tileInFrontOfFootCol].getBoundingBox())){
      //push the character away and re-set its position
      currentX+=DISPLACEMENT;
      boundingBox.setLocation(currentX, currentY);
      currentCol=currentX/Tile.TILE_SIZE;
     }
    }
    
    if(World.tiledMap[currentRow][currentCol] instanceof Block){
     //if the tile the character finds himself in contains a block, act like above
     if(boundingBox.intersects(World.tiledMap[currentRow][currentCol].getBoundingBox())){
      currentX+=DISPLACEMENT;
      boundingBox.setLocation(currentX, currentY);
      currentCol=currentX/Tile.TILE_SIZE;
     }
    }
   } 
  }
 }


 public void checkFallingState(){
  if(boundingBox.getMaxY()/Tile.TILE_SIZE>=World.ROWS){
   die();
  }
  
  
  if(jumping){
   return;
  }
  
  if(falling){
   currentY+=DISPLACEMENT;
   currentRow=currentY/Tile.TILE_SIZE;
   boundingBox.setLocation(currentX, currentY);
  }
  
  int lowLeftX=(int)boundingBox.getMinX()+1;
  int lowRightX=(int) boundingBox.getMaxX()-1;
  
  int underlyingTileXR=lowRightX/Tile.TILE_SIZE;
  int underlyingTileXL=lowLeftX/Tile.TILE_SIZE;
  
  if(currentRow+1>=World.ROWS || underlyingTileXR>=World.COLS){
   return;
  }
  
  if(!((World.tiledMap[currentRow+1][underlyingTileXR]) instanceof Block)
   && !((World.tiledMap[currentRow+1][underlyingTileXL]) instanceof Block)){
   falling=true;
   return;
  }
  
  falling=false;
 }
 
 private void die() {
  currentX=BOY_START_X;
  currentY=GameFrame.HEIGHT-PlayPanel.TERRAIN_HEIGHT-BOY_HEIGHT;
  currentCol=currentX/Tile.TILE_SIZE;
  currentRow=currentY/Tile.TILE_SIZE;
  boundingBox=new Rectangle(BOY_START_X+DISPLACEMENT,currentY,BOY_WIDTH,BOY_HEIGHT);
  last_direction=KeyEvent.VK_RIGHT;
  falling=false;
  restoring=true;
  restoring_count=RESTORING_THRESH;
  life--;
 }

 public void reinitialize() {
  currentX=0;
  currentY=GameFrame.HEIGHT-PlayPanel.TERRAIN_HEIGHT-BOY_HEIGHT;
  currentCol=0;
  currentRow=currentY/Tile.TILE_SIZE;
  boundingBox=new Rectangle(BOY_START_X+DISPLACEMENT,currentY,BOY_WIDTH,BOY_HEIGHT);
  last_direction=KeyEvent.VK_RIGHT;
  falling=false;
 }
 
 //checks the jumping variables and animates jumps 

 public void checkJumpState() {
  if(jumping){
   if(jump_count<JUMP_COUNTER_THRESH){
    currentY-=DISPLACEMENT;
    boundingBox.setLocation(currentX, currentY);
   } 
   
   jump_count++;
   
   if(jump_count>=JUMP_COUNTER_THRESH){
    jumping=false;
    jump_count=0;
    falling=true;
   }
  }
 }
 
 //sets the current frame of movement, each direction has 6 frames
 private void setFrameNumber() {

  currentFrameNumber  = moveCounter/MOVE_COUNTER_THRESH;
  currentFrameNumber %= 6;
  
  //reset move counter if necessary
  if(moveCounter>MOVE_COUNTER_THRESH*6){
   moveCounter=0;
  }
 }

 
 //called every time the player presses the jump key (SPACE for now)
 public void jump() {
  //sets the jumping state to true
  this.jumping=true;
  
  //reinitialize the jump_count
  this.jump_count=0;
  
  //sets the current jumping frame based on the last direction 
  if(last_direction==KeyEvent.VK_RIGHT){
   currentFrame=run_R[2];
  } else {
   currentFrame=run_L[2];
  }
 }
 
 public boolean getJumping() {
  return jumping;
 }
 
 //jump_count works with JUMP_COUNTER_THRESH 
 //the variable is incremented every time the main thread calls checkState() until it = JUMP_COUNTER_THRESH
 //then reverses and character descends until = JUMP_COUNTER_THRESH*2, the end of our not very parabolic jump
 //and thus the boolean becomes false and count is restarted 
 private int jump_count=0;
 
 //if jump is in progress 
 private boolean jumping;

 public BufferedImage getCurrentFrame(){
  return currentFrame;
 }

//get x position 
 public int getCurrentX(){
  return currentX;
 }
 
 //get y position 
 public int getCurrentY(){
  return currentY;
 }
 
 //gets the bounding box of the character
 public Rectangle getBoundingBox() {
  return boundingBox;
 }
 
 //the stop() function sets an idle position as current frame
 public void stop() {
  //if the last direction was right
  if(last_direction==KeyEvent.VK_RIGHT){
   currentFrame=idle_R;
  //if was left 
  } else {
   currentFrame=idle_L;
  }
  this.idle=true;
 }
 
 
 public boolean getFalling(){
  return falling;
 }

 public boolean getRestoring() {
  return restoring;
 }
 
 public int getLife() {
  return life;
 }

 public boolean outOfBounds() {
  if(currentX>=GameFrame.WIDTH){
    //currently there is no next frame to progress to so plops you right back at the start 
    currentX = 0; 
   return true;
  }
  
  return false;
 }
 
 private final static int RESTORING_THRESH=84;
 
 private final static int RESTORING_MODULE=12;
 
 private int restoring_count=0;
 
 //restoring is true when you just died, it's the blink in-out animation 
 private boolean restoring=false;
 
 //starts as false because you start on terrain 
 private boolean falling=false;
 
 //upper bound for jump count
 private static final int JUMP_COUNTER_THRESH=20;
 
 //initially the last direction is right (since we start on idle_R) 
 private int last_direction=KeyEvent.VK_RIGHT;
 
 private static final int MOVE_COUNTER_THRESH=5;
 private int moveCounter=0;
 private Rectangle boundingBox;
 
 //distance covered by a single step
 private static final int DISPLACEMENT=4;
 
 private BufferedImage currentFrame;
 
 //size of the run animation buffer - a slot for each frame
 private static final int BUFFER_RUN_SIZE=6;
 
 //all the bufferedImages used in the character's animation 
 private BufferedImage idle_R;
 private BufferedImage idle_L;
 private BufferedImage[] run_R;
 private BufferedImage[] run_L;
 
 //determines the currentFrame to be used in a run animation
 private int currentFrameNumber=0;
 
 //the initial width offset of the character
 public static final int BOY_START_X=128;

 public static final int MAX_LIFE = 3;
 
 //height of the main character (used to set the boundingBox)
 private final int BOY_HEIGHT=64;
 
 //width of the main character (used to set the boundingBox)
 private final int BOY_WIDTH=32;
 
 //current position of the character along the x-axis 
 //initially the character is placed at BOY_START_X
 private int currentX=BOY_START_X;
 
 //current position of the character along the y-axis 
 //initially the character is placed at BOY_START_X
 private int currentY=GameFrame.HEIGHT-PlayPanel.TERRAIN_HEIGHT-BOY_HEIGHT;
 
 private int currentCol=currentX/Tile.TILE_SIZE;
 
 private int currentRow=currentY/Tile.TILE_SIZE;
 

 private boolean idle=true;
 private int life=MAX_LIFE;
}
