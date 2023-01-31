package reactions;

import global.I;
import global.UC;
import graphicsLib.G;

import java.awt.*;
import java.nio.Buffer;
import java.util.ArrayList;

public class Ink extends G.PL implements I.Show {
    public static Buffer BUFFER = new Buffer();

    public Ink(){
        super(BUFFER.n);
        for(int i = 0; i < BUFFER.n; i++) {
            points[i].set(BUFFER.points[i]);
        }
    }
    @Override
    public void show(Graphics g) {
        draw(g);
    }
    //-----------------Buffer-----------------
    public static class Buffer extends G.PL implements I.Show, I.Area{
        //max size of buffer
        public static final int MAX = UC.ink_Buffer_Max;
        //how many points in that buffer
        public int n;
        public G.BBOX bbox = new G.BBOX();
        //constructor
        private Buffer() {super(MAX);}

        public void add(int x, int y) {if(n < MAX) {points[n].set(x, y); bbox.add(x, y);n++;}}

        public void clear() {n = 0;}

        @Override
        public boolean hit(int x, int y) {return true;}

        @Override
        public void dn(int x, int y) {clear();add(x, y); bbox.set(x, y);}

        @Override
        public void up(int x, int y) {}

        @Override
        public void drag(int x, int y) {add(x, y);}

        @Override
        public void show(Graphics g) {drawNDots(g, n); bbox.draw(g);}
    }

    //-----------------List--------------------------------------------
    public static class List extends ArrayList<Ink> implements I.Show {
        @Override
        public void show(Graphics g) {
            for(Ink ink: this) {ink.show(g);}
        }
    }
}
