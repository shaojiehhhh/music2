package reactions;

import global.UC;
import graphicsLib.G;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.stream.Collectors;

public class Shape implements Serializable {
    public static Shape bestMatch;
    public static DataBase DB = (DataBase) DataBase.Load(); //database
    public static Shape DOT = DB.get("DOT");
    public static Collection<Shape> LIST = DB.values();
    public Prototype.List prototypes = new Prototype.List();
    public String name;

    //constructor
    public Shape(String name) {this.name = name;}
//    public static HashMap<String, Shape> LoadShapeDB(String fileName) {
//        HashMap<String, Shape> res = new HashMap<>();
//        res.put("DOT", new Shape("DOT")); //make sure we get DOT in the DB
//        try{
//            System.out.println("Attempting DB load...");
//            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName));
//            res = (HashMap<String, Shape>) ois.readObject();
//            System.out.println("Successfully load - found" + res.keySet()) ;
//            ois.close();
//        } catch (Exception e) {
//            System.out.println("load failed");
//            System.out.println(e);
//        }
//        return res;
//    }
//    public static void saveShapeDB(String fileName) {
//        try{
//            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName));
//            oos.writeObject(DB);
//            System.out.println("Saved" + fileName) ;
//            oos.close();
//        } catch (Exception e) {
//            System.out.println("failed DB save");
//            System.out.println(e);
//        }
//    }

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
    public static class Prototype extends Ink.Norn implements Serializable{
        public int nBlend = 1; //average element blended together
        public void blend(Ink.Norn norm) {blend(norm, nBlend);nBlend ++;}

        //--------List of Prototype-----------
        public static class List extends ArrayList<Prototype> implements Serializable{
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

            public int hitProto(int x, int y) {
                if(y < m || x < m || y > m+w) return -1;
                int res = (x-m)/(m+w);
                return res < size() ? res : -1;
            }
            public void train(Ink.Norn norm) {
                if(bestDist(norm) < UC.noMatchDist) { //found match->blend
                    bestMatch.blend(norm);
                } else {
                    add(new Shape.Prototype());
                }
            }
        }
    }
    //-----------------DataBase----------------------------
    public static class DataBase extends HashMap<String, Shape> {
        public DataBase() {
            super();
            String DOT = "DOT";
            put(DOT, new Shape(DOT));
        }

        public static HashMap<String, Shape> Load() {
           DataBase res = null;
//            res.put("DOT", new Shape("DOT")); //make sure we get DOT in the DB
            try{
                System.out.println("Attempting DB load...");
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(UC.shapeDBFILENAME));
                res = (DataBase) ois.readObject();
                System.out.println("Successfully load - found" + res.keySet()) ;
                ois.close();
            } catch (Exception e) {
                System.out.println("load failed");
                System.out.println(e);
                res = new DataBase();
                res.put("DOT", new Shape("DOT"));
            }
            return res;
        }

        public static void save() {
            try{
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(UC.shapeDBFILENAME));
                oos.writeObject(DB);
                System.out.println("Saved" + UC.shapeDBFILENAME) ;
                oos.close();
            } catch (Exception e) {
                System.out.println("failed DB save");
                System.out.println(e);
            }
        }
        public Shape forcedGet(String name) {
            if(!DB.containsKey(name)) {
                DB.put(name, new Shape(name));
            }
            return DB.get(name);
        }
        public void train(String name, Ink.Norn norm) {
            if(isLegal(name)) {
                forcedGet(name).prototypes.train(norm);
            }
        }

        public static boolean isLegal(String name) {
            return !name.equals("") && !name.equals("DOT");
        }
    }
}
