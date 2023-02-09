package reactions;

import global.UC;
import graphicsLib.G;
import graphicsLib.Window;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class ShapeTrainer extends Window {
    public static String UNKNOWN = " <- this name is currently unknown.";
    public static String ILLEGAL = " <- this name is NOT a legal shape name.";
    public static String KNOWN = " <- this name is an known shape.";
    public static String curName = "";
    public static String curState = ILLEGAL;
    public static Shape.Prototype.List pList = null;

    public ShapeTrainer() {
        super("ShapeTrainer", 1000, 700);
    }
    public void setState(){
        curState = ((Shape.DataBase.isLegal(curName)) ? UNKNOWN : ILLEGAL);
        if(curState == UNKNOWN) {
            if(Shape.DB.containsKey(curName)) {
                curState = KNOWN;
                pList = Shape.DB.get(curName).prototypes;
            } else pList = null;
        }
    }

    public static void main(String[] args) {
        (PANEL = new ShapeTrainer()).launch();
    }
    public void paintComponent(Graphics g) {
        G.clearBack(g);
        g.setColor(Color.black);
        g.drawString(curName, 600, 30);
        g.drawString(curState, 700, 30);
        g.setColor(Color.red);
        Ink.BUFFER.show(g);
        if(pList != null) {pList.show(g);}

    }
    public void keyTyped(KeyEvent ke) {
        char c = ke.getKeyChar();
        System.out.println("Typed: " + c);
        curName = (c == ' ' || c == 0x0d || c == 0x0a)? "" : curName+c;
        if(c == 0x0d || c == 0x0a) {
            Shape.DataBase.save();
        }
        setState();
        repaint();
    }

    public void mousePressed(MouseEvent me) {Ink.BUFFER.dn(me.getX(), me.getY());repaint();}
    public void mouseDragged(MouseEvent me) {Ink.BUFFER.drag(me.getX(), me.getY());repaint();}
    public void mouseReleased(MouseEvent me) {
        Ink ink = new Ink();
        Shape.DB.train(curName, ink.norn);
        setState();
        Shape recoginzed = Shape.recognize(ink);
        if(recoginzed == Shape.DOT) {
            removePrototype(me.getX(), me.getY());
            return;
        }
        repaint();
//        if(curState != ILLEGAL) {
//            Ink ink = new Ink();
//            Shape.Prototype proto;
//            Shape recoginzed = Shape.recognize(ink);
//            if(recoginzed == Shape.DOT) {
//                removePrototype(me.getX(), me.getY());
//                return;
//            }
//            if(pList == null) {
//                Shape s = new Shape(curName);
//                Shape.DB.put(curName, s);
//                pList = s.prototypes;
//            }
//            if(pList.bestDist((ink.norn)) < UC.noMatchDist) {
//                //found match
//                proto = Shape.Prototype.List.bestMatch;
//                proto.blend(ink.norn);
//            } else {
//                //not good match
//                proto = new Shape.Prototype();
//                pList.add(proto);
//            }
//            //set State
//            setState();
//        }
//        repaint();
    }
    public void removePrototype(int x, int y) {
        if(pList == null) return ;
        int index = pList.hitProto(x, y);
        if(index > 0) {
            pList.remove(index);
        }
        repaint();
    }
}
