package music;

import global.UC;
import reactions.Gesture;
import reactions.Mass;
import reactions.Reaction;

import java.awt.*;

import static music.MusicEd.PAGE;

public class Staff extends Mass {
    public Sys sys; //the staff lives in system
    public int iStaff; //index of staff in system
    public Staff.Fmt fmt; //staff format

    public Staff(int iStaff, Staff.Fmt sf) {
        super("BACK");
        this.iStaff = iStaff;
        this.fmt = sf;
        //----------------------add new reaction: bar line-------------------
        addReaction(new Reaction("S-S") {
            @Override
            public int bid(Gesture g) {
                int x = g.vs.xM(), y1 = g.vs.yL(),y2 = g.vs.yM();
                if(x < PAGE.margins.left || x > PAGE.margins.right + UC.barToMarginSnap) {return UC.noBid;}
                int d = Math.abs(y1-Staff.this.yTop()) + Math.abs(y2-Staff.this.yBot()); //compute distance
                return (d < 30) ? d+UC.barToMarginSnap: UC.noBid;
            }

            @Override
            public void act(Gesture g) {
                new Bar(Staff.this.sys, g.vs.xM());
            }
        });
        //---------------------toggling bar continues------------------------
        addReaction(new Reaction("S-S") {
            @Override
            public int bid(Gesture g) {
                if(Staff.this.sys.iSys != 0) return UC.noBid;
                int y1 = g.vs.yL(), y2 = g.vs.yH();
                int iStaff = Staff.this.iStaff;
                if(iStaff == PAGE.sysFmt.size() - 1) return UC.noBid;
                if(Math.abs(y1 - Staff.this.yBot()) > 30) return UC.noBid;

                Staff nextStaff = sys.staffs.get(iStaff + 1);
                if(Math.abs(y2-nextStaff.yTop()) > 30) return UC.noBid;
                return 10;
            }

            @Override
            public void act(Gesture g) {
                PAGE.sysFmt.get(Staff.this.iStaff).toggleBarContinues();
            }
        });
    }

    public int sysOff() {return sys.fmt.staffOffset.get(iStaff);}
    public int yTop() {return sys.yTop() + sysOff();}
    public int yBot() {return yTop() + fmt.height();}

    @Override
    public void show(Graphics g) {}

    //-------------------Staff.Fmt-----------------------
    public static class Fmt {
        public int nLine = 5, H = UC.defaultStaffH; //H for half line
        public boolean barContinues = false;

        public int height() {
            return 2 * H * (nLine -1);
        }

        public void toggleBarContinues() {
            barContinues = !barContinues;
        }

        public void showAt(Graphics g, int y) {
            int left = PAGE.margins.left, right = PAGE.margins.right;
            for(int i  = 0; i < nLine; i ++) {
                g.drawLine(left, y+2*H*i, right, y+2*H*i);
            }
        }
    }
}
