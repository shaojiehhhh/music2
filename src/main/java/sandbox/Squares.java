package sandbox;

import graphicsLib.G;
import graphicsLib.Window;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Squares extends Window {
    public static G.VS theVS = new G.VS(100, 100, 200, 300);
    public static Color theColor = G.rndColor();
    public static Square.List theList = new Square.List();

    public Squares() {
        super("squares", 1000, 700);
    }

    @Override
    public void paintComponent(Graphics g) {
        //random color every time
        G.clearBack(g);
        theVS.fill(g, theColor);
        theList.draw(g);
    }

    @Override
    public void mousePressed(MouseEvent me) {
//        if(theVS.hit(me.getX(), me.getY())) {
//            theColor = G.rndColor();
//        }
        theList.add(new Square(me.getX(), me.getY()));
        //need to write repaint() or else the OS doesn't know unless you resize the window
        repaint();
    }

    public static void main(String[] args) {
        (PANEL = new Squares()).launch();
    }

    //---------Square-------nested class
    //order: non-static functions, main function, static function

    public static class Square extends G.VS {
        //if static Color c, the new square color would always keep the same
        public Color c = G.rndColor();
        Square(int x, int y) {
            super(x, y, 100, 100);
        }

        //-----------another next class----ListSquare-----
        public static class List extends ArrayList<Square> {
            public void draw(Graphics g) {
                for (Square s : this) {
                    //s.c not c
                    s.fill(g,s.c);
                }
            }
        }

    }
}
