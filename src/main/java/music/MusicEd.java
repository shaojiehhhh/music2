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
        Layer.createAll("BACK NOTE FORE".split(" "));
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
        if(PAGE != null){
            Glyph.CLEF_G.showAt(g, 8, 100, PAGE.margins.top + 4*8);
            Glyph.HEAD_HALF.showAt(g, 8, 200, PAGE.margins.top + 4*8);
            Glyph.HEAD_Q.showAt(g, 8, 200, PAGE.margins.top + 4*8);
//            int H = 32;
//            Glyph.HEAD_Q.showAt(g, H, 200, PAGE.margins.top + 4*H);
//            g.setColor(Color.red);
//            g.drawRect(200,PAGE.margins.top + 3*H, 24*H/10, 2*H);
        }
        g.setColor(Color.black);
        int H = 8, x1 = 100, x2 = 200;
        Beam.setMasterBeam(x1, 100+G.rnd(100), x2, 100+G.rnd(100));
        Beam.drawBeamstack(g,0,1,x1, x2, -H);
        g.setColor(Color.ORANGE);
        Beam.drawBeamstack(g,1,3,x1+10, x2-10, -H);

//        Beam.setPoly(100,100+G.rnd(100),200,100+G.rnd(100),8);
//        g.fillPolygon(Beam.poly);
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
