package reactions;

import global.I;

import java.awt.*;
import java.util.ArrayList;

public class Ink implements I.Show{
    @Override
    public void show(Graphics g) {
        g.setColor(Color.red);
        g.fillRect(100,100,100,100);
    }

    //-----------------List--------------------------------------------
    public static class List extends ArrayList<Ink> implements I.Show {

        @Override
        public void show(Graphics g) {
            for(Ink ink: this) {ink.show(g);}
        }
    }
}
