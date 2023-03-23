package music;

import reactions.Mass;
import java.awt.*;

public class Beam extends Mass {
    public Stem.List stems = new Stem.List();

    public Beam(Stem first, Stem last) {
        super("NOTE");
        stems.addStem(first);
        stems.addStem(last);
    }

    public Stem first(){return stems.get(0);}
    public Stem last(){return stems.get(stems.size()-1);}
    public void deleteBeam() {
        for(Stem s: stems) {s.beam = null;}
        deleteMass();
    }

    public void addStem(Stem s) {
        if(s.beam == null) {
            stems.add(s);
            s.beam = this;
            stems.sort();
        }
    }

    @Override
    public void show(Graphics g) {
        g.setColor(Color.black);
        drawBeamGroup(g);
    }

    public static int mX1, mY1, mX2, mY2; //coordinates for the Master Beam

    public static int yOfX(int x, int x1, int y1, int x2, int y2) {
        int dY = y2 - y1, dX = x2- x1;
        return (x-x1) * dY/dX + y1;
    }

    public static int yOfX(int x) {
        int dy = mY2-mY1, dx = mX2-mX1;
        return (x-mX1)*dy/dx + mY1;
    }

    public static void setMasterBeam(int x1, int y1, int x2, int y2) {
        mX1 = x1;
        mX2 = x2;
        mY1 = y1;
        mY2 = y2;
    }

    public void setMasterBeam() {
        mX1=first().x();
        mY1=first().yBeamEnd();
        mX2=last().x();
        mY2=last().yBeamEnd();
    }

    public static Polygon poly;
    static {int[] foo = {0,0,0,0};poly = new Polygon(foo,foo,4);} //constructor
    public static void setPoly(int x1, int y1, int x2, int y2, int h) {
        int[] a = poly.xpoints; a[0] = x1; a[1] = x2; a[2] = x2; a[3] = x1;
        a = poly.ypoints; a[0] = y1; a[1] = y2; a[2] = y2 + h; a[3] = y1 + h;
    }

    public static void drawBeamstack(Graphics g, int n1, int n2, int x1, int x2, int h) {
        int y1 = yOfX(x1), y2 = yOfX(x2);
        for(int i = n1; i < n2; i++) {
            setPoly(x1, y1+i*2*h, x2, y2+i*2*h, h);
            g.fillPolygon(poly);
        }
    }

    public void drawBeamGroup(Graphics g) {
        setMasterBeam();
        Stem firstStem = first();
        int H = firstStem.staff.h();
        int sH = firstStem.isUp ? H : -H;
        int nPrev = 0, nCur = firstStem.nFlag, nNext = stems.get(1).nFlag;
        int pX;
        int cX = firstStem.x();
        int bX = cX + 3*H; // beamlet on first stem runs from cX to bX
        if(nCur > nNext) {drawBeamstack(g, nNext, nCur, cX, bX, sH);}
        for(int cur = 1; cur < stems.size(); cur ++) {
            Stem sCur = stems.get(cur);
            pX = cX; //updating previous one with the current one
            cX = sCur.x();
            nPrev = nCur;
            nCur = nNext;
            nNext = (cur < (stems.size() - 1) ? stems.get(cur+1).nFlag : 0);
            int nBack = Math.min(nPrev, nCur);
            drawBeamstack(g, 0, nBack, pX, cX, sH); // draw beams back to previous stem
            if(nCur > nPrev && nCur > nNext) {//beamlets are required
                if(nPrev < nNext) {//beamlets lean toward side with more beams
                    bX = cX + 3 *H;
                    drawBeamstack(g, nNext, nCur, cX, bX, sH);
                } else {
                    bX = cX - 3*H;
                    drawBeamstack(g, nPrev, nCur, bX, cX, sH);
                }

            }
        }

    }
}
