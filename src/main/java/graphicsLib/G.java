package graphicsLib;

import java.awt.*;
import java.util.Random;

public class G {

    // final means 'will never change it', similar to const
    // format hint:
    // Proper: class
    // UPPER: final
    // lowerProper: variable, functions, etc
    public static final Random RANDOM = new Random();
    public static int rnd(int max) {return RANDOM.nextInt(max);} // single line function
    //helper function
    public static Color rndColor() {return new Color(rnd(256), rnd(256), rnd(256));} //red green blue if all of them 0 - black
    //256 256 256 -- red

    //clear the background
    //Mac and PC runs different
    public static void clearBack (Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, 5000,5000);
    }

    //---------nested class V------------
    public static class V{
        public int x, y;
        //constructor
        public V(int x, int y){set(x,y);}
        public void set(int x, int y) {this.x = x; this.y = y;}
        //make the data element private; make function public
        public void add(V v){x += v.x; y += v.y;}
    }

    public static class VS{
       public V loc, size;
       public VS(int x, int y, int w, int h) {
           loc = new V(x, y);
           size = new V(w, h);
       }

       public void fill(Graphics g, Color c) {
           g.setColor(c);
           g.fillRect(loc.x, loc.y, size.x, size.y);
       }
       //hit detection
       public boolean hit(int x, int y) {
           return (loc.x <= x && loc.y <= y && x <= (loc.x + size.x) && y <= (loc.y + size.y));
       }

       public int xL() {return loc.x;}
       public int xH() {return loc.x + size.x;}
       //public int
       public int yL() {return loc.y;}
       public int yH() {return loc.y + size.y;}
       //public int

    }

    public static class LoHi{
        public int lo, hi;
    }

    //bounding box
    public static class BBOX{
        public LoHi H, V;
    }
    //Pole line whole long list of x y z
    public static class PL{

    }
}