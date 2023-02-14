package reactions;

import global.I;
import graphicsLib.G;

public class Gesture {
    public Shape shape;
    public G.VS vs;

    private Gesture(Shape shape, G.VS vs) {
        this.shape = shape;
        this.vs = vs;
    }
    public static Gesture getNew(Ink ink) {//can return null
        Shape s = Shape.recognize(ink);
        return s == null ? null : new Gesture(s,ink.vs);
    }
    public static I.Area AREA = new I.Area() {
        public void dn(int x, int y) {Ink.BUFFER.dn(x, y);}
        public void up(int x, int y) {
            Ink.BUFFER.add(x, y);
            Ink ink = new Ink();
            Gesture gesture = Gesture.getNew(ink);//can fail
            Ink.BUFFER.clear();
            if(gesture != null) {
                Reaction r = Reaction.best(gesture);//can fail
                if(r != null) {r.act(gesture);}
                //TODO: need to be tested
            }

        }
        public void drag(int x, int y) {Ink.BUFFER.drag(x, y);}
        public boolean hit(int x, int y) {return true;}

    };
}
