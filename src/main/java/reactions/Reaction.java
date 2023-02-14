package reactions;

import global.I;
import global.UC;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//we are not really implementing in this abstract class
public abstract class Reaction implements I.React{
    private static Map byShape = new Map();
    public Shape shape;
    public void enable() {
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
            for(Reaction r: this) {
                int b = r.bid(g);
                if(b < bestSoFar) {bestSoFar = b;res = r;}
            }
            return res;
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
