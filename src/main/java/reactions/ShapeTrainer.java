package reactions;

import graphicsLib.G;
import graphicsLib.Window;

import java.awt.*;
import java.awt.event.KeyEvent;

public class ShapeTrainer extends Window {
    public static String UNKNOWN = " <- this name is currently unknown.";
    public static String ILLEGAL = " <- this name is NOT a legal shape name.";
    public static String KNOWN = " <- this name is an known shape.";
    public static String curName = "";
    public static String curState = ILLEGAL;

    public ShapeTrainer() {
        super("ShapeTrainer", 1000, 700);
    }
    public void setState(){
        curState = (curName.equals("") || curName.equals("DOT") ? ILLEGAL : UNKNOWN);
    }

    public static void main(String[] args) {
        (PANEL = new ShapeTrainer()).launch();
    }
    public void paintComponent(Graphics g) {
        G.clearBack(g);
        g.setColor(Color.black);
        g.drawString(curName, 600, 30);
        g.drawString(curState, 700, 30);

    }
    public void keyTyped(KeyEvent ke) {
        char c = ke.getKeyChar();
        System.out.println("Typed: " + c);
        curName = (c == ' ' || c == 0x0d || c == 0x0a)? "" : curName+c;
        setState();
        repaint();
    }
}
