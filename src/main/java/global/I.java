package global;

import reactions.Gesture;

import java.awt.*;

public interface I {
    //-----nested interface---------
    public interface Draw {public void draw(Graphics g);}
    public interface Hit {public boolean hit(int x, int y);}
    //Area interface is very common in graphics project
    public interface Area extends Hit{
        public void dn(int x, int y);
        public void up(int x, int y);
        public void drag(int x, int y);
    }
    //show is a better name than draw in this project
    public interface Show {public void show(Graphics g);}
    public interface Act {public void act(Gesture g);}
    //react has the same function as act
    public interface React extends Act{public int bid(Gesture g);}

}

