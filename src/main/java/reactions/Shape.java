package reactions;

import global.UC;
import graphicsLib.G;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.stream.Collectors;

public class Shape {
    public static Shape bestMatch;
    public static HashMap<String, Shape> DB = LoadShapeDB(); //database
    public static Shape DOT = DB.get("DOT");
    public static Collection<Shape> LIST = DB.values();
    public Prototype.List prototypes = new Prototype.List();
    public String name;

    //constructor
    public Shape(String name) {this.name = name;}
    public static HashMap<String, Shape> LoadShapeDB() {
        HashMap<String, Shape> res = new HashMap<>();
        res.put("DOT", new Shape("DOT")); //make sure we get DOT in the DB
        return res;
    }
    public static void saveShapeDB() {}
    public static Shape recognize(Ink ink) {// can return null
        if(ink.vs.size.x < UC.dotThreshold && ink.vs.size.y < UC.dotThreshold) {return DOT;}
        Shape.bestMatch = null;
        int bestSoFar = UC.noMatchDist;
        for(Shape s: LIST) {
            int d = s.prototypes.bestDist(ink.norn);
            if(d < bestSoFar) {bestMatch= s;bestSoFar = d;}
        }
        return bestMatch;
    }

    //-----------Prototype----------
    public static class Prototype extends Ink.Norn {
        public int nBlend = 1; //average element blended together
        public void blend(Ink.Norn norm) {blend(norm, nBlend);nBlend ++;}

        //--------List of Prototype-----------
        public static class List extends ArrayList<Prototype> {
            public static Prototype bestMatch; //set by side effect by min/max dist
            public int bestDist(Ink.Norn norm) {//does not have to exist
                bestMatch = null; //assume no match
                int res = UC.noMatchDist;
                for(Prototype p: this) {
                    int d = p.dist(norm);
                    if(d < res) {bestMatch = p; res = d;}
                }
                return res;
            }
            private static int m = 10, w = 60; //margin & width
            private static G.VS showBox = new G.VS(m, m, w,w);
            public void show(Graphics g) {
                g.setColor(Color.orange);
                for(int i = 0; i < size(); i++) {
                    Prototype p = get(i);
                    int x = m + i*(m+w);
                    showBox.loc.set(x, m);
                    p.drawAt(g, showBox);
                    g.drawString(""+p.nBlend, x, 20);
                }
            }
        }

    }

}
