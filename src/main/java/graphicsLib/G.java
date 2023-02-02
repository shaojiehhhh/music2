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
        public static Transform T = new Transform(); //single Transform project
        //constructor
        public V(int x, int y){set(x,y);}
        public void set(int x, int y) {this.x = x; this.y = y;}
        //make the data element private; make function public
        public void add(V v){x += v.x; y += v.y;}
        public void set(V v) {this.x = v.x; this.y = v.y;}
        public void setT(V v) {set(v.tx(), v.ty());} //transform x and y
        public int tx() {return x * T.n / T.d + T.dx;}
        public int ty() {return y * T.n / T.d + T.dy;}

        //----------Transform--------------
        public static class Transform {
            int n, d, dx, dy;
            //old box, new VS
            public void set(VS oVS, VS nVS) {
                setScale(oVS.size.x, oVS.size.y, nVS.size.x, nVS.size.y);
                dx = setOff(oVS.loc.x, oVS.size.x, nVS.loc.x, nVS.size.x);
                dy = setOff(oVS.loc.y, oVS.size.y, nVS.loc.y, nVS.size.y);
            }
            public void set(BBOX bbox, VS nVS) {
                setScale(bbox.h.size(), bbox.v.size(), nVS.size.x, nVS.size.y);
                dx = setOff(bbox.h.lo, bbox.h.size(), nVS.loc.x, nVS.size.x);
                dy = setOff(bbox.v.lo, bbox.v.size(), nVS.loc.y, nVS.size.y);
            }
            //old width, old height, new width, new height
            public void setScale(int oW, int oH, int nW, int nH) {
                n = (nW > nH) ? nW : nH;
                d = (oW > oH) ? oW : oH;
            }
            //old & new locX
            public int setOff(int oX, int oW, int nX, int nW) {
                return (-oX - oW/2) * n/d  + nX + nW/2;
            }
        }
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
       //public int xH {return loc.x + size.x/2;}
       public int yL() {return loc.y;}
       public int yH() {return loc.y + size.y;}
       //public int yH() {return loc.y + size.y/2;}

    }

    public static class LoHi{
        public int lo, hi;
        public LoHi(int min, int max) {lo = min; hi = max;}
        public void add(int v) {if(v < lo) {lo = v;}if(v > hi) {hi = v;}}
        public void set(int v) {lo = v;hi = v;}
        public int size(){return (hi-lo) > 0 ? (hi-lo) : 1;}
    }

    //bounding box
    public static class BBOX{
        public LoHi h, v;
        public BBOX(){
            h = new LoHi(0,0);
            v = new LoHi(0,0);
        }
        public void set(int x, int y) {
            h.set(x);
            v.set(y);
        }
        public void add(int x, int y) {h.add(x);v.add(y);}

        public void add(V v) {h.add(v.x);this.v.add(v.y);} //add a single Vector E

        public VS getNewVS() {
            return new VS(h.lo, v.lo, h.size(), v.size());
        }
        public void draw(Graphics g) {
            g.drawRect(h.lo, v.lo, h.size(), v.size());
        }
    }

    //Pole line whole long list of x y z
    public static class PL {
        public V[] points;

        public PL(int n) {
            points = new V[n];
            for (int i = 0; i < n; i++) {points[i] = new V(0, 0);}
        }

        public int size() {return points.length;}
        public void transform() {
            for(int i = 0; i < points.length; i ++) {
                points[i].setT(points[i]); //transform
            }
        }

        public void drawN(Graphics g, int n) {
            for (int i = 1; i < n; i++) {
                g.drawLine(points[i-1].x, points[i - 1].y, points[i].x, points[i].y);
            }
        }
        public void drawNDots(Graphics g, int n) {
            for (int i = 0; i < n; i++) {
                g.drawOval(points[i].x - 1, points[i].y - 1, 3, 3);
            }
        }

        public void drawDots(Graphics g) {drawNDots(g, size());
        }
        public void draw(Graphics g) {drawN(g, size());}

    }

}


