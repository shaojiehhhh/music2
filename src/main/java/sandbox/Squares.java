package sandbox;

import global.I;
import global.UC;
import graphicsLib.G;
import graphicsLib.Spline;
import graphicsLib.Window;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import static javax.swing.text.html.HTML.Tag.I;

public class Squares extends Window implements ActionListener {
    //public static G.VS theVS = new G.VS(100, 100, 200, 300);
    //public static Color theColor = G.rndColor();
    public static Square.List theList = new Square.List();
    public static Square theSquare;
    public static boolean dragging = false;
    public static G.V mouseDelta = new G.V(0, 0);
    public static Timer timer;
    public static G.V pressedLoc = new G.V(0, 0);
    public static final int WIDTH = UC.MAIN_WINDOW_WIDTH, HEIGHT = 700;
    public static I.Area curArea;
    public static Square BACKGROUND = new Square(0,0) {
        public void dn(int x, int y) {theSquare =  new Square(x, y); theList.add(theSquare);}
        public void drag(int x, int y) {theSquare.resize(x, y);}
    };
    static {BACKGROUND.c = Color.white; BACKGROUND.size.set(5000, 5000); theList.add(BACKGROUND);};

    public Squares() {
        super("squares", WIDTH, HEIGHT);
        timer = new Timer(30,this);
        timer.setInitialDelay(5000);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        //random color every time
        G.clearBack(g);
        //theVS.fill(g, theColor);
        theList.draw(g);
        if(theList.size() > 3) {
            Spline.pSpline(g,theList.get(1).loc, theList.get(2).loc, theList.get(3).loc, 5);
        }
    }

    @Override
    public void mousePressed(MouseEvent me) {
//        if(theVS.hit(me.getX(), me.getY())) {
//            theColor = G.rndColor();
//        }
        int x = me.getX(), y = me.getY();
        //define which area got hit
        curArea = theList.hit(x, y);
        curArea.dn(x, y);
        //need to write repaint() or else the OS doesn't know unless you resize the window
        repaint();
    }

    public void mouseDragged(MouseEvent me) {
        int x = me.getX(), y = me.getY();
        curArea.drag(x, y);
        repaint();
    }

    public void mouseReleased (MouseEvent me) {
        if(dragging) {
            theSquare.dv.set(me.getX() - pressedLoc.x, me.getY() - pressedLoc.y);
        }
    }

    public static void main(String[] args) {
        (PANEL = new Squares()).launch();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    //---------Square-------nested class
    //order: non-static functions, main function, static function

    public static class Square extends G.VS implements I.Draw, I.Area{
        //if static Color c, the new square color would always keep the same
        public Color c = G.rndColor();
        public G.V dv = new G.V(0,0);

        //public G.V dv = new G.V(G.rnd(20)-10, G.rnd(20)-10);//max value is 20
        Square(int x, int y) {
            super(x, y, 100, 100);
        }
        public void draw(Graphics g) {
            fill(g, c);  //draw
            moveAndBounce(); //move the box(old-school animation) calculate the next step
        }
        public void resize(int x, int y) {if(x > loc.x && y > loc.y) {size.set(x - loc.x, y - loc.y);}}

        public void move(int x , int y) {loc.set(x, y);}

        public void moveAndBounce() {
            loc.add(dv);
            if(xL() < 0 && dv.x < 0) {dv.x = -dv.x;}
            if(xH() > 1000 && dv.x > 0) {dv.x = -dv.x;}
            if(yL() < 0 && dv.y < 0) {dv.y = -dv.y;}
            if(yH() > 700 && dv.x > 0) {dv.y = -dv.y;}
        }

        @Override
        public void dn(int x, int y) {
            mouseDelta.set(loc.x - x, loc.y - y);
        }

        @Override
        public void up(int x, int y) {}

        @Override
        public void drag(int x, int y) {
            loc.set(mouseDelta.x + x, mouseDelta.y + y);
        }

        //-----------another nested class----ListSquare-----
        public static class List extends ArrayList<Square> {
            public void draw(Graphics g) {
                for (Square s : this) {
                    //s.c not c
                    s.draw(g);
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
