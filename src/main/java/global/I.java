package global;

import java.awt.*;

public interface I {
    //-----nested interface---------
    public interface Draw {public void draw(Graphics g);}
    public interface Hit {public boolean hit(int x, int y);}
}

