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
    static{inkList.add(new Ink());}

    public PaintInk() {
        super("PaintInk", UC.MAIN_WINDOW_WIDTH, UC.MAIN_WINDOW_HEIGHT);
    }
    public void paintComponent(Graphics g) {
        G.clearBack(g);
        g.setColor(Color.black);inkList.show(g);
        g.setColor(Color.red);Ink.BUFFER.show(g);
        g.drawString("points: " + Ink.BUFFER.n, 600, 30);
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
