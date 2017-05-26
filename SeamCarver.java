import java.awt.Color;
import java.lang.IllegalArgumentException;
import java.lang.IndexOutOfBoundsException;
import java.lang.Math;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import edu.princeton.cs.algs4.Picture;

public class SeamCarver {

   private Picture pic;
   private int width;
   private int height;

   //Constructor
   public SeamCarver(Picture picture) {
      pic = new Picture(picture);
      width = pic.width();
      height = pic.height();
   }

   //The picture contained in the sc data structure.
   public Picture picture() {
      return pic;
   }

   //Width of the picture
   public int width() {
      return width;
   }

   //Height of the picture
   public int height() {
      return height;
   }

   //Returns the energy of a pixel, given its coordinates.
   //Energy is a calculation of the contrast between a pixel's RGB values
   // and its neighboring pixels' RGB values.
   public double energy(int x, int y) {
      if (x < 0 || x >= width() || y < 0 || y >= height())
         throw new IndexOutOfBoundsException();
      
      if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1)
      {
         return Math.pow(255.0, 2) * 3;
      }

      double deltaX = 0.0, deltaY = 0.0;
      Color x1, x2, y1, y2;
      x1 = pic.get(x - 1, y);
      x2 = pic.get(x + 1, y);
      y1 = pic.get(x, y - 1);
      y2 = pic.get(x, y + 1);
      deltaX = Math.pow((x1.getRed() - x2.getRed()), 2) + Math.pow((x1.getGreen() - x2.getGreen()), 2) + Math.pow((x1.getBlue() - x2.getBlue()), 2);
      deltaY = Math.pow((y1.getRed() - y2.getRed()), 2) + Math.pow((y1.getGreen() - y2.getGreen()), 2) + Math.pow((y1.getBlue() - y2.getBlue()), 2);
      return deltaX + deltaY;
   }

   private int[] getSeam(String mode, HashMap edgeTo, String end) {
      int size;
      if (mode.equals("h"))
         size = width();
      else if (mode.equals("v"))
         size = height();
      else
         throw new IllegalArgumentException();

      int[] path = new int[size];
      String cur = end;

      while (size > 0) {
         path[--size] = str2id(mode, cur);
         cur = (String)edgeTo.get(cur);
      }
      // path represents the seam as a 1D array of the coordinates in the seam.
      //y-coordinates are stored if the seam traverses horizontally.
      //x-coordinates are stored if the seam traverses vertically.
      return path;
   }

   private String id2str(int col, int row) {
      return col + " " + row;
   }

   private int str2id(String mode, String str) {
      if (mode.equals("v"))
         return Integer.parseInt(str.split(" ")[0]);
      else if (mode.equals("h"))
         return Integer.parseInt(str.split(" ")[1]);
      else
         throw new IllegalArgumentException();
   }

   // Loops through indices to find horizontal seam.
   public int[] findHorizontalSeam() {
      String mode = "h";
      HashMap<String, String> edgeTo = new HashMap<String, String>();
      HashMap<String, Double> energyTo = new HashMap<String, Double>();
      double cost = Double.MAX_VALUE;
      //cur represents the current pixel.
      //next represents a potential pixel to connect cur to.
      String cur, next, end = null;

      for (int col = 0; col < width() - 1; col++)
         for (int row = 0; row < height(); row++) {
         
            cur = id2str(col, row);
            if (col == 0) {
               edgeTo.put(cur, null);
               energyTo.put(cur, energy(col, row));
            }
            for (int i = row - 1; i <= row + 1; i++)
               if (i >= 0 && i < height()) {
                  next = id2str(col + 1, i);
                  double newEng = energy(col + 1, i) + energyTo.get(cur);
                  //If we don't have a next edge yet, add one. Or, if this edge
                  // is better than the one we have, use it.
                  if (energyTo.get(next) == null || newEng < energyTo.get(next)) {
                  
                     edgeTo.put(next, cur);
                     energyTo.put(next, newEng);

                     //End at the second to last column, because 'next' inolves
                     // the next column.
                     if (col + 1 == width() - 1 && newEng < cost) {
                        cost = newEng;
                        end = next;
                     }
                  }
               }
         }    
      return getSeam(mode, edgeTo, end);
   }

   // Loops through indices to find vertical seam.
   public int[] findVerticalSeam() {
      //See comments in findHorizontalSeam() for equivalent explanations.
      String mode = "v";
      HashMap<String, String> edgeTo = new HashMap<String, String>();
      HashMap<String, Double> energyTo = new HashMap<String, Double>();
      double cost = Double.MAX_VALUE;
      String cur, next, end = null;

      for (int row = 0; row < height() - 1; row++)
         for (int col = 0; col < width(); col++) {
         
            cur = id2str(col, row);
            if (row == 0) {
               edgeTo.put(cur, null);
               energyTo.put(cur, energy(col, row));
            }
            for (int k = col - 1; k <= col + 1; k++)
               if (k >= 0 && k < width()) {
                  next = id2str(k, row + 1);
                  double newEng = energy(k, row + 1) + energyTo.get(cur);
                  if (energyTo.get(next) == null || newEng < energyTo.get(next)) {
                  
                     edgeTo.put(next, cur);
                     energyTo.put(next, newEng);
                     if (row + 1 == height() - 1 && newEng < cost) {
                        cost = newEng;
                        end = next;
                     }
                  }
               }
         }
      return getSeam(mode, edgeTo, end);
   }

   private boolean isValidSeam(int[] seam) {
      for (int i = 0; i < seam.length - 1; i++) {
         if (Math.abs(seam[i] - seam[i + 1]) > 1) {
            return false;
         }
      }
      return true;
   }

   // Removes horizontal seam from picture.
   public void removeHorizontalSeam(int[] seam) {
      if (width() <= 1 || height() <= 1 || seam.length < 0 || seam.length >width() || !isValidSeam(seam))
         throw new IllegalArgumentException();

      Picture newPic = new Picture(width(), height() - 1);

      for (int col = 0; col < width(); col++)
         for (int row = 0; row < height() - 1; row++) {

            if (row < seam[col])
               newPic.set(col, row, pic.get(col, row));
            else
               newPic.set(col, row, pic.get(col, row + 1));

         }

      height--;
      pic = new Picture(newPic);
   }

   // Removes vertical seam from picture.
   public void removeVerticalSeam(int[] seam) {
      if (width() <= 1 || height() <= 1 || seam.length < 0 || seam.length > height() || !isValidSeam(seam))
         throw new IllegalArgumentException();

      Picture newPic = new Picture(width() - 1, height());

      for (int row = 0; row < height(); row++)
         for (int col = 0; col < width() - 1; col++) {
            
            if (col < seam[row])
               newPic.set(col, row, pic.get(col, row));
            else
               newPic.set(col, row, pic.get(col + 1, row));
       }

      width--;
      pic = new Picture(newPic);
   }

   public static void main(String args[]) {
      Picture inputImg = new Picture(args[0]);
      SeamCarver sc = new SeamCarver(inputImg);
      inputImg.show();

      while (sc.width > 250)
      {
         System.out.println("resizing... Currently at width " + sc.width());
         int[] seam = sc.findVerticalSeam();
         sc.removeVerticalSeam(seam);
      }

      sc.picture().show();
   }
}
