package sandbox;

import global.UC;
import graphicsLib.G;
import graphicsLib.Window;
import reactions.Ink;

import java.awt.*;
import java.awt.event.MouseEvent;

public class PaintInk extends Window {
    public static Ink.List inkList = new Ink.List();
    //initialization
//    static{inkList.add(new Ink());}

    public PaintInk() {
        super("PaintInk", UC.MAIN_WINDOW_WIDTH, UC.MAIN_WINDOW_HEIGHT);
    }
    public void paintComponent(Graphics g) {
        G.clearBack(g);
        g.setColor(Color.black);inkList.show(g);
        g.setColor(Color.red);Ink.BUFFER.show(g);
        int n = inkList.size() - 1;
        if(n > 2) {
            int d = inkList.get(n).norn.dist(inkList.get(n-1).norn);
            g.setColor(d < 1000000 ? Color.black : Color.red);
            g.drawString("dist: "+ d, 600, 30);
        }
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
        inkList.add(new Ink());
        repaint();
    }
    public static void main(String[] args) {
        (PANEL = new PaintInk()).launch();
    }
}
