package sandbox;
import java.awt.Graphics;
import java.awt.Color;
import global.UC;
import graphicsLib.G;
import graphicsLib.Window;
import reactions.Ink;
import reactions.Shape;

import java.awt.*;
import java.awt.event.MouseEvent;

public class PaintInk extends Window {
    public static Ink.List inkList = new Ink.List();
    public static Shape.Prototype.List pList = new Shape.Prototype.List();
    //initialization
//    static{inkList.add(new Ink());}

    public PaintInk() {
        super("PaintInk", UC.MAIN_WINDOW_WIDTH, UC.MAIN_WINDOW_HEIGHT);
    }
    public void paintComponent(Graphics g) {
        G.clearBack(g);
        g.setColor(Color.black);inkList.show(g);
        g.setColor(Color.red);Ink.BUFFER.show(g);
//        int n = inkList.size() - 1;
//        if(n > 2) {
//            int d = inkList.get(n).norn.dist(inkList.get(n-1).norn);
//            g.setColor(d < 1000000 ? Color.black : Color.red);
//            g.drawString("dist: "+ d, 600, 30);
//        }
        pList.show(g);
    }

    public void mousePressed(MouseEvent me) {
        Ink.BUFFER.dn(me.getX(), me.getY());
        repaint();
    }

    public void mouseDragged(MouseEvent me) {
        Ink.BUFFER.drag(me.getX(), me.getY());
        repaint();
    }
    public void mouseReleased(MouseEvent me) {
        Ink ink = new Ink();
        Shape.Prototype proto;
        inkList.add(ink);
        if(pList.bestDist(ink.norn) < UC.noMatchDist) {
            proto = Shape.Prototype.List.bestMatch;
            proto.blend(ink.norn);
        } else {
            proto = new Shape.Prototype();
            pList.add(proto);
        }
        ink.norn = proto;
        repaint();
    }
    public static void main(String[] args) {
        (PANEL = new PaintInk()).launch();
    }
}
