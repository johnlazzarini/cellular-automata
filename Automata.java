import java.util.Scanner;
import java.lang.System.*;

//Test edit

public class Automata
{
   //this was the provided main
   public static void main(String[] args)
   {
      int rule, k;
      String strUserIn;
      
      Scanner inputStream = new Scanner(System.in);
      Automaton aut;

      // get rule from user
      do
      {
         System.out.print("Enter Rule (0 - 255): ");
         // get the answer in the form of a string:
         strUserIn = inputStream.nextLine();
         // and convert it to a number so we can compute:
         rule = Integer.parseInt(strUserIn);
      } while (rule < 0 || rule > 255);

      // create automaton with this rule and single central dot
      aut = new Automaton(rule);

      // now show it
      System.out.println("   start");
      for (k = 0; k < 100; k++)
      {
         System.out.println( aut.toStringCurrentGen() );
         aut.propagateNewGeneration();
      }
      System.out.println("   end");
      inputStream.close();
   }
}

/*
 * this class assigns an initial value for the game, "*",
 * and translates a rule into binary that is then translated
 * to a series of rules that govern the behavior of future
 * generations from this generation.
 */
class Automaton
{
// class constants
   public final static int MAX_DISPLAY_WIDTH = 125;
   
   //private members
   private boolean rules[];  // allocate rules[8] in constructor!
   private String thisGen;   // same here
   String extremeBit; // bit, "*" or " ", implied everywhere "outside"
   int displayWidth;  // an odd number so it can be perfectly centered
   
   // public constructors, mutators, etc. (need to be written)
   public Automaton(int newRule)
   {
      setRule(newRule);
      setDisplayWidth(79);
      thisGen = "*";
      extremeBit = " ";
   }
   
   public void resetFirstGen()
   {
      thisGen = "*";
      extremeBit = " ";
   }
   
   public boolean setRule(int newRule)
   {
      if (newRule > 255 || newRule < 0)
         return false;
      
      String binaryVal = Integer.toBinaryString(newRule);
      String binary = "";
      
      if (newRule < 128)
      {
      int difference = 8 - binaryVal.length();
      
      if (difference != 0)
      {   
         for (int i = 0; i < difference; i++)
         {
            binary += "0";
         }
         binary += binaryVal;
      }
      }
      else
         binary = binaryVal;
      
      rules = new boolean[binary.length()];
      
      for (int i = 0; i < binary.length(); i++)
      {
         if (binary.charAt(i) == '0')
            rules[(binary.length() - 1) - i] = false;
         else
            rules[(binary.length() - 1) - i] = true;
      }      
      return true;
   }
   
   public boolean setDisplayWidth(int width)
   {
      if (width < 0 || width > MAX_DISPLAY_WIDTH
            || width/2.0 == 0)
         return false;
      
      this.displayWidth = width;
      return true;
   }
   
   public String toStringCurrentGen()
   {
      String retString = "";
      int extreme;
      if (thisGen.length() < displayWidth)
      {
         extreme = (displayWidth - thisGen.length())/2;
         
         retString = extremeLoop(retString, extreme);
         retString += thisGen;
         retString = extremeLoop(retString, extreme);
         
         return retString;
      }
      else if (thisGen.length() >= displayWidth)
      {
         int truncate = (thisGen.length() - displayWidth)/2;
         
         retString = thisGen.substring(truncate,
               (thisGen.length() - truncate));
         
         return retString;
      }
      else return "Should never see this";
   }
   
   public void propagateNewGeneration()
   {
      String nextGen, temp;
      nextGen = "";
      temp = extremeBit + extremeBit + thisGen
            + extremeBit + extremeBit;
      
      for (int i = 1; i < temp.length() - 1; i++)
      {
         if ((temp.charAt(i - 1)) == '*'
               && temp.charAt(i) == '*'
               && temp.charAt(i + 1) == '*')
         {
            if (rules[7] == true)
               nextGen += "*";
            else nextGen += " ";
         }
         
         else if ((temp.charAt(i - 1)) == '*'
               && temp.charAt(i) == '*'
               && temp.charAt(i + 1) == ' ')
         {
            if (rules[6] == true)
               nextGen += "*";
            else nextGen += " ";
         }
         
         else if ((temp.charAt(i - 1)) == '*'
               && temp.charAt(i) == ' '
               && temp.charAt(i + 1) == '*')
         {
            if (rules[5] == true)
               nextGen += "*";
            else nextGen += " ";
         }
         
         else if ((temp.charAt(i - 1)) == '*'
               && temp.charAt(i) == ' '
               && temp.charAt(i + 1) == ' ')
         {
            if (rules[4] == true)
               nextGen += "*";
            else nextGen += " ";
         }
         
         else if ((temp.charAt(i - 1)) == ' '
               && temp.charAt(i) == '*'
               && temp.charAt(i + 1) == '*')
         {
            if (rules[3] == true)
               nextGen += "*";
            else nextGen += " ";
         }
         
         else if ((temp.charAt(i - 1)) == ' '
               && temp.charAt(i) == '*'
               && temp.charAt(i + 1) == ' ')
         {
            if (rules[2] == true)
               nextGen += "*";
            else nextGen += " ";
         }
         
         else if ((temp.charAt(i - 1)) == ' '
               && temp.charAt(i) == ' '
               && temp.charAt(i + 1) == '*')
         {
            if (rules[1] == true)
               nextGen += "*";
            else nextGen += " ";
         }
         
         else if ((temp.charAt(i - 1)) == ' '
               && temp.charAt(i) == ' '
               && temp.charAt(i + 1) == ' ')
         {
            if (rules[0] == true)
               nextGen += "*";
            else nextGen += " ";
         }
         
         String extremeString = "";
         
         for (int k = 0; k < 3; k++)
         {
            extremeString += extremeBit;
         }
         
         extremeBit = testExtreme(extremeString);
      }
      
      thisGen = nextGen;
   }
   
   //helper for end of powerhouse
   private String testExtreme(String extremeString)
   {
      if ((extremeString.charAt(0)) == ' '
            && extremeString.charAt(1) == ' '
            && extremeString.charAt(2) == ' ')
      {
         if (rules[0] == true)
            return "*";
         else return " ";
      }
      else if ((extremeString.charAt(0)) == '*'
            && extremeString.charAt(1) == '*'
            && extremeString.charAt(2) == '*')
      {
         if (rules[7] == true)
            return "*";
         else return " ";
      }
      
      else return "Should never see";
   }
   
   //helper for toString
   private String extremeLoop(String retString, int extreme)
   {
      for (int i = 0; i < extreme; i++)
         retString += extremeBit;
      
      return retString;
   }
}

/*
PASTE OF RUNS:

Enter Rule (0 - 255): 4
   start
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
                                       *                                       
   end
   
   //testing 126

Enter Rule (0 - 255): 126
   start
                                       *                                       
                                      ***                                      
                                     ** **                                     
                                    *******                                    
                                   **     **                                   
                                  ****   ****                                  
                                 **  ** **  **                                 
                                ***************                                
                               **             **                               
                              ****           ****                              
                             **  **         **  **                             
                            ********       ********                            
                           **      **     **      **                           
                          ****    ****   ****    ****                          
                         **  **  **  ** **  **  **  **                         
                        *******************************                        
                       **                             **                       
                      ****                           ****                      
                     **  **                         **  **                     
                    ********                       ********                    
                   **      **                     **      **                   
                  ****    ****                   ****    ****                  
                 **  **  **  **                 **  **  **  **                 
                ****************               ****************                
               **              **             **              **               
              ****            ****           ****            ****              
             **  **          **  **         **  **          **  **             
            ********        ********       ********        ********            
           **      **      **      **     **      **      **      **           
          ****    ****    ****    ****   ****    ****    ****    ****          
         **  **  **  **  **  **  **  ** **  **  **  **  **  **  **  **         
        ***************************************************************        
       **                                                             **       
      ****                                                           ****      
     **  **                                                         **  **     
    ********                                                       ********    
   **      **                                                     **      **   
  ****    ****                                                   ****    ****  
 **  **  **  **                                                 **  **  **  ** 
****************                                               ****************
*              **                                             **              *
**            ****                                           ****            **
 **          **  **                                         **  **          ** 
****        ********                                       ********        ****
   **      **      **                                     **      **      **   
  ****    ****    ****                                   ****    ****    ****  
 **  **  **  **  **  **                                 **  **  **  **  **  ** 
************************                               ************************
                       **                             **                       
                      ****                           ****                      
                     **  **                         **  **                     
                    ********                       ********                    
                   **      **                     **      **                   
                  ****    ****                   ****    ****                  
                 **  **  **  **                 **  **  **  **                 
                ****************               ****************                
*              **              **             **              **              *
**            ****            ****           ****            ****            **
 **          **  **          **  **         **  **          **  **          ** 
****        ********        ********       ********        ********        ****
   **      **      **      **      **     **      **      **      **      **   
  ****    ****    ****    ****    ****   ****    ****    ****    ****    ****  
 **  **  **  **  **  **  **  **  **  ** **  **  **  **  **  **  **  **  **  ** 
*******************************************************************************
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
*                                                                             *
**                                                                           **
 **                                                                         ** 
****                                                                       ****
   **                                                                     **   
  ****                                                                   ****  
 **  **                                                                 **  ** 
********                                                               ********
       **                                                             **       
      ****                                                           ****      
     **  **                                                         **  **     
    ********                                                       ********    
   end

//testing 130
 * 
Enter Rule (0 - 255): 130
   start
                                       *                                       
                                      *                                        
                                     *                                         
                                    *                                          
                                   *                                           
                                  *                                            
                                 *                                             
                                *                                              
                               *                                               
                              *                                                
                             *                                                 
                            *                                                  
                           *                                                   
                          *                                                    
                         *                                                     
                        *                                                      
                       *                                                       
                      *                                                        
                     *                                                         
                    *                                                          
                   *                                                           
                  *                                                            
                 *                                                             
                *                                                              
               *                                                               
              *                                                                
             *                                                                 
            *                                                                  
           *                                                                   
          *                                                                    
         *                                                                     
        *                                                                      
       *                                                                       
      *                                                                        
     *                                                                         
    *                                                                          
   *                                                                           
  *                                                                            
 *                                                                             
*                                                                              
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
   end
   
   //testing choice, 124
    * 
Enter Rule (0 - 255): 124
   start
                                       *                                       
                                       **                                      
                                       ***                                     
                                       * **                                    
                                       *****                                   
                                       *   **                                  
                                       **  ***                                 
                                       *** * **                                
                                       * *******                               
                                       ***     **                              
                                       * **    ***                             
                                       *****   * **                            
                                       *   **  *****                           
                                       **  *** *   **                          
                                       *** * ****  ***                         
                                       * *****  ** * **                        
                                       ***   ** ********                       
                                       * **  ****      **                      
                                       ***** *  **     ***                     
                                       *   **** ***    * **                    
                                       **  *  *** **   *****                   
                                       *** ** * *****  *   **                  
                                       * ********   ** **  ***                 
                                       ***      **  ****** * **                
                                       * **     *** *    *******               
                                       *****    * ****   *     **              
                                       *   **   ***  **  **    ***             
                                       **  ***  * ** *** ***   * **            
                                       *** * ** ****** *** **  *****           
                                       * ********    *** ***** *   **          
                                       ***      **   * ***   ****  ***         
                                       * **     ***  *** **  *  ** * **        
                                       *****    * ** * ***** ** ********       
                                       *   **   ********   ******      **      
                                       **  ***  *      **  *    **     ***     
                                       *** * ** **     *** **   ***    * **    
                                       * **********    * *****  * **   *****   
                                       ***        **   ***   ** *****  *   **  
                                       * **       ***  * **  ****   ** **  *** 
                                       *****      * ** ***** *  **  ****** * **
                                       *   **     ******   **** *** *    ******
                                       **  ***    *    **  *  *** ****   *     
                                       *** * **   **   *** ** * ***  **  **    
                                       * *******  ***  * ******** ** *** ***   
                                       ***     ** * ** ***      ****** *** **  
                                       * **    ********* **     *    *** ***** 
                                       *****   *       *****    **   * ***   **
                                       *   **  **      *   **   ***  *** **  * 
                                       **  *** ***     **  ***  * ** * ***** **
                                       *** * *** **    *** * ** ********   ****
                                       * ***** *****   * ********      **  *   
                                       ***   ***   **  ***      **     *** **  
                                       * **  * **  *** * **     ***    * ***** 
                                       ***** ***** * *******    * **   ***   **
                                       *   ***   *****     **   *****  * **  **
                                       **  * **  *   **    ***  *   ** ***** * 
                                       *** ***** **  ***   * ** **  ****   ****
                                       * ***   ***** * **  ******** *  **  *  *
                                       *** **  *   ******* *      **** *** ** *
                                       * ***** **  *     ****     *  *** ******
                                       ***   ***** **    *  **    ** * ***     
                                       * **  *   *****   ** ***   ****** **    
                                       ***** **  *   **  **** **  *    *****   
                                       *   ***** **  *** *  ***** **   *   **  
                                       **  *   ***** * **** *   *****  **  *** 
                                       *** **  *   *****  ****  *   ** *** * **
                                       * ***** **  *   ** *  ** **  **** ******
                                       ***   ***** **  ***** ****** *  ***     
                                       * **  *   ***** *   ***    **** * **    
                                       ***** **  *   ****  * **   *  *******   
                                       *   ***** **  *  ** *****  ** *     **  
                                       **  *   ***** ** ****   ** *****    *** 
                                       *** **  *   ******  **  ****   **   * **
                                       * ***** **  *    ** *** *  **  ***  ****
                                       ***   ***** **   **** **** *** * ** *   
                                       * **  *   *****  *  ***  *** *********  
                                       ***** **  *   ** ** * ** * ***       ** 
                                       *   ***** **  ************** **      ***
                                       **  *   ***** *            *****     * *
                                       *** **  *   ****           *   **    ***
                                       * ***** **  *  **          **  ***   *  
                                       ***   ***** ** ***         *** * **  ** 
                                       * **  *   ****** **        * ******* ***
                                       ***** **  *    *****       ***     *** *
                                       *   ***** **   *   **      * **    * ***
                                       **  *   *****  **  ***     *****   *** *
                                       *** **  *   ** *** * **    *   **  * ***
                                       * ***** **  **** *******   **  *** ***  
                                       ***   ***** *  ***     **  *** * *** ** 
                                       * **  *   **** * **    *** * ***** *****
                                       ***** **  *  *******   * *****   ***   *
                                       *   ***** ** *     **  ***   **  * **  *
                                       **  *   *******    *** * **  *** ***** *
                                       *** **  *     **   * ******* * ***   ***
                                       * ***** **    ***  ***     ***** **  *  
                                       ***   *****   * ** * **    *   ***** ** 
                                       * **  *   **  **********   **  *   *****
                                       ***** **  *** *        **  *** **  *   *
                                       *   ***** * ****       *** * ***** **  *
                                       **  *   *****  **      * *****   ***** *
   end

   */