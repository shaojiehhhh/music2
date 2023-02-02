package reactions;

import global.I;
import global.UC;
import graphicsLib.G;

import java.awt.*;
import java.nio.Buffer;
import java.util.ArrayList;

public class Ink implements I.Show {
    public static Buffer BUFFER = new Buffer();
//    public static G.VS temp = new G.VS(100, 100, 100, 100); //temp coordinate system
    public Norn norn;
    public G.VS vs;

    public Ink(){
        norn = new Norn();
        vs = BUFFER.bbox.getNewVS();
    }
    @Override
    public void show(Graphics g) {
        g.setColor(UC.ink_color);
        norn.drawAt(g, vs);
    }
    //-----------------NORN-------------------
    public static class Norn extends G.PL{
        public static final int N = UC.nornSampleSize, MAX = UC.nornCoordMax;
        public static final G.VS nornCoordSystem = new G.VS(0,0,MAX,MAX);
        public Norn() {
            super(N);
            BUFFER.subSample(this);
            G.V.T.set(BUFFER.bbox, nornCoordSystem);
            transform();
        }
        public void drawAt(Graphics g, G.VS vs) {
            G.V.T.set(nornCoordSystem, vs);
            for(int i = 1; i < N; i++) {
                g.drawLine(points[i-1].tx(), points[i-1].ty(), points[i].tx(), points[i].ty());
            }
        }
        public int dist(Norn n) {
            int res = 0;
            for(int i = 0; i <  N; i++) {
                int dx = points[i].x - n.points[i].x;
                int dy = points[i].y - n.points[i].y;
                res += dx * dx + dy * dy;
            }
            return res;
        }
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

        public void subSample(G.PL pl) {
            int K = pl.size();
            for(int i = 0; i < K; i++) {
                int j = i * (n - 1) / (K - 1); // j is the index in the buffer
                pl.points[i].set(points[j]); //copy value
            }
        }
    }

    //-----------------List--------------------------------------------
    public static class List extends ArrayList<Ink> implements I.Show {
        @Override
        public void show(Graphics g) {
            for(Ink ink: this) {ink.show(g);}
        }
    }
}
