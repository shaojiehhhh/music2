package music;

import global.UC;
import reactions.Gesture;
import reactions.Reaction;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

public class Stem extends Duration implements Comparable<Stem>{
    public Staff staff;
    public Head.List heads = new Head.List();
    public boolean isUp = true;
    public Beam beam = null; //stem is not required to have beam on it (legal)

    public Stem(Staff staff, boolean up){
        this.staff = staff;
        isUp = up;
//        staff.sys.stems.addStem(this); //this should be done in time.stemHeads

        addReaction(new Reaction("E-E") {//inc FLAG on stem
            @Override
            public int bid(Gesture g) {
                return bidLineCrossStem(g.vs.yM(), g.vs.xL(), g.vs.xH(), Stem.this);
            }
            @Override
            public void act(Gesture g) {
                Stem.this.incFlag();
            }
        });

        addReaction(new Reaction("W-W") {//dec FLAG on stem
            @Override
            public int bid(Gesture g) {
                return bidLineCrossStem(g.vs.yM(), g.vs.xL(), g.vs.xH(), Stem.this);
            }
            @Override
            public void act(Gesture g) {
                Stem.this.decFlag();
            }
        });
    }

    public static int bidLineCrossStem(int y, int x1, int x2, Stem stem) {
        int xS = stem.heads.get(0).time.x; //x Stem
        if(x1 > xS || x2 < xS) return UC.noBid;
        int y1 = stem.yLo(), y2 = stem.yHi();
        if(y < y1 || y > y2) return UC.noBid;
        return Math.abs(y - (y1+y2)/2);
    }

    public int x(){Head h = firstHead(); return h.time.x + (isUp?h.W():0);}
    public Head firstHead(){return heads.get(isUp ? heads.size() - 1 : 0);}
    public Head lastHead(){return  heads.get(isUp ? 0 : heads.size() - 1);}
    public int yFirstHead(){Head h = firstHead(); return h.staff.yLine(h.line);}
    public int yLo(){return isUp ? yBeamEnd() : yFirstHead();}
    public int yHi(){return isUp ? yFirstHead() : yBeamEnd();}

    public int yBeamEnd(){
        Head h = lastHead();
        int line = h.line;
        line += (isUp? -7:7); // default is one octave from last head on the beam
        int flagInc = nFlag > 2 ? 2*(nFlag-2) :0; // if more than 2 flags we adjust stem end..
        line += (isUp? -flagInc : flagInc); // .. but direction of adjustment depends on up or down stem
        if((isUp && line > 4) || (!isUp && line < 4)){line = 4;} // meet center line if we must
        return h.staff.yLine(line);
    }

    public void deleteStem(){// only call if list of heads is empty
        staff.sys.stems.remove(this);
        deleteMass();
    }

    public void setWrongSides() {//call by time.stemHeads
        Collections.sort(heads);
        int i, last, next;
        if(isUp) {i = heads.size() - 1; last = 0; next = -1;
        }
        else {i = 0; last = heads.size() - 1; next = 1;
        }
        Head ph = heads.get(i);//previous head
        ph.wrongSide = false; //first head is always right
        while(i != last) {
            i += next;
            Head nh = heads.get(i);//now head
            nh.wrongSide = ((ph.staff == nh.staff) && Math.abs(nh.line - ph.line) <= 1 && !ph.wrongSide);
            ph = nh;
        }

    }

    @Override
    public void show(Graphics g) {
        if(nFlag >= -1 && heads.size() > 0){
            int h = staff.h(), yH = yFirstHead(), yB = yBeamEnd();
            g.drawLine(x(),yH,x(),yB);
            if(nFlag > 0){
                if(nFlag == 1){(isUp? Glyph.FLAG1D: Glyph.FLAG1U).showAt(g,h,x(),yB);}
                if(nFlag == 2){(isUp? Glyph.FLAG2D: Glyph.FLAG2U).showAt(g,h,x(),yB);}
                if(nFlag == 3){(isUp? Glyph.FLAG3D: Glyph.FLAG3U).showAt(g,h,x(),yB);}
                if(nFlag == 4){(isUp? Glyph.FLAG4D: Glyph.FLAG4U).showAt(g,h,x(),yB);}
            }
        }
    }

    @Override
    public int compareTo(Stem s) {
        return x() - s.x();
    }


    //-----------------------------List----------------------------//
    public static class List extends ArrayList<Stem> {
        public int yMin = 1000000, yMax = -1000000;
        public void addStem(Stem s) {
            add(s);
            if(s.yLo() < yMin) {yMin = s.yLo();}
            if(s.yHi() < yMax) {yMax = s.yHi();}
        }
        public void sort() {
            Collections.sort(this);
        }
    }
 }
