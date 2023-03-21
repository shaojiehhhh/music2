package music;

import global.UC;
import reactions.Gesture;
import reactions.Mass;
import reactions.Reaction;

import java.awt.*;
import java.util.ArrayList;

public class Head extends Mass implements Comparable<Head>{//Node head
    public Glyph forcedGlyph = null; // null means use the normal Glyph calculated from the flag count
    public Staff staff;
    public int line;
    public Stem stem = null; // heads are created with no stem so the user can choose direction..
    public Time time; // .. so heads, in order to have x, have a time.
    public boolean wrongSide = false; // normally a head is NOT on the wrong side

    public Head(Staff staff, int x, int y){
        super("NOTE");
        this.staff = staff;
        this.line = staff.lineOfY(y);
        time = staff.sys.getTime(x); // get the time for this head.
        time.heads.add(this);

        addReaction(new Reaction("S-S"){ // Stem or unStem heads
            public int bid(Gesture g){
                int x = g.vs.xM(), y1 = g.vs.yL(), y2 = g.vs.yH();
                int W = Head.this.W(), hy = Head.this.y();
                if(y1 > y || y2 < y){return UC.noBid;} // heads not in y range reject this gesture
                int hl = Head.this.time.x, hr = hl + W; // left and right side of Head.
                if(x < hl-W || x > hr+W){return UC.noBid;} // must be reasonably close to the head.
                if(x < (hl+W/2)){return hl-x;}
                if(x > (hr-W/2)){return x-hr;}
                return UC.noBid;
            }
            public void act(Gesture g){
                int x = g.vs.xM(), y1 = g.vs.yL(), y2 = g.vs.yH(); // gesture locations
                Staff staff = Head.this.staff; // Head parameters
                Time t = Head.this.time;
                int W = Head.this.W();
                boolean up = x > (t.x+ W/2); //
                if(Head.this.stem == null){ // winner of bid gets to choose between stem or unStem action
                    t.stemHeads(staff, up, y1,y2); // staff and up needed to create the stem
                }else{
                    t.unStemHeads(y1,y2);
                }
            }
        });

        addReaction(new Reaction("DOT") {//add augmentation dots
            @Override
            public int bid(Gesture g) {
                int xH = Head.this.x(), yH = Head.this.y(), H = Head.this.staff.h(), W = Head.this.W();
                int x = g.vs.xM(), y = g.vs.yM();
                if(x < xH || x > xH + 2*W || y < yH - H || y > yH + H) {return UC.noBid;}
                return Math.abs(xH + W -x) + Math.abs(yH - y);
            }

            @Override
            public void act(Gesture g) {
                if(Head.this.stem != null) {
                    Head.this.stem.cycleDot();
                }
            }
        });
    }

    public int x(){
        int res = time.x;
        if(wrongSide) {
            res += (stem != null && stem.isUp) ? W() : -W();
        }
        return res;
    }
    public int y(){return staff.yLine(line);}
    public int W(){return 24*staff.h()/10;} // Width of a note head, RIGHT = LEFT + W();
    public Glyph normalGlyph(){
        if(stem == null) {return Glyph.HEAD_Q;}
        if(stem.nFlag == -1) {return Glyph.HEAD_HALF;}
        if(stem.nFlag == -2) {return Glyph.HEAD_W;}
        return Glyph.HEAD_Q;
    }
    public void deleteHead(){time.heads.remove(this);} //STUB

    @Override
    public void show(Graphics g) {
        int H = staff.h();
        g.setColor(wrongSide ? Color.green : Color.blue); //for debugging
        if(stem != null && stem.heads.size() != 0 && this == stem.firstHead()) {
            g.setColor(Color.red);
        }
        (forcedGlyph != null ? forcedGlyph : normalGlyph()).showAt(g, H, x(), y());
        if(stem != null) {
            int off = UC.gapRestToFirstDot, sp = UC.gapBetweenAugDots;
            for(int i = 0; i < stem.nDot; i++) {
                g.fillOval(time.x + off + i*sp, y() - 3*H/2, H*2/3, H*2/3);
            }
        }
    }

    public void unStem(){
        if(stem != null){  // if stem is null, this head is already unStemmed, otherwise...
            stem.heads.remove(this); // get out of old stem
            if(stem.heads.size() == 0){stem.deleteStem();} // and delete old stem if it becomes empty
            stem = null;
            wrongSide = false;
        }
    }

    public void joinStem(Stem s){
        if(stem != null){unStem();} // make sure that this head is NOT on some other stem..
        s.heads.add(this); // before it joins the Stem's heads list
        stem = s; // reference your stem - this head now has a stem.
    }

    @Override
    public int compareTo(Head h) {
        return (staff.iStaff != h.staff.iStaff) ? (staff.iStaff - h.staff.iStaff) : (line - h.line);
    }


    //----------------HEAD LIST---------------------
    public static class List extends ArrayList<Head> {

    }
}
