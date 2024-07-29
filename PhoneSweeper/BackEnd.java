public class BackEnd {
 //contains the bombs
   public static int[][] bomb = new int[15][15]; // empty = 0, bomb = 10
   
    //populates the bomb array with the bombs
   public static void placeBomb() {
      int num = 35;
      
      boolean finish = placeBomb(num);
   }

   public static boolean placeBomb(int z) {
      if (z == 0) {
         return true;
      } else {
         int x = (int) (Math.random() * 15);
         int y = (int) (Math.random() * 15);
         if (bomb[y][x] == 0) {
            bomb[y][x] = 10;
            placeBomb(z - 1);
            return false;
         } else {
            placeBomb(z);
            return false;
         }
      }
   
   }
   
   public static void click(int x, int y){
      
      if (x == 0 && y == 0) {
         bomb[y][x + 1] = 100;
         bomb[y + 1][x] = 100;
         bomb[y + 1][x + 1] = 100;
      } else if (x == 14 && y == 0) {
         bomb[y][x - 1] = 100;
         bomb[y + 1][x] = 100;
         bomb[y + 1][x - 1] = 100;
      } else if (x == 0 && y == 14) {
         bomb[y - 1][x] = 100;
         bomb[y][x + 1] = 100;
         bomb[y - 1][x + 1] = 100;
      } else if (x == 14 && y == 14) {
         bomb[y - 1][x] = 100;
         bomb[y][x - 1] = 100;
         bomb[y - 1][x - 1] = 100;
      } else if (y == 0) {
         bomb[y + 1][x] = 100;
         bomb[y][x + 1] = 100;
         bomb[y + 1][x + 1] = 100;
         bomb[y][x - 1] = 100;
         bomb[y + 1][x - 1] = 100;
      } else if (y == 14) {
         bomb[y - 1][x] = 100;
         bomb[y][x + 1] = 100;
         bomb[y - 1][x + 1] = 100;
         bomb[y][x - 1] = 100;
         bomb[y - 1][x - 1] = 100;
      } else if (x == 0) {
         bomb[y + 1][x] = 100;
         bomb[y][x + 1] = 100;
         bomb[y + 1][x + 1] = 100;
         bomb[y - 1][x + 1] = 100;
         bomb[y - 1][x] = 100;
      } else if (x == 14) {
         bomb[y + 1][x] = 100;
         bomb[y][x - 1] = 100;
         bomb[y + 1][x - 1] = 100;
         bomb[y - 1][x - 1] = 100;
         bomb[y - 1][x] = 100;
      } else {
         bomb[y + 1][x] = 100;
         bomb[y][x - 1] = 100;
         bomb[y + 1][x - 1] = 100;
         bomb[y - 1][x - 1] = 100;
         bomb[y - 1][x] = 100;
         bomb[y][x + 1] = 100;
         bomb[y + 1][x + 1] = 100;
         bomb[y - 1][x + 1] = 100;
      }
      
      
      
   
   }
   

//populates the rest of the bomb array with the other numbers
   public static void placeNumber() {
      int x = 0;
      int y = 0;
      int xMax = 15 - 1;
      int yMax = xMax;
      int bombCounter;
      for (x = 0; x <= xMax; x++) {
         for (y = 0; y <= yMax; y++) {
            bombCounter = 0;
            if (bomb[y][x] != 10) {
            
                 // top left
               if (x == 0 && y == 0) {
                     // right
                  if (bomb[y][x + 1] == 10) {
                     bombCounter++;
                  }
                     // down
                  if (bomb[y + 1][x] == 10) {
                     bombCounter++;
                  }
                     // corner
                  if (bomb[y + 1][x + 1] == 10) {
                     bombCounter++;
                  }
                  bomb[y][x] = bombCounter;
               
               
                     // top right corner
               } else if (x == xMax && y == 0) {
                     // left
                  if (bomb[y][x - 1] == 10) {
                     bombCounter++;
                  }
                     // down
                  if (bomb[y + 1][x] == 10) {
                     bombCounter++;
                  }
                     // leftcorner
                  if (bomb[y + 1][x - 1] == 10) {
                     bombCounter++;
                  }
                  bomb[y][x] = bombCounter;
               
               
                     // bottom left
               } else if (x == 0 && y == yMax) {
                     // top
                  if (bomb[y - 1][x] == 10) {
                     bombCounter++;
                  }
                     // right
                  if (bomb[y][x + 1] == 10) {
                     bombCounter++;
                  }
                     // right corner
                  if (bomb[y - 1][x + 1] == 10) {
                     bombCounter++;
                  }
                  bomb[y][x] = bombCounter;
               
               
                     // bottom right
               } else if (x == xMax && y == yMax) {
                     // top
                  if (bomb[y - 1][x] == 10) {
                     bombCounter++;
                  }
                     // left
                  if (bomb[y][x - 1] == 10) {
                     bombCounter++;
                  }
                     // leftcorner
                  if (bomb[y - 1][x - 1] == 10) {
                     bombCounter++;
                  }
                  bomb[y][x] = bombCounter;
               
               
                     // top row
               } else if (y == 0) {
                     // bottom
                  if (bomb[y + 1][x] == 10) {
                     bombCounter++;
                  }
                     // right
                  if (bomb[y][x + 1] == 10) {
                     bombCounter++;
                  }
                     // right corner
                  if (bomb[y + 1][x + 1] == 10) {
                     bombCounter++;
                  }
                     // left
                  if (bomb[y][x - 1] == 10) {
                     bombCounter++;
                  }
                     // left corner
                  if (bomb[y + 1][x - 1] == 10) {
                     bombCounter++;
                  }
                  bomb[y][x] = bombCounter;
               
               
                     // bottom row
               } else if (y == yMax) {
                     // top
                  if (bomb[y - 1][x] == 10) {
                     bombCounter++;
                  }
                     // right
                  if (bomb[y][x + 1] == 10) {
                     bombCounter++;
                  }
                     // right corner
                  if (bomb[y - 1][x + 1] == 10) {
                     bombCounter++;
                  }
                     // left
                  if (bomb[y][x - 1] == 10) {
                     bombCounter++;
                  }
                     // left corner
                  if (bomb[y - 1][x - 1] == 10) {
                     bombCounter++;
                  }
                  bomb[y][x] = bombCounter;
               
               
                     // left side
               } else if (x == 0) {
                     // bottom
                  if (bomb[y + 1][x] == 10) {
                     bombCounter++;
                  }
                     // right
                  if (bomb[y][x + 1] == 10) {
                     bombCounter++;
                  }
                     // right bottom corner
                  if (bomb[y + 1][x + 1] == 10) {
                     bombCounter++;
                  }
                     // right top corner
                  if (bomb[y - 1][x + 1] == 10) {
                     bombCounter++;
                  }
                     // top
                  if (bomb[y - 1][x] == 10) {
                     bombCounter++;
                  }
                  bomb[y][x] = bombCounter;
               
               
                     // right side
               } else if (x == xMax) {
                     // bottom
                  if (bomb[y + 1][x] == 10) {
                     bombCounter++;
                  }
                     // left
                  if (bomb[y][x - 1] == 10) {
                     bombCounter++;
                  }
                     // left bottom corner
                  if (bomb[y + 1][x - 1] == 10) {
                     bombCounter++;
                  }
                     // left top corner
                  if (bomb[y - 1][x - 1] == 10) {
                     bombCounter++;
                  }
                     // top
                  if (bomb[y - 1][x] == 10) {
                     bombCounter++;
                  }
                  bomb[y][x] = bombCounter;
               
                     //middle squares
               } else {
                     // bottom
                  if (bomb[y + 1][x] == 10) {
                     bombCounter++;
                  }
                     // left
                  if (bomb[y][x - 1] == 10) {
                     bombCounter++;
                  }
                     // left bottom corner
                  if (bomb[y + 1][x - 1] == 10) {
                     bombCounter++;
                  }
                     // left top corner
                  if (bomb[y - 1][x - 1] == 10) {
                     bombCounter++;
                  }
                     // top
                  if (bomb[y - 1][x] == 10) {
                     bombCounter++;
                  }
                     // right
                  if (bomb[y][x + 1] == 10) {
                     bombCounter++;
                  }
                     // right bottom corner
                  if (bomb[y + 1][x + 1] == 10) {
                     bombCounter++;
                  }
                     // right top corner
                  if (bomb[y - 1][x + 1] == 10) {
                     bombCounter++;
                  }
                  bomb[y][x] = bombCounter;
               }
            }
         }
      }
   
   }


}