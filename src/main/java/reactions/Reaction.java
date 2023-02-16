package reactions;

import global.I;
import global.UC;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//we are not really implementing in this abstract class
public abstract class Reaction implements I.React{
    private static Map byShape = new Map();
    public static List initialReactions = new List(); //used by undo to restart
    public Shape shape;
    //constructor
    public Reaction(String shapeName) {
        shape = Shape.DB.get(shapeName);
        if(shape == null) {
            System.out.println("WT ? shape DB does not contain "+shapeName);
        }
    }

    public void enable() {
        System.out.println("enable");
        List list = byShape.getList(shape);
        //this = one single reaction
        if(!list.contains(this)) {list.add(this);}
    }
    public void disable() {
        List list = byShape.getList(shape);
        list.remove(this);
    }
    //best reaction
    public static Reaction best(Gesture g) {//can return NULL
        return byShape.getList(g.shape).loBid(g);
    }

    public static void nuke() {//for undo
        byShape.clear();
        initialReactions.enable();
    }
    //------------------List---------------------
    public static class List extends ArrayList<Reaction> {
        public void addReaction(Reaction r) {add(r);r.enable();}
        public void removeReaction(Reaction r) {remove(r);r.disable();}
        public void clearAll() {
            for(Reaction r: this) {r.disable();}
            this.clear();
        }
        public Reaction loBid(Gesture g) {//can return NULL
            //reaction is like the marketplace
            Reaction res = null;
            int bestSoFar = UC.noBid;
            System.out.println("loBid: " + bestSoFar);
            for(Reaction r: this) {
                int b = r.bid(g);
                if(b < bestSoFar) {bestSoFar = b;res = r;}
            }
            return res;
        }

        public void enable() {
            for(Reaction r: this) {
                r.enable();
            }
        }
    }
    //------------------Map------------------------
    public static class Map extends HashMap<Shape,List> {
        // forced to return List
        public List getList(Shape s) {
            List res = get(s);
            if(res == null) {
                res = new List();
                put(s,res);
            }
            return res;
        }
    }
}
