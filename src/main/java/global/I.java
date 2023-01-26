package global;

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
}

