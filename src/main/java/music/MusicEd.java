package music;

import global.UC;
import graphicsLib.G;
import graphicsLib.Window;
import reactions.Gesture;
import reactions.Ink;
import reactions.Layer;
import reactions.Reaction;

import java.awt.*;
import java.awt.event.MouseEvent;

public class MusicEd extends Window {
    public static Page PAGE; //one single page - will create in gesture

    static {
        Layer.createAll("BACK FORE".split(" "));
//        new Layer("BACK");
//        new Layer("FORE");
    }
    //constructor
    public MusicEd() {
        super("Music Editor", UC.MAIN_WINDOW_WIDTH, UC.MAIN_WINDOW_HEIGHT);

        Reaction.initialReactions.addReaction(new Reaction("E-E") {
            @Override
            public int bid(Gesture g) {return 10;}

            @Override
            public void act(Gesture g) {
                int y = g.vs.yM();
                Sys.Fmt sf = new Sys.Fmt();
                PAGE = new Page(sf);
                PAGE.margins.top = y;
                PAGE.addNewSys();
                PAGE.addNewStaff(0);
                this.disable();
            }
        });
    }

    public void paintComponent(Graphics g) {
        G.clearBack(g);
        g.setColor(Color.green);
        Ink.BUFFER.show(g);
        Layer.ALL.show(g);
    }
    public void mousePressed(MouseEvent me) {
        Gesture.AREA.dn(me.getX(), me.getY());
        repaint();
    }
    public void mouseDragged(MouseEvent me) {
        Gesture.AREA.drag(me.getX(), me.getY());
        repaint();
    }
    public void mouseReleased(MouseEvent me) {
        Gesture.AREA.up(me.getX(), me.getY());
        repaint();
    }
    public static void main(String[] args) {
        (PANEL = new MusicEd()).launch();
    }
}
