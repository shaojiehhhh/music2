package sandbox;

import graphicsLib.G;
import graphicsLib.Window;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Squares extends Window {
    //public static G.VS theVS = new G.VS(100, 100, 200, 300);
    public static Color theColor = G.rndColor();
    public static Square.List theList = new Square.List();
    public static Square theSquare;
    public static boolean dragging = false;
    public static G.V mouseDelta = new G.V(0, 0);

    public Squares() {
        super("squares", 1000, 700);
    }

    @Override
    public void paintComponent(Graphics g) {
        //random color every time
        G.clearBack(g);
        //theVS.fill(g, theColor);
        theList.draw(g);
    }

    @Override
    public void mousePressed(MouseEvent me) {
//        if(theVS.hit(me.getX(), me.getY())) {
//            theColor = G.rndColor();
//        }
        int x = me.getX(), y = me.getY();
        theSquare = theList.hit(x, y);
        if(theSquare == null) {
            dragging = false; //we are not going to dragging
            theSquare = new Square(x, y);
            theList.add(theSquare);
        } else {
            dragging = true;
            mouseDelta.set(theSquare.loc.x - x, theSquare.loc.y - y);
        }
        //need to write repaint() or else the OS doesn't know unless you resize the window
        repaint();
    }

    public void mouseDragged(MouseEvent me) {
        int x = me.getX(), y = me.getY();
        if(dragging) {
            theSquare.move(x + mouseDelta.x, y + mouseDelta.y);
        } else {
            theSquare.resize(x, y);
        }
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
        public void resize(int x, int y) {if(x > loc.x && y > loc.y) {size.set(x - loc.x, y - loc.y);}}

        public void move(int x , int y) {loc.set(x, y);}

        //-----------another nested class----ListSquare-----
        public static class List extends ArrayList<Square> {
            public void draw(Graphics g) {
                for (Square s : this) {
                    //s.c not c
                    s.fill(g,s.c);
                }
            }
            public Square hit(int x, int y) {
                Square res = null; //if not hitting anything return null
                for(Square s: this) {
                    if(s.hit(x, y)) {
                        res = s;
                    }
                }
                return res;
            }
        }

    }
}
