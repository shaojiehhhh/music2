package sandbox;

import global.UC;
import graphicsLib.G;
import graphicsLib.Window;
import reactions.Ink;

import java.awt.*;

public class PaintInk extends Window {
    public static Ink.List inkList = new Ink.List();
    //initialization
    static{inkList.add(new Ink());}

    public PaintInk() {
        super("PaintInk", UC.MAIN_WINDOW_WIDTH, UC.MAIN_WINDOW_HEIGHT);
    }
    public void paintComponent(Graphics g) {
        G.clearBack(g);
        inkList.show(g);
        //repaint();
    }
    public static void main(String[] args) {
        (PANEL = new PaintInk()).launch();
    }
}
