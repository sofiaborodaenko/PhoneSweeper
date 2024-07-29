//This class is where most of the game play runs
import java.awt.*;
import java.awt.event.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class PhoneSweeper extends JFrame implements ActionListener, KeyListener, MouseListener {
  // Canvas size
   public static final int CANVAS_WIDTH = 600;
   public static final int CANVAS_HEIGHT = 600;
   
   //Custom colors for the program
   public static final Color DARK_GREEN = new Color(4, 114, 77);
   public static final Color LIGHT_GREEN = new Color(165, 204, 107);
   public static final Color DARK_BROWN = new Color(176, 137, 104);
   public static final Color LIGHT_BROWN = new Color(221, 184, 146);
   public static final Color BACKGROUND = new Color(108, 88, 76);
   public static final Color FONT = new Color(173, 193, 120);
   public static final Color SOFIAGREEN = new Color(221, 229, 182);

  //Custom fonts
   public static final Font title = new Font("Bodoni MT", Font.PLAIN, 60);
   public static final Font subTitle = new Font("Bodoni MT", Font.PLAIN, 20);
   public static final Font frontScreenNum = new Font("Bodoni MT", Font.PLAIN, 30);
   public static final Font inGameNum = new Font("Bodoni MT", Font.PLAIN, 20);
   public static final Font gameOver = new Font("Bodoni MT", Font.PLAIN, 70);

  // the custom drawing canvas (extends JPanel)
   private DrawCanvas canvas;

  // Grass grid
  // true = grass still attach, false = grass remove
   public boolean[][] grass = new boolean[15][15];
  // true = lighter color, false = darker color
   boolean[][] grassColor = new boolean[15][15];
   //if there's a flag placed or not
   public boolean[][] flag = new boolean [15][15];
   int pause = 0;

  // GAMESTATE
   int GAMESTATE, MAINMENU = 0, PLAYING = 1, LOSE = 2, WON = 3, HOWTOPLAY = 4;

  // Buttons
   JButton start, howToPlay, quit, back, playAgain;
   //JLabel for Flags
   JLabel jFlags, mittenIcon;

  //Images
   BufferedImage coreaIsCool, coreaNoPhones, iphone, samsung, nokia, mitten, coreaLost, coreaWon, mittenforicon;

  //timer updates the screen
   Timer timer;

  // coordinates for the little animation screen
   int x1, x2, x3, x4, x5, x6, x7;
   int y1, y2, y3, y4, y5, y6, y7;
   //For main menu
   ArrayList<Shape> Shape = new ArrayList<Shape>();

  //counter for animation
   int counter = 0;
   int dontTouch = (int)(Math.random()*3); //this randomize the ending messages and phones
   
   //indicate where the player mouse is pressed
   public int buttonPressX = 0;
   public int buttonPressY = 0;
   
   //Game play
   public boolean firstClick = false; //randomize bomb after the player's first click
   int flagOnBomb = 0; //check if flag is place correctly
   int ammountOfFlags = 35; //Maximum number of flag

   public PhoneSweeper() {
     // set the gamestate automatically to main menu;
      GAMESTATE = MAINMENU;
   
     // load the images
      coreaIsCool = loadImage("coreaIsCool.png");
      coreaNoPhones = loadImage("coreaNoPhones.png");
      iphone = loadImage("iphone.png");
      samsung = loadImage("samsung.png");
      nokia = loadImage("nokia.png");
      mitten = loadImage("mitten.png");
      coreaWon = loadImage("coreaWon.png");
      coreaLost = loadImage("coreaLost.png");
      mittenforicon = loadImage("mittenverysmall.png");
   
     // Making the Canvas
      Container cp = getContentPane();
      JPanel mainMenuPanel = new JPanel(null);
      mainMenuPanel.setPreferredSize(new Dimension(CANVAS_WIDTH, 80));
   
     // Set up a custom drawing JPanel
      canvas = new DrawCanvas();
      canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
   
     // Rectrieve content of the frame and set the layout
      cp.setLayout(new BorderLayout());
   
     // add panel to the content of the frame
      cp.add(canvas, BorderLayout.CENTER);
      cp.add(mainMenuPanel, BorderLayout.SOUTH);
      setResizable(false);
      
      //Call to make the grid
      assignGrassColor();
   
     // set Timer
      timer = new Timer(20, this);
      timer.start();
      
      
      // create JLabel
      jFlags = new JLabel("Flags: " + ammountOfFlags);
      mittenIcon = new JLabel(new ImageIcon(mittenforicon));
      
     // create buttons
      start = new JButton("START");
      howToPlay = new JButton("HOW TO PLAY");
      quit = new JButton("QUIT");
      back = new JButton("BACK");
      playAgain = new JButton("PLAY AGAIN");
   
     // listens for button presses
      start.addActionListener(this);
      howToPlay.addActionListener(this);
      quit.addActionListener(this);
      back.addActionListener(this);
      playAgain.addActionListener(this);
      canvas.addMouseListener(this);
   
     // set size and location for buttons
      start.setSize(140, 50);
      start.setLocation(85, 15);
      howToPlay.setSize(140, 50);
      howToPlay.setLocation(225, 15);
      quit.setSize(140, 50);
      quit.setLocation(365, 15);
      back.setSize(140, 50);
      back.setLocation(225, 15);
      playAgain.setSize(140, 50);
      playAgain.setLocation(225, 15);
     // set size and location for labels
      jFlags.setFont(inGameNum);
      jFlags.setSize(240, 50);
      jFlags.setLocation(125, 15);
      mittenIcon.setSize(50, 50);
      mittenIcon.setLocation(80, 12);
    
   
     // if the gamestate is on main menu add the buttons
      if (GAMESTATE == MAINMENU) {
         mainMenuPanel.add(start);
         mainMenuPanel.add(howToPlay);
         mainMenuPanel.add(quit);
      
         mainMenuPanel.add(back);
         back.setVisible(false);
         mainMenuPanel.add(playAgain);
         playAgain.setVisible(false);
         mainMenuPanel.add(jFlags);
         jFlags.setVisible(false);
         mainMenuPanel.add(mittenIcon);
         mittenIcon.setVisible(false);
      
      //Remove how to play button when user is reading the how to play rule
      } else if (GAMESTATE == HOWTOPLAY) {
         mainMenuPanel.remove(howToPlay);
      
      //If player is playing, only contains the quit button
      } else if (GAMESTATE == PLAYING) {
         mainMenuPanel.remove(start);
         mainMenuPanel.remove(howToPlay);
         mainMenuPanel.remove(back);
      
      }
   
     // populates the arraylist for the animation
      animationSquares();
   
     // listen for keypresses on this frame
      addKeyListener(this);
   
     // set default actions and other properties
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     // Handle the CLOSE button
      setTitle("PhoneSweeper");
      pack(); // pack all the components in the JFrame
      setVisible(true); // show it
      requestFocus();
   
   }
   
  // updates the coordinates of the main menu (the falling tiles)
   public void updateCoordinates() {
      if (GAMESTATE == MAINMENU) {
         for (int i = 0; i <= 6; i++) {
            //update the shapes location
            Rectangle2D.Double temp = (Rectangle2D.Double) Shape.get(i);
            if (counter / 100 < 1) {
               temp.x -= 2;
               temp.y -= 2;
            } else if (counter / 100 < 18) {
               temp.x += 2;
               temp.y += 2;
            }
            counter++;
         }
      }
   }

  // populates the squares that fall into an arraylist
   public void animationSquares() {
      //the shapes that will fall down locations
      int x1 = 600, x2 = 420, x3 = 60, x4 = 300, x5 = 360;
      int y1 = 120, y2 = -120, y3 = 300, y4 = 0, y5 = -300;
      
      //add the shapes to the array
      Shape.add(new Rectangle2D.Double(x1, y1, 60, 60)); // lg
      Shape.add(new Rectangle2D.Double(x1, y2, 60, 60));
      Shape.add(new Rectangle2D.Double(x2, y3, 60, 60));
      Shape.add(new Rectangle2D.Double(x2, y1, 60, 60)); // dg
      Shape.add(new Rectangle2D.Double(x3, y4, 60, 60));
      Shape.add(new Rectangle2D.Double(x4, y2, 60, 60));
      Shape.add(new Rectangle2D.Double(x5, y5, 60, 60));
   
   }

  // assign so the ground can have grid color :)
   public void assignGrassColor() {
      boolean color = true;
      // true = light color, false = dark color
      for (int i = 0; i < 15; i++) {
         for (int j = 0; j < 15; j++) {
            if (color == false) {
               grassColor[i][j] = color;
               color = true;
            } else {
               grassColor[i][j] = color;
               color = false;
            }
         }
      }
   }
   
   //run the program
   public void actionPerformed(ActionEvent e) {
      //run the timer
      if (e.getSource() == timer) {
         updateCoordinates();
         //update flag numbers
         jFlags.setText("Flags: " + ammountOfFlags);
         //repaint the canvas
         canvas.repaint();
      }
      
      //if clicked start
      if (e.getSource() == start) {
        // change the gamestate
         GAMESTATE = PLAYING;
         
         //add the flags and icons
         jFlags.setVisible(true);
         mittenIcon.setVisible(true);
       
        // make the buttons invisible
         start.setVisible(false);
         howToPlay.setVisible(false);
         back.setVisible(false);
         Shape.clear();
      
      //if clicked how to play
      } else if (e.getSource() == howToPlay) {
        // change the gamestate
         GAMESTATE = HOWTOPLAY;
      
         // make button invisible
         howToPlay.setVisible(false);
         back.setVisible(true);
         Shape.clear();
      
      //return the main menu
      } else if (e.getSource() == back) {
         GAMESTATE = MAINMENU;
       
         back.setVisible(false);
         howToPlay.setVisible(true);
       
         animationSquares();
         counter = 0;
      
      //return to main menu and reset everything
      }  else if (e.getSource() == playAgain){
         GAMESTATE = MAINMENU;
       
         playAgain.setVisible(false);
         howToPlay.setVisible(true);
         start.setVisible(true);
       
         Shape.clear();
         animationSquares();
         counter = 0;
         ammountOfFlags = 35;
         flagOnBomb = 0;
         dontTouch = (int)(Math.random()*3);
         pause = 0;
         
         //reset the arrays
         for (int i = 0; i < 15; i++){
            for (int j = 0; j < 15; j++){
               grass[i][j] = false;
               BackEnd.bomb[i][j] = 0;
               flag[i][j] = false;
            }
         }
         firstClick = false;
         buttonPressX = 0;
         buttonPressY = 0;
      
      }
      //terminate the program when 
      if (e.getSource() == quit) {
         System.exit(0);
      }
   }

   @Override
   public void mouseClicked(MouseEvent e) {}

   @Override
   //Track when the mouse is left click and right click
   public void mousePressed(MouseEvent e) {
      if (GAMESTATE == PLAYING) {
         buttonPressX = (int) e.getX();
         buttonPressY = (int) e.getY();
         //if left click, open the grass
         if(SwingUtilities.isLeftMouseButton(e) && buttonPressX != 0 && buttonPressY != 0){
            click();
         }
         //if right click, place a flag
         if(SwingUtilities.isRightMouseButton(e) && buttonPressX != 0 && buttonPressY != 0){
            placeFlag();
         }
      }
   }


   @Override
   public void mouseReleased(MouseEvent e) {}

   @Override
   public void mouseEntered(MouseEvent e) {}

   @Override
   public void mouseExited(MouseEvent e) {}

   public void keyPressed(KeyEvent evt) {}

   public void keyReleased(KeyEvent evt) {}

   public void keyTyped(KeyEvent evt) {}
   
   //place flag if right click
   public void placeFlag(){
      //get square index
      int x = buttonPressX / 40;
      int y = buttonPressY / 40;
      
      //if the square isnt' open yet, proceed
      if (!grass[y][x]){
            //if flag is already placed, remove it
         if (flag[y][x]){
            flag[y][x] = false;
            ammountOfFlags++;
            //check if it's where the bomb is
            if (BackEnd.bomb[y][x] == 10) {
               flagOnBomb--;
            }
         }else{
            //if there's no flag, place a flag. Make sure that it's not over the number of avaliable flags
            if (ammountOfFlags > 0) {
               flag[y][x] = true;
               ammountOfFlags--;
               //if it's placed correctly, add one flagOnBomb variable
               if (BackEnd.bomb[y][x] == 10) {
                  flagOnBomb++;
               }
            }
         }
         //if all 35 flags is placed correctly, you win the game
         if (flagOnBomb == 35) {
            GAMESTATE = WON;
         }
      }
   }

 
   //Open the squares when user clicks
   public void openSquares(int x, int y){
      //if index is out of bound, return
      if (x < 0 || x >= 15 || y < 0 || y >= 15){
         return;
      }
      //if it's already open, return
      if (grass[y][x] == true){
         return;
      }
      //if click on an empty spot, opens the open around it
      if (BackEnd.bomb[y][x] == 0 || BackEnd.bomb[y][x] == 100) {
       
         grass[y][x] = true;
         
         //check the spot around it using recursion
         openSquares(x, y-1);
         openSquares(x, y+1);
         openSquares(x-1, y);
         openSquares(x+1, y);
         openSquares(x-1, y-1);
         openSquares(x+1, y-1);
         openSquares(x+1, y+1);
         openSquares(x-1, y+1);
      
      } else {
         //if it's not empty, don't open more
         grass[y][x] = true;
      }
   }

  
   //When player left click
   public void click() {
      //get square index
      int x = buttonPressX / 40;
      int y = buttonPressY / 40;
      
      //If there isn't a flag placed
      if (!flag[y][x]){
         //If this is the player first click, randomize the bomb and put in the numbers locations
         if (firstClick == false) {
            BackEnd.bomb[y][x] = 100;
            BackEnd.click(x, y);    
            BackEnd.placeBomb();
            BackEnd.placeNumber();
            firstClick = true;
            openSquares(x, y);
         
         }else{
         //if not the first click, just open
            openSquares(x, y);
            
            //if open a bomb, the player loses
            if (BackEnd.bomb[y][x] == 10){
               GAMESTATE = LOSE;
               for (int i = 0; i < 15; i ++) {
                  for (int j = 0; j < 15; j++) {
                  
                     if (BackEnd.bomb[i][j] == 10){
                        grass[i][j] = true;
                        GAMESTATE = LOSE;
                     }
                  }
               
               }
            }
         }
      }
   }

  // Drawing the Canvas
   class DrawCanvas extends JPanel {
      public void paintComponent(Graphics g) {
         super.paintComponent(g);
         Graphics2D g2d = (Graphics2D) g;
         
         //Draw the canvas base on the game state
         if (GAMESTATE == MAINMENU) {
            mainMenu(g);
            //falling shapes
            for (int i = 0; i < Shape.size(); i++) {
               if (i <= 2) {
                  g2d.setColor(SOFIAGREEN);
               } else {
                  g2d.setColor(FONT);
               }
               g2d.fill(Shape.get(i));
            
            }
         
         } else if (GAMESTATE == HOWTOPLAY) {
            howToPlayMenu(g);
         
         } else if (GAMESTATE == PLAYING) {
            gameLayout(g);
         
         } else if (GAMESTATE == WON){
            winScreen(g);
            playAgain.setVisible(true);
            mittenIcon.setVisible(false);
            jFlags.setVisible(false);
         
         //if Lose, delay the program a bit for the player to see the bomb
         }else if (GAMESTATE == LOSE){
            if (pause == 0){
               gameLayout(g);
               pause = 1;
            }else if (pause == 1){
               try{
                  Thread.sleep(3000);
               }catch(InterruptedException e){}
               pause = 2;
            }else{
               loseScreen(g);
               playAgain.setVisible(true);
               mittenIcon.setVisible(false);
               jFlags.setVisible(false);
            }
         }
      
      }
   
     // draws the starting screen of the game
      public void mainMenu(Graphics g) {
         Graphics2D g2d = (Graphics2D) g;
         setBackground(Color.WHITE);
      
         g.setColor(Color.RED);
         g2d.rotate(Math.toRadians(45));
         
         //locations
         int lox = 0;
         int loy = 0;
         int size = 60;
      
         for (int i = 0; i < 15; i++) {
         
            lox = 0;
            for (int j = 0; j < 15; j++) {
              // if it's a dark color
               if (grassColor[i][j] == false) {
                 // draw square
                  g.setColor(FONT);
                  g.fillRect(lox, loy, size, size);
               
                 // if light color
               } else {
                  g.setColor(SOFIAGREEN);
                  g.fillRect(lox, loy, size, size);
               }
               lox += 59;
            }
            loy -= 59;
         }
      
         lox = 0;
         loy = 0;
      
         for (int i = 0; i < 15; i++) {
         
            lox = 0;
            for (int j = 0; j < 15; j++) {
              // if it's a dark color
               if (grassColor[i][j] == false) {
                 //draw square
                  g.setColor(FONT);
                  g.fillRect(lox, loy, size, size);
               
                 // if light color
               } else {
               
                  g.setColor(SOFIAGREEN);
                  g.fillRect(lox, loy, size, size);
               }
               lox += 59;
            }
            loy += 59;
         }
         
         //rotate the squares
         g2d.rotate(Math.toRadians(315));
      
         g.setColor(BACKGROUND);
         g.setFont(title);
         g.drawString("PHONESWEEPER", 65, CANVAS_HEIGHT / 2);
      
         g2d.rotate(Math.toRadians(45));
      
        // brown squares under
         g.fillRect(59, 0, 60, 60); // dg
         g.fillRect(300 - 5, -120 + 2, 60, 60); // dg
         g.fillRect(360 - 6, -300 + 5, 60, 60); // dg
         g.fillRect(600 - 10, -120 + 2, 60, 60); // lg
      
         g.fillRect(420 - 7, 120 - 2, 60, 60); // lg
         g.fillRect(420 - 7, 300 - 5, 60, 60); // dg
         g.fillRect(600 - 10, 120 - 2, 60, 60); // lg
      
        // prints the numbers
         g2d.rotate(Math.toRadians(315));
         g.setFont(frontScreenNum);
         g.setColor(Color.RED);
         g.drawString("1", 452, 97);
         g.drawString("3", 77, 555);
         
         //draw the images
         g.drawImage(coreaIsCool, 460, CANVAS_HEIGHT / 2 + 20, 120, 140, null);
         g.drawImage(coreaNoPhones, 20, CANVAS_HEIGHT / 2 + 20, 120, 140, null);
         g.drawImage(iphone, 20, 63, 45, 45, null);
         g.drawImage(samsung, 274, 148, 35, 45, null);
         g.drawImage(nokia, 308, CANVAS_HEIGHT - 83, 50, 55, null);
         g.drawImage(mitten, 97, 555, 60, 60, null);
         g2d.rotate(Math.toRadians(45));
      
      }
      
      //how to play screen
      public void howToPlayMenu(Graphics g) {
         setBackground(BACKGROUND);
         g.setFont(title);
         g.setColor(FONT);
         g.drawString("HOW TO PLAY", 90, 70);
         g.setFont(subTitle);
         g.drawString("- Phone (mines) are hidden under the square grid", 55, 140);
         g.drawString("- If you open a safe square, it will have a number", 55, 190);
         g.drawString("indiciating the amount of phones in the perimeter", 25, 240);
         g.drawString("- You can use the number clues to solve the ", 55, 290);
         g.drawString("game by placing the mittens (flags) on top of the ", 25, 340);
         g.drawString("squares containing the bombs.", 25, 390);
         g.drawString("- Right click to place the mitten on top of the ", 55, 440);
         g.drawString("square with the phone", 25, 490);
         g.drawString("- If you click on a mine you lose the game!", 55, 540);
      
      }
   
     // draw the layout of the grass
      public void gameLayout(Graphics g) {
         setBackground(Color.WHITE);
         //grass kicatuib
         int lox = 0;
         int loy = 0;
         //images location
         int imageLocX;
         int imageLocY;
         int imageW;
         int imageH;
         BufferedImage bombImage;
         
      
        // size of the square
         int size = 40;
         
         //Use of for loop to draw the square
         for (int i = 0; i < 15; i++) {
            lox = 0;
            for (int j = 0; j < 15; j++) {
              // if it's a dark color
               if (grassColor[i][j] == false) {
                 // if the grass is not open, only draw square
                  if (grass[i][j] == false) {
                     g.setColor(DARK_GREEN);
                     g.fillRect(lox, loy, size, size);
                     if (flag[i][j]){
                        g.drawImage(mitten, lox + 1, loy + 3, 40, 35, null);
                     }
                    // grass is open
                  } else {
                     g.setColor(DARK_BROWN);
                     g.fillRect(lox, loy, size, size);
                  
                    //bomb
                     if (BackEnd.bomb[i][j] == 10) {
                        if (dontTouch == 0) {
                           imageLocX = 3;
                           imageLocY = 3;
                           imageW = 35;
                           imageH = 35;
                           bombImage = iphone;
                        } else if (dontTouch == 1){
                           imageLocX = 6;
                           imageLocY = 6;
                           imageW = 25;
                           imageH = 30;
                           bombImage = samsung;
                        } else {
                           imageLocX = 3;
                           imageLocY = 3;
                           imageW = 35;
                           imageH = 35;
                           bombImage = nokia;
                        
                        }
                        g.drawImage(bombImage, lox+imageLocX, loy+imageLocY,imageW, imageH, null);
 
                     //number
                     } else if (BackEnd.bomb[i][j] != 0) {
                        g.setColor(Color.WHITE);
                        g.setFont(inGameNum);
                        g.drawString(Integer.toString(BackEnd.bomb[i][j]), lox + 15, loy + 27);
                     }
                  }
                 // if light color
               } else {
                 // if grass is not open, only draw square
                  if (grass[i][j] == false) {
                     g.setColor(LIGHT_GREEN);
                     g.fillRect(lox, loy, size, size);
                     if (flag[i][j]){
                        g.drawImage(mitten, lox + 1, loy + 3, 40, 35, null);
                     }
                    // grass is open
                  } else {
                     g.setColor(LIGHT_BROWN);
                     g.fillRect(lox, loy, size, size);
                    // bomb
                     if (BackEnd.bomb[i][j] == 10) {
                        if (dontTouch == 0) {
                           imageLocX = 3;
                           imageLocY = 3;
                           imageW = 35;
                           imageH = 35;
                           bombImage = iphone;
                        } else if (dontTouch == 1){
                           imageLocX = 6;
                           imageLocY = 6;
                           imageW = 25;
                           imageH = 30;
                           bombImage = samsung;
                        } else {
                           imageLocX = 3;
                           imageLocY = 3;
                           imageW = 35;
                           imageH = 35;
                           bombImage = nokia;
                        
                        }
                        g.drawImage(bombImage, lox+imageLocX, loy+imageLocY,imageW, imageH, null);
                     
                     //number
                     } else if (BackEnd.bomb[i][j] != 0){
                        g.setColor(Color.WHITE);
                        g.setFont(inGameNum);
                        g.drawString(Integer.toString(BackEnd.bomb[i][j]), lox + 15, loy + 27);
                     }
                  }
               }
               lox += size;
            }
            loy += size;
         }
      }
      
      //lose screeb
      public void loseScreen(Graphics g){
         setBackground(BACKGROUND);
         g.setColor(FONT);
         g.setFont(gameOver);
         g.drawString("YOU LOST", 120,170);
         g.drawImage(coreaLost, 150, 230, 100*3, 120*3, null);
         String message;
         if (dontTouch == 0){
            message = "Mr.Corea is disapointed in you";
         }else if (dontTouch == 1){
            message = "You're a potato";
         }else{
            message = "You're a turkey";
         }
         g.setFont(subTitle);
         g.drawString(message, 0, 20);
       
      }
      //Winning screen
      public void winScreen(Graphics g){
         setBackground(FONT);
         g.setColor(BACKGROUND);
         g.setFont(gameOver);
         g.drawString("YOU WON", 120,170);
         g.drawImage(coreaWon, 150, 230, 100*3, 120*3, null);
         String message;
         if (dontTouch == 0){
            message = "So proud";
         }else if (dontTouch == 1){
            message = "crazy.";
         }else{
            message = "Took you long enough";
         }
         g.setFont(subTitle);
         g.drawString(message, 0, 20);
      }
   
   }


  // load the images
   public BufferedImage loadImage(String path) {
      BufferedImage img = null;
      File temp = new File(path);
      try {
         img = ImageIO.read(new java.io.File(path));
      } catch (Exception e) {
         System.err.println("Exception Occurred");
         e.printStackTrace();
      }
      return img;
   }


}


























